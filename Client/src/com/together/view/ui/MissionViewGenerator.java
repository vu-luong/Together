package com.together.view.ui;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
		
		ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.progressBar1);
		pb.getProgressDrawable().setColorFilter(Color.parseColor("#80cbc4"), Mode.SRC_IN);
		
		TextView idText = (TextView) convertView.findViewById(R.id.idText);
		idText.setText("" + item.getId());
		
		TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
		nameText.setText(item.getOwnerName());
		
		TextView numcText = (TextView) convertView.findViewById(R.id.numcText);
		numcText.setText(item.getNumUsers() + "/");
		
		TextView numText = (TextView) convertView.findViewById(R.id.numText);
		numText.setText("" + item.getMinUsers());
		
		return convertView;
		
	}

}
