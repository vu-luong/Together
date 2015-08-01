package com.together.presenter;

import java.lang.ref.WeakReference;

import android.content.Context;

import com.together.common.ConfigurableOps;
import com.together.common.ContextView;
import com.together.model.services.WebsocketService;

public class BrowsePresenter implements ConfigurableOps<BrowsePresenter.View> {
	private final String TAG = getClass().getSimpleName();
	private Context mContext;
	private WebsocketService mSocketService;
	private WeakReference<View> mView;

	public interface View extends ContextView {

	}

	@Override
	public void onConfiguration(View view, boolean firstTimeIn) {
		if (firstTimeIn) {
			mView = new WeakReference<BrowsePresenter.View>(view);
			mContext = view.getApplicationContext();
		}
		
	}

}
