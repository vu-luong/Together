package com.together.view.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.together.R;
import com.together.pojo.ItemChat;

public class ChatViewGenerator implements ViewGenerator<ItemChat> {
	
	public interface Callback {
		void clap(ItemChat item, int position);
	}
	
	private Callback callback;

	private HashMap<String, Bitmap> mMap = new HashMap<String, Bitmap>();

	public ChatViewGenerator(Callback callback) {
		this.callback = callback;
		
	}

	@Override
	public View getView(final ItemChat item, LayoutInflater layoutInflater,
			View convertView, ViewGroup parent,final int position) {

		// if (convertView == null)

		if (item.getType() == ItemChat.ANSWER) {

			// - TODO
			if (item.isSelf()) {
				Log.i("TAG", "is self: " + item.isSelf());
				convertView = layoutInflater.inflate(R.layout.item_chat2,
						parent, false);
			} else
				convertView = layoutInflater.inflate(R.layout.item_chat,
						parent, false);
			
			ImageButton mImageTrue = (ImageButton) convertView.findViewById(R.id.trueIcon);
			if (!item.isCorrect()) {
				mImageTrue.setVisibility(View.INVISIBLE);
			}
			ImageButton voice = (ImageButton) convertView
					.findViewById(R.id.sound);
			voice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String url = item.getUrl();
					try {
						Log.i("TAG", item.getUrl());
						MediaPlayer mediaPlayer = new MediaPlayer();
						mediaPlayer
								.setAudioStreamType(AudioManager.STREAM_MUSIC);
						mediaPlayer.setDataSource(url);
						mediaPlayer.prepare(); // might take long! (for
												// buffering, etc)
						mediaPlayer.start();
					} catch (IllegalArgumentException | SecurityException
							| IllegalStateException | IOException e) {
						e.printStackTrace();
					}

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
			ImageButton mImageTrue = (ImageButton) convertView.findViewById(R.id.trueIcon);
			mImageTrue.setVisibility(View.INVISIBLE);


		}

		TextView name = (TextView) convertView.findViewById(R.id.nameText);
		TextView comment = (TextView) convertView.findViewById(R.id.comment);

		final CircularImageView mView = (CircularImageView) convertView
				.findViewById(R.id.icon);
		if (mMap.get(item.getUser_id()) != null) {
			mView.setImageBitmap(mMap.get(item.getUser_id()));
		} else {
			new AsyncTask<String, Void, Bitmap>() {

				@Override
				protected Bitmap doInBackground(String... params) {
					try {
						URL url = new URL(params[0]);
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						connection.setDoInput(true);
						connection.connect();
						InputStream input = connection.getInputStream();
						Bitmap myBitmap = BitmapFactory.decodeStream(input);
						return myBitmap;
					} catch (IOException e) {
						return null;
					}
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					mMap.put(item.getUser_id(), result);
					mView.setImageBitmap(result);
				}
			}.execute(item.getUser_avatar());
		}

		if (item.isSelf())
			name.setText("Me");
		else
			name.setText(item.getName());
		comment.setText(item.getMessage());
		
		ImageButton clapButton = (ImageButton) convertView.findViewById(R.id.clapButton);
		clapButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.clap(item, position);
			}
		});
		TextView clapNum = (TextView) convertView.findViewById(R.id.numClapText);
		clapNum.setText(String.valueOf(item.getClap()));
		
		return convertView;

	}

}
