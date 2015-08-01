package com.together.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.together.R;
import com.together.pojo.ItemBrowse;

public class BrowseViewGenerator implements ViewGenerator<ItemBrowse> {

	@Override
	public View getView(ItemBrowse item, LayoutInflater layoutInflater,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_browse, parent, false);
		}
		
		return convertView;
	}

}
