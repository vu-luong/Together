package com.together.socket;

import org.json.JSONObject;

import android.os.Handler;

public abstract class SocketObserver {
	
	protected Handler mHandler = new Handler();
	
	public SocketObserver() {
		
	}
	
	public void update(final JSONObject jsonObject) {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				onReceived(jsonObject);
			}
		});
	}
	
	protected abstract void onReceived(JSONObject jsonObject);
	
	
}
