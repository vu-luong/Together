package com.together.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.together.R;
import com.together.pojo.ItemFinish;

public class FinishViewGenerator implements ViewGenerator<ItemFinish>{

	@Override
	public View getView(ItemFinish item, LayoutInflater layoutInflater,
			View convertView, ViewGroup parent, int position) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_finish, parent, false);
		}
		
		return convertView;
	}

}
