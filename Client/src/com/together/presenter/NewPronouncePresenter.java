package com.together.presenter;

import java.lang.ref.WeakReference;

import org.json.JSONObject;

import android.content.Context;

import com.together.common.ConfigurableOps;
import com.together.common.ContextView;
import com.together.model.services.WebsocketService;
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
			
		}
	};

	public interface View extends ContextView {
		void completedNewRoom();
	}

	@Override
	public void onConfiguration(View view, boolean firstTimeIn) {
		mView = new WeakReference<NewPronouncePresenter.View>(view);
		if (firstTimeIn) {
			mContext = view.getApplicationContext();
			mSocketService = WebsocketService.getInstance();
		}
	}


}
