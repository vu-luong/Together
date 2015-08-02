package com.together.presenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;

import com.together.common.ConfigurableOps;
import com.together.common.ContextView;
import com.together.common.Utils;
import com.together.constants.Events;
import com.together.model.services.WebsocketService;
import com.together.pojo.ItemChat;
import com.together.socket.SocketObserver;

public class SpeakingPresenter implements
		ConfigurableOps<SpeakingPresenter.View> {

	private final String TAG = getClass().getSimpleName();
	private Context mContext;
	private WeakReference<View> mView;
	private WebsocketService mSocketService;
	private int mRoomID;

	private SocketObserver mChatObserver = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject jsonObject) {
			Log.i(TAG, jsonObject.toString());

			ItemChat itemChat = new ItemChat();

			try {

				if (jsonObject.getInt("mission_id") == mRoomID) {

					itemChat.setMessage(jsonObject.getString("message"));
					itemChat.setType(jsonObject.getInt("type"));
					itemChat.setName(jsonObject.getString("name"));
					itemChat.setUser_id(jsonObject.getString("user_id"));
					itemChat.setUser_avatar(jsonObject.getString("user_avatar"));
					if (itemChat.getType() == ItemChat.ANSWER) {
						itemChat.setUrl(jsonObject.getString("url"));
						itemChat.setCorrect(jsonObject.getBoolean("correct"));
					}

					if (itemChat.getUser_id().equals(mSocketService.getID())) {
						itemChat.setSelf(true);
					} else
						itemChat.setSelf(false);

					mView.get().addItemChat(itemChat);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	};
	
	private SocketObserver mFinishObserver = new SocketObserver() {
		
		@Override
		protected void onReceived(JSONObject jsonObject) {
			mView.get().finishMission(jsonObject.toString());
		}
	};

	private SocketObserver mGetCurrent = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject jsonObject) {
			try {
				int currentAnswered = jsonObject.getInt("currentAnswered");
				int numUsers = jsonObject.getInt("numUsers");
				String currentWord = jsonObject.getString("word");
				
				
				mView.get().updateProgress(currentAnswered, numUsers, currentWord);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	};

	private SocketObserver mLoadObserver = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject json) {
			try {
				JSONArray messages = json.getJSONArray("missionlog");

				for (int i = 0; i < messages.length(); i++) {
					JSONObject jsonObject = messages.getJSONObject(i);
					ItemChat itemChat = new ItemChat();
					itemChat.setMessage(jsonObject.getString("message"));
					itemChat.setType(jsonObject.getInt("type"));
					itemChat.setName(jsonObject.getString("name"));
					itemChat.setUser_id(jsonObject.getString("user_id"));
					itemChat.setUser_avatar(jsonObject.getString("user_avatar"));

					if (itemChat.getType() == ItemChat.ANSWER) {
						itemChat.setUrl(jsonObject.getString("url"));
					}

					if (itemChat.getUser_id().equals(mSocketService.getID())) {
						itemChat.setSelf(true);
					} else
						itemChat.setSelf(false);

					mView.get().addItemChat(itemChat);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	public interface View extends ContextView {
		void addItemChat(ItemChat item);

		void finishMission(String string);

		void updateProgress(int currentAnswered, int numUsers,
				String currentWord);
	}

	@Override
	public void onConfiguration(View view, boolean firstTimeIn) {
		mView = new WeakReference<SpeakingPresenter.View>(view);
		if (firstTimeIn) {
			mContext = view.getApplicationContext();
			mSocketService = WebsocketService.getInstance();

			// register observer
			if (mSocketService != null) {
				mSocketService.registerObserver(Events.CHAT, mChatObserver);
				mSocketService.registerObserver(
						Events.LOAD_MISSION_LOG_SUCCEED, mLoadObserver);
				mSocketService.registerObserver(
						Events.GET_CURRENT_WORD_SUCCEED, mGetCurrent);
				mSocketService.registerObserver(
						Events.MISSION_FINISHED, mFinishObserver);
			}

		}
	}

	
	public void joinMission(int mIdRoom) {
		this.mRoomID = mIdRoom;
		mSocketService.unregisterNotification(mRoomID);
		try {
			mSocketService.emitMessage(Events.JOIN_MISSION,
					new JSONObject().put("user_id", mSocketService.getID())
							.put("mission_id", mIdRoom));
			mSocketService.emitMessage(Events.LOAD_MISSION_LOG,
					new JSONObject().put("mission_id", mIdRoom));
			mSocketService.emitMessage(Events.GET_CURRENT_WORD,
					new JSONObject().put("mission_id", mIdRoom));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void unregisterAndWaitNotification() {
		// unregister
		mSocketService.unregisterObserver(Events.CHAT, mChatObserver);
		mSocketService.unregisterObserver(Events.LOAD_MISSION_LOG_SUCCEED,
				mLoadObserver);
		mSocketService.unregisterObserver(Events.GET_CURRENT_WORD_SUCCEED, 
				mGetCurrent);
		
		// TODO --

		// set up notification
		mSocketService.registerNotification(mRoomID);
	}

	public void sendMessage(String message) {
		ItemChat item = new ItemChat();

		item.setMessage(message);
		item.setMission_id(mRoomID);
		item.setUser_id(mSocketService.getID());
		item.setType(ItemChat.CHAT);

		mSocketService.emitMessage(Events.CHAT, item);

	}

	public void onAnswer(Intent data) {
		new UploadVoiceTask().execute(data);

	}

	private class UploadVoiceTask extends AsyncTask<Intent, Void, ItemChat> {

		@Override
		protected ItemChat doInBackground(Intent... params) {
			Intent intent = params[0];
			// the resulting text is in the getExtras:
			Bundle bundle = intent.getExtras();
			final ArrayList<String> matches = bundle
					.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
			// the recording url is in getData:
			Uri mCurrentURI = intent.getData();
			
			if (mCurrentURI == null) 
				return null;
			try {
				File file = new File(mContext.getCacheDir(), "temp.amr");
				InputStream is = mContext.getContentResolver().openInputStream(
						mCurrentURI);
				OutputStream os = new FileOutputStream(file);
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = is.read(buf)) > 0) {
					os.write(buf, 0, len);
				}
				os.close();

				mCurrentURI = Uri.fromFile(file);
				Log.i(TAG, mCurrentURI.toString());
				String filePath = Utils.uploadFile(file);

				if (filePath != null) {
					Log.i(TAG, filePath);
					ItemChat item = new ItemChat();
					item.setMessage(matches.get(0));
					item.setType(ItemChat.ANSWER);
					item.setMission_id(mRoomID);
					item.setUrl(filePath);
					item.setUser_id(mSocketService.getID());

					return item;
				} else
					return null;

			} catch (FileNotFoundException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(ItemChat result) {
			if (result != null)
				mSocketService.emitMessage(Events.CHAT, result);
		}
	}
}
