package com.together.view.activity;

import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.together.R;
import com.together.common.GenericActivity;
import com.together.constants.Constants;
import com.together.model.services.WebsocketService;
import com.together.presenter.LoginPresenter;
import com.together.view.fragment.NavigationFragment;

public class LoginActivity extends
		GenericActivity<LoginPresenter.View, LoginPresenter>
		implements LoginPresenter.View, FacebookCallback<LoginResult> {
	
	private LoginButton mLoginButton;
	private CallbackManager mCallback;

	public static Intent makeIntent(Context context) {
		return new Intent(context, LoginActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		FacebookSdk.sdkInitialize(getApplicationContext());
		startService(WebsocketService.makeIntent(
				getApplicationContext()));
		setContentView(R.layout.activity_login);
		
		// Initialize view
		initializeView();
		mCallback = CallbackManager.Factory.create();
		mLoginButton = (LoginButton) findViewById(R.id.login_button);
		mLoginButton.setReadPermissions(Arrays.asList("user_friends", "email"));
		mLoginButton.registerCallback(mCallback, this);
				

		super.onCreate(savedInstanceState,
				LoginPresenter.class, this);
	}
	
	private void initializeView() {
		
	}
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mCallback.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSuccess(LoginResult result) {
		Log.i(TAG, "onSuccess login facebook");
		getOps().login(Profile.getCurrentProfile());
		
//		/* make the API call */
//		new GraphRequest(
//		    AccessToken.getCurrentAccessToken(),
//		    "/me/friends",
//		    null,
//		    HttpMethod.GET,
//		    new GraphRequest.Callback() {
//		        public void onCompleted(GraphResponse response) {
//		            Log.i(TAG, response.getJSONObject().toString()); 
//		        }
//		    }
//		).executeAsync();
		
	}

	@Override
	public void onCancel() {
		
	}

	@Override
	public void onError(FacebookException error) {
		
	}

	@Override
	public void loginCompleted() {
		Intent intent = MainActivity.makeIntent(getApplicationContext());
		startActivity(intent);
	}
	
}
