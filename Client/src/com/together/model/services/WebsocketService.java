package com.together.model.services;

import java.lang.ref.WeakReference;
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
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter.Listener;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.together.constants.Constants;
import com.together.constants.Events;
import com.together.socket.SocketObserver;
import com.together.view.activity.SpeakingActivity;

public class WebsocketService extends Service {

	private static final String TAG = WebsocketService.class.getSimpleName();

	private static final int NOTIFICATION_ID = 43;
	private Socket mSocket;
	private HashMap<String, List<WeakReference<SocketObserver>>> mEventMap;
	
	private String mID;
	
	private SocketObserver mChatObserver = new SocketObserver() {
		protected void onReceived(JSONObject jsonObject) {
			// TODO - push chat notification
		}
	};
	
	private SocketObserver mLoginObserver = new SocketObserver() {
		
		@Override
		protected void onReceived(JSONObject jsonObject) {
			
			try {
				mID = jsonObject.getString("id");
				SharedPreferences.Editor pref = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE)
						.edit();
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

		mEventMap = new HashMap<String, List<WeakReference<SocketObserver>>>();

		// initialize event and equivalent listener
		for (String event : Events.EVENTS) {
			List<WeakReference<SocketObserver>> messengers = new ArrayList<WeakReference<SocketObserver>>();
			mEventMap.put(event, messengers);
		}

		for (final String event : Events.EVENTS) {
			mSocket.on(event, new Listener() {

				@Override
				public void call(Object... arg0) {
					List<WeakReference<SocketObserver>> observers = mEventMap
							.get(event);
					Log.i(TAG, event + " emit to " + observers.size()
							+ " observer(s) ");
					for (WeakReference<SocketObserver> observer : observers) {
						JSONObject jsonObject = (JSONObject) arg0[0];
						if (observer.get() != null)
							observer.get().update(jsonObject);
					}
				}
			});
		}

		Log.i(TAG, "On Created Service");

		mSocket.connect();

		setUpReconnect();

		registerObserver(Events.CHAT, mChatObserver);
		registerObserver(Events.LOGIN_SUCCEED, mLoginObserver);
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

		List<WeakReference<SocketObserver>> observers = mEventMap.get(event);
		if (!observers.contains(new WeakReference<SocketObserver>(observer)))
			observers.add(new WeakReference<SocketObserver>(observer));
	}

	public void unregisterObserver(String event, SocketObserver observer) {
		List<WeakReference<SocketObserver>> observers = mEventMap.get(event);
		for (int i = 0; i < observers.size(); i++) {
			if (observers.get(i).get() == observer) {
				observers.remove(i);
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
	private void startNotification(int roomID, String message) {
		// Gets access to the Android Notification Service.
		mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = SpeakingActivity.makeIntent(
				getApplicationContext(), roomID);
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, notificationIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		// Create the Notification and set a progress indicator for an
		// operation of indeterminate length.
		mBuilder = new NotificationCompat.Builder(this)
				.setContentIntent(contentIntent)
				.setContentTitle("Incomming from: " + roomID)
				.setContentText(message)
				.setSmallIcon(android.R.drawable.stat_sys_upload);

		// Build and issue the notification.
		mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy()");
	}
	
	public void registerNotification() {
		// TODO - register notification after quit activiy
	}

	public String getID() {
		return mID;
	}


}
