package com.together.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.together.R;
import com.together.pojo.ItemBrowse;
import com.together.pojo.Mission;

public class BrowseViewGenerator implements ViewGenerator<Mission> {

	public interface Callback {
		void joinRoom(Mission item);
	}
	
	private Callback mCallback;
	
	public BrowseViewGenerator(Callback cb) {
		mCallback = cb;
	}
	
	
	@Override
	public View getView(Mission item, LayoutInflater layoutInflater,
			View convertView, ViewGroup parent, int position) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_browse, parent, false);
		}
		
		TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
		nameText.setText(item.getOwnerName());
		
		TextView idText = (TextView) convertView.findViewById(R.id.idText);
		idText.setText(String.valueOf(item.getId()));
		
		final Mission itemf = item;
		
		ImageView mJoinButton = (ImageView) convertView.findViewById(R.id.joinButton);
		mJoinButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCallback.joinRoom(itemf);
				
			}
		});
		
		return convertView;
	}

}
