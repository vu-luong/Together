package com.together.view.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GenericAdapter<Item> extends BaseAdapter {

	protected List<Item> items = new ArrayList<Item>();
	protected Context mContext;
	protected ViewGenerator<Item> mViewGenerator;
	
	public GenericAdapter(Context context, ViewGenerator<Item> viewGenerator) {
		mContext = context;
		mViewGenerator = viewGenerator;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Item getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);
		Item item = items.get(position);
		
		return mViewGenerator.getView(item, mLayoutInflater, 
				convertView, parent);
	}
	
	public void add(Item item) {
		items.add(item);
		notifyDataSetChanged();
	}
	
	public void setItemList(List<Item> items) { 
		this.items = items;
		notifyDataSetChanged();
	}

}
