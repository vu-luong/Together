package com.together.presenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.Profile;
import com.together.common.ConfigurableOps;
import com.together.common.ContextView;
import com.together.constants.Events;
import com.together.model.services.WebsocketService;
import com.together.pojo.Mission;
import com.together.socket.SocketObserver;
import com.together.view.activity.SpeakingActivity;

public class BrowsePresenter implements ConfigurableOps<BrowsePresenter.View> {
	private final String TAG = getClass().getSimpleName();
	private Context mContext;
	private WebsocketService mSocketService;
	private WeakReference<View> mView;
	
	
	private SocketObserver mGetMissionObserver = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject jsonObject) {
			Log.i(TAG, jsonObject.toString());
			try {
				List<Mission> list = new ArrayList<Mission>();
				JSONArray jsonArray = jsonObject.getJSONArray("missions");
				for (int i = 0; i < jsonArray.length(); i++) {
					// - TODO - add more value
					JSONObject json = jsonArray.getJSONObject(i);
					Mission mission = new Mission();
					mission.setId(json.getInt("id"));
					mission.setType(json.getInt("type"));
					mission.setOwnerName(json.getString("owner_name"));
					mission.setOwnerId(json.getString("owner_id"));
					
					if (!mission.getOwnerId().equals(Profile.getCurrentProfile().getId())) {
						list.add(mission);
					}
						mView.get().changeItemMission(list);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};


	public interface View extends ContextView {

		void changeItemMission(List<Mission> list);
		

	}

	@Override
	public void onConfiguration(View view, boolean firstTimeIn) {
		if (firstTimeIn) {
			mView = new WeakReference<BrowsePresenter.View>(view);
			mContext = view.getApplicationContext();
			mSocketService = WebsocketService.getInstance();
			mSocketService.registerObserver(Events.GET_ALL_MISSIONS_SUCCEED, mGetMissionObserver);
			getAllRoom();
			
			Log.i(TAG, "den day roi");
		}
		
	}

	private void getAllRoom() {
		Log.i(TAG, "get all rooms");
		mSocketService.emitMessage(Events.GET_ALL_MISSIONS, new JSONObject());
	}

	public void unRegister() {
		mSocketService.unregisterObserver(Events.GET_ALL_MISSIONS_SUCCEED, mGetMissionObserver);
		
	}

	public void getMissions() {
		getAllRoom();
	}


}
