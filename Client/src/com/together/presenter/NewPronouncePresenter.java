package com.together.presenter;

import java.lang.ref.WeakReference;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.together.common.ConfigurableOps;
import com.together.common.ContextView;
import com.together.constants.Events;
import com.together.model.services.WebsocketService;
import com.together.pojo.Mission;
import com.together.socket.SocketObserver;

public class NewPronouncePresenter implements
		ConfigurableOps<NewPronouncePresenter.View> {
	
	private final String TAG = getClass().getSimpleName();
	private Context mContext;
	private WebsocketService mSocketService;
	private WeakReference<View> mView;
	
	private SocketObserver mGenerateObserver = new SocketObserver() {
		
		@Override
		protected void onReceived(JSONObject jsonObject) {
			JSONArray words;
			try {
				words = jsonObject.getJSONArray("words");
				for (int i = 0; i < words.length(); i++) {
					mView.get().addWord(words.getString(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	};
	
	
	private SocketObserver mNewMissionSucceedObserver = new SocketObserver() {
		
		@Override
		protected void onReceived(JSONObject jsonObject) {
			
			mView.get().completedNewRoom();
		}
	};

	public interface View extends ContextView {
		void completedNewRoom();
		void addWord(String word);
	}

	@Override
	public void onConfiguration(View view, boolean firstTimeIn) {
		mView = new WeakReference<NewPronouncePresenter.View>(view);
		if (firstTimeIn) {
			mContext = view.getApplicationContext();
			mSocketService = WebsocketService.getInstance();
			mSocketService.registerObserver(Events.GENERATE_WORDS_SUCCEED, mGenerateObserver);
			mSocketService.registerObserver(Events.CREATE_MISSION_SUCCEED, mNewMissionSucceedObserver);
			mSocketService.emitMessage(Events.GENERATE_WORDS, new JSONObject());
		}
	}

	public void createNewMission(List<String> savedWords, int minUsers) {
		Mission mission = new Mission();
		mission.setWords(savedWords);
		mission.setOwnerId(mSocketService.getID());
		mission.setType(Mission.SPEAK);
		mission.setMinUsers(minUsers);
		
		mSocketService.emitMessage(Events.NEW_MISSION, mission);
	}

	public void unRegister() {
		mSocketService.unregisterObserver(Events.GENERATE_WORDS_SUCCEED, mGenerateObserver);
		mSocketService.unregisterObserver(Events.CREATE_MISSION_SUCCEED, mNewMissionSucceedObserver);
		
	}


}
