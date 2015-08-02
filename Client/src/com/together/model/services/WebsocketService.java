package com.together.model.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter.Listener;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.together.R;
import com.together.constants.Constants;
import com.together.constants.Events;
import com.together.pojo.ItemChat;
import com.together.socket.SocketObserver;
import com.together.view.activity.SpeakingActivity;

public class WebsocketService extends Service {

	private static final String TAG = WebsocketService.class.getSimpleName();
	private static Handler mHandler = new Handler();

	private static final int NOTIFICATION_ID = 43;
	private Socket mSocket;
	private HashMap<String, List<SocketObserver>> mEventMap;
	private int mCurrentMission;

	private String mID;

	private SocketObserver mPushWordObserver = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject jsonObject) {
			
		}
	};

	private SocketObserver mChatObserver = new SocketObserver() {
		protected void onReceived(JSONObject jsonObject) {
			try {
				if (jsonObject.getString("user_id").equals(mID)
						|| jsonObject.getInt("mission_id") == mCurrentMission)
					return;

				int roomID = jsonObject.getInt("mission_id");
				String user_name = jsonObject.getString("name");
				String message = jsonObject.getString("message");

				if (jsonObject.getInt("type") == ItemChat.CHAT)
					startNotification(roomID, user_name, message);
				else 
					startNotification(roomID, user_name + " has just answered", message);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	};

	private SocketObserver mMissionStartObserver = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject jsonObject) {
			try {
				int roomID = jsonObject.getInt("mission_id");
				startNotification(roomID, "Mission #" + roomID + "has just started" + roomID,
						"Click to open");
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	};

	private SocketObserver mJoinRoomObserver = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject jsonObject) {
			try {
				int roomID = jsonObject.getInt("mission_id");
				String user_name = jsonObject.getString("newly_joined_name");
				startNotification(roomID, user_name + "has just joined mission #" + roomID, " ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	private SocketObserver mLoginObserver = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject jsonObject) {

			try {
				mID = jsonObject.getString("id");
				SharedPreferences.Editor pref = getSharedPreferences(
						Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
				pref.putString(Constants.ID_KEY, mID);
				pref.commit();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	public class LocalBinder extends Binder {
		public WebsocketService getService() {
			return WebsocketService.this;
		}
	}

	private LocalBinder mLocalBinder = new LocalBinder();

	/**
	 * Manages the Notification displayed in System UI.
	 */
	private NotificationManager mNotifyManager;

	/**
	 * Builder used to build the Notification.
	 */
	private Builder mBuilder;

	/**
	 * 
	 */
	private static WebsocketService mInstance;

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */

	public static Intent makeIntent(Context context) {
		return new Intent(context, WebsocketService.class);
	}

	public static Handler getHandler() {
		return mHandler;
	}

	public static WebsocketService getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			mSocket = IO.socket(Constants.SOCKET_SERVER);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		mEventMap = new HashMap<String, List<SocketObserver>>();

		// initialize event and equivalent listener
		for (String event : Events.EVENTS) {
			List<SocketObserver> messengers = new ArrayList<SocketObserver>();
			mEventMap.put(event, messengers);
		}

		for (final String event : Events.EVENTS) {
			mSocket.on(event, new Listener() {

				@Override
				public void call(Object... arg0) {
					List<SocketObserver> observers = mEventMap.get(event);
					Log.i(TAG, event + " emit to " + observers.size()
							+ " observer(s) ");
					for (SocketObserver observer : observers) {
						if (String.class.isInstance(arg0[0])) {
							if (observer != null) {
								Log.i(TAG, arg0[0].toString());
							}
						} else {
							JSONObject jsonObject = (JSONObject) arg0[0];
							if (observer != null)
								observer.update(jsonObject);
						}
					}
				}
			});
		}

		Log.i(TAG, "On Created Service");

		mSocket.connect();

		setUpReconnect();

		registerObserver(Events.LOGIN_SUCCEED, mLoginObserver);
		registerObserver(Events.PEOPLE_JOIN_MISSION, mJoinRoomObserver);
		registerObserver(Events.CHAT, mChatObserver);
		registerObserver(Events.MISSION_START, mMissionStartObserver);
		registerObserver(Events.PUSH_WORD, mPushWordObserver);

		mSocket.on(Events.CONNECTED, new Listener() {

			@Override
			public void call(Object... arg0) {
				Log.i(TAG, "reconnect + login");
				setUpReconnect();
			}
		});

		// TODO
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					setUpReconnect();
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		mInstance = this;

	}

	private void setUpReconnect() {
		SharedPreferences pref = getSharedPreferences(Constants.MY_PREFS_NAME,
				Context.MODE_PRIVATE);
		String id = pref.getString(Constants.ID_KEY, null);

		if (id != null) {
			try {
				Log.i(TAG, "reconnecting");
				emitMessage(Events.RELOGIN, new JSONObject().put("id", id));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mInstance = this;
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mLocalBinder;
	}

	public void registerObserver(String event, SocketObserver observer) {

		List<SocketObserver> observers = mEventMap.get(event);
		if (!observers.contains(observer))
			observers.add(observer);
	}

	public void unregisterObserver(String event, SocketObserver observer) {
		List<SocketObserver> observers = mEventMap.get(event);
		for (int i = 0; i < observers.size(); i++) {
			if (observers.get(i).equals(observer)) {
				observers.remove(i);
				break;
			}
		}
	}

	public void emitMessage(String event, Object message) {
		if (JSONObject.class.isInstance(message)) {
			Log.i(TAG, message.toString());
			mSocket.emit(event, message);
			return;
		}
		Gson gson = new Gson();
		String mes = gson.toJson(message);
		try {

			Log.i(TAG, mes);
			JSONObject jsonObject = new JSONObject(mes);
			mSocket.emit(event, jsonObject);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Starts the Notification to show the progress of video upload.
	 */
	private void startNotification(int roomID, String title, String message) {
		// Gets access to the Android Notification Service.
		mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		Intent notificationIntent = SpeakingActivity.makeIntent(
				getApplicationContext(), roomID);
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, notificationIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		// Create the Notification and set a progress indicator for an
		// operation of indeterminate length.
		mBuilder = new NotificationCompat.Builder(this)
				.setContentIntent(contentIntent).setContentTitle(title)
				.setContentText(message).setSound(alarmSound)
				.setSmallIcon(R.drawable.ico_prono_browser_mission);

		// Build and issue the notification.
		mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy()");
	}

	public void registerNotification(int missionId) {
		mCurrentMission = -1;
	}

	public void unregisterNotification(int missionId) {
		mCurrentMission = missionId;
	}

	public String getID() {
		return mID;
	}

}
