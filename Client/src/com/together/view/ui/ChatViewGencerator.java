package com.together.view.ui;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.together.R;
import com.together.pojo.ItemChat;

public class ChatViewGencerator implements ViewGenerator<ItemChat> {

	private MediaPlayer mediaPlayer;

	public ChatViewGencerator() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}

	@Override
	public View getView(final ItemChat item, LayoutInflater layoutInflater,
			View convertView, ViewGroup parent) {

		// if (convertView == null)

		if (item.getType() == ItemChat.ANSWER) {
			if (item.isSelf()) {
				Log.i("TAG", "is self: " + item.isSelf());
				convertView = layoutInflater.inflate(R.layout.item_chat2,
						parent, false);
			} else
				convertView = layoutInflater.inflate(R.layout.item_chat,
						parent, false);
			
			ImageButton voice = (ImageButton) convertView
					.findViewById(R.id.sound);
			voice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String url = item.getUrl();
					try {
						mediaPlayer.setDataSource(url);
						mediaPlayer.prepare(); // might take long! (for
												// buffering, etc)
					} catch (IllegalArgumentException | SecurityException
							| IllegalStateException | IOException e) {
						e.printStackTrace();
					}
					mediaPlayer.start();

				}
			});

		} else {

			// @@ - TODO - ANother view
			if (item.isSelf()) {
				Log.i("TAG", "is self: " + item.isSelf());
				convertView = layoutInflater.inflate(R.layout.item_chat3,
						parent, false);
			} else
				convertView = layoutInflater.inflate(R.layout.item_chat1,
						parent, false);

		}

		TextView name = (TextView) convertView.findViewById(R.id.nameText);
		TextView comment = (TextView) convertView.findViewById(R.id.comment);
		
		if (item.isSelf()) name.setText("Me");
		else name.setText(item.getName());
		comment.setText(item.getMessage());

		return convertView;

	}

}
