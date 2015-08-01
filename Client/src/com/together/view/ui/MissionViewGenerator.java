package com.together.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.together.R;
import com.together.pojo.Mission;

public class MissionViewGenerator implements ViewGenerator<Mission>{

	@Override
	public View getView(Mission item, LayoutInflater layoutInflater,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_execute, parent, false);
		}
		
		TextView itemTitle = (TextView) convertView.findViewById(R.id.itemTittle);
		itemTitle.setText(item.getName());
		
		TextView idText = (TextView) convertView.findViewById(R.id.idText);
		idText.setText("ID: " + item.getId());
		
		return convertView;
		
	}

}
