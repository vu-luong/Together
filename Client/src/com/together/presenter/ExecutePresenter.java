package com.together.presenter;

import java.lang.ref.WeakReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

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

					JSONObject json = jsonArray.getJSONObject(i);
					Mission mission = new Mission();
					mission.setId(json.getInt("id"));
					mission.setName(json.getString("name"));
					mission.setType(json.getInt("type"));

					mView.get().addItemMission(mission);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	private SocketObserver mNewMissionObserver = new SocketObserver() {

		@Override
		protected void onReceived(JSONObject jsonObject) {
			// TODO Auto-generated method stub

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
			mSocketService.registerObserver(Events.GET_MY_MISSION_SUCCEED,
					mGetMissionObserver);
			mSocketService.registerObserver(Events.NEW_MISSION_SUCCEED,
					mNewMissionObserver);
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

}
