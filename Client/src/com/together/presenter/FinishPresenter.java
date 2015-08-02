package com.together.presenter;

import com.together.common.ConfigurableOps;
import com.together.common.ContextView;

public class FinishPresenter implements ConfigurableOps<FinishPresenter.View>{

	
	public interface View extends ContextView {
		
	}

	@Override
	public void onConfiguration(View view, boolean firstTimeIn) {
		
	}
	
}
