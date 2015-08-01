package com.together.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.together.R;
import com.together.pojo.ItemInvite;

public class InviteViewGenerator implements ViewGenerator<ItemInvite> {

	@Override
	public View getView(ItemInvite item, LayoutInflater layoutInflater,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_invite, parent, false);
		}
		
		return convertView;
	}
}
