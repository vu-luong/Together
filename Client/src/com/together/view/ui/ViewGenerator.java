package com.together.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ViewGenerator<Item> {
	View getView(Item item, LayoutInflater layoutInflater,
			View convertView, ViewGroup parent, int position);
}
