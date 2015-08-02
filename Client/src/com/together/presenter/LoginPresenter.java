package com.together.presenter;

import java.lang.ref.WeakReference;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.facebook.Profile;
import com.together.common.ConfigurableOps;
import com.together.common.ContextView;
import com.together.constants.Events;
import com.together.model.services.WebsocketService;
import com.together.pojo.User;
import com.together.socket.SocketObserver;

public class LoginPresenter implements ConfigurableOps<LoginPresenter.View> {
	
	private static Handler mHandler = new Handler();
	private Context mContext;
	private WebsocketService mSocketService;
	private WeakReference<View> mView;
	

	
	private SocketObserver mLoginObserver = new SocketObserver() {
		
		@Override
		protected void onReceived(JSONObject jsonObject) {
			// TODO - handle login succeed
			mView.get().loginCompleted();
		}
	};

	public interface View extends ContextView {
		void loginCompleted();
	}

	@Override
	public void onConfiguration(View view, boolean firstTimeIn) {
		mView = new WeakReference<View>(view);
		
		if (firstTimeIn) {
			mContext = view.getApplicationContext();
		}
	}
	
	public void login(Profile profile) {
		mSocketService = WebsocketService.getInstance();
		mSocketService.registerObserver(Events.LOGIN_SUCCEED, mLoginObserver);
		// TODO - emit login/register message
		User user = new User();
		user.setName(profile.getName());
		user.setId(profile.getId());
		user.setAvatar(profile.getProfilePictureUri(100, 100)
				.toString());
		mSocketService.emitMessage(Events.LOGIN, user);
	}

	public void unRegister() {
		mSocketService.unregisterObserver(Events.LOGIN_SUCCEED, mLoginObserver);
	}

}
