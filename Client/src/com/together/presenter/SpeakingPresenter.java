package com.together.presenter;

import com.together.common.ConfigurableOps;
import com.together.common.ContextView;
import com.together.pojo.ItemChat;

public class SpeakingPresenter implements
		ConfigurableOps<SpeakingPresenter.View> {

	public interface View extends ContextView {

		void addItemChat(ItemChat item);
	}

	@Override
	public void onConfiguration(View view, boolean firstTimeIn) {

	}

}
