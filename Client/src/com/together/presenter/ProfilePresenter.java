package com.together.presenter;

import com.together.common.ConfigurableOps;
import com.together.common.ContextView;

public class ProfilePresenter implements ConfigurableOps<ProfilePresenter.View>{

	
	public interface View extends ContextView {
		
	}

	@Override
	public void onConfiguration(View view, boolean firstTimeIn) {
		
	}
	
}
