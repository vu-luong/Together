package com.together.presenter;

import java.lang.ref.WeakReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.facebook.Profile;
import com.together.common.ConfigurableOps;
import com.together.common.ContextView;
import com.together.constants.Events;
import com.together.model.services.WebsocketService;
import com.together.pojo.Mission;
import com.together.socket.SocketObserver;

public class ExecutePresenter implements ConfigurableOps<ExecutePresenter.View> {

	private final String TAG = getClass().getSimpleName();
	private Context mContext;
	private WebsocketService mSocketService;
	private WeakReference<View> mView;

	private SocketObserver mGetMissionObserver = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject jsonObject) {
			try {
				JSONArray jsonArray = jsonObject.getJSONArray("missions");
				Log.i(TAG, "got " + jsonArray.length() + " missions");
				for (int i = 0; i < jsonArray.length(); i++) {
					// TODO - add more detail
					JSONObject json = jsonArray.getJSONObject(i);
					Mission mission = new Mission();
					mission.setId(json.getInt("id"));
					mission.setType(json.getInt("type"));
					mission.setOwnerName(json.getString("owner_name"));
					mission.setMinUsers(json.getInt("minUsers"));
					mission.setNumUsers(json.getInt("numUsers"));
					mView.get().addItemMission(mission);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	private SocketObserver mJoinRoomSucess = new SocketObserver() {
		@Override
		protected void onReceived(JSONObject jsonObject) {

		}
	};

	private SocketObserver mNewMissionObserver = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject jsonObject) {
			Log.i(TAG, jsonObject.toString());

			try {
				Mission mission = new Mission();

				mission.setId(jsonObject.getInt("id"));
				mission.setType(jsonObject.getInt("type"));
				mission.setOwnerName(jsonObject.getString("owner_name"));
				mission.setOwnerId(jsonObject.getString("owner_id"));
				mission.setMinUsers(jsonObject.getInt("minUsers"));
				mission.setNumUsers(jsonObject.getInt("numUsers"));

				if (mission.getOwnerId().equals(
						Profile.getCurrentProfile().getId()))
					mView.get().addItemMission(mission);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	public interface View extends ContextView {

		void addItemMission(Mission itemRoom);

	}

	@Override
	public void onConfiguration(View view, boolean firstTimeIn) {
		mView = new WeakReference<ExecutePresenter.View>(view);
		if (firstTimeIn) {
			mContext = view.getApplicationContext();
			mSocketService = WebsocketService.getInstance();

			// register observers
			if (mSocketService != null) {
				mSocketService.registerObserver(Events.GET_MY_MISSION_SUCCEED,
						mGetMissionObserver);
				mSocketService.registerObserver(Events.NEW_MISSION_SUCCEED,
						mNewMissionObserver);
			}
		}
	}

	public void getMyMission() {
		String id = mSocketService.getID();
		try {
			mSocketService.emitMessage(Events.GET_MY_MISSION,
					new JSONObject().put("user_id", id));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void unRegister() {
		mSocketService.unregisterObserver(Events.GET_MY_MISSION_SUCCEED,
				mGetMissionObserver);
		mSocketService.unregisterObserver(Events.NEW_MISSION_SUCCEED,
				mNewMissionObserver);
	}

}
