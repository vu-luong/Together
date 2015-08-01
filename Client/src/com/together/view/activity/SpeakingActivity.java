package com.together.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.together.R;
import com.together.common.GenericActivity;
import com.together.pojo.ItemChat;
import com.together.presenter.SpeakingPresenter;
import com.together.view.ui.ChatViewGencerator;
import com.together.view.ui.GenericAdapter;

public class SpeakingActivity extends
		GenericActivity<SpeakingPresenter.View, SpeakingPresenter> implements
		SpeakingPresenter.View {

	private static final String ROOM_ID = "id";

	public static Intent makeIntent(Context context, int id) {
		return new Intent(context, SpeakingActivity.class)
				.putExtra(ROOM_ID, id);
	}

	public static final int VIEW_ANSWER_STATE = 17;
	public static final int VIEW_ALL_STATE = 19;
	private static final int ACTION_SPEECH_TO_TEXT = 24;

	private GenericAdapter<ItemChat> mChatAdapter;

	private ListView mChatList;
	private ImageButton mChangeListButton;
	private ImageButton mSendButton;
	private EditText mEditText;
	private int mIdRoom;

	private int state = VIEW_ALL_STATE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_speaking);

		mIdRoom = getIntent().getIntExtra(ROOM_ID, 0);

		// Initialize view
		initializeView();
		super.onCreate(savedInstanceState, SpeakingPresenter.class, this);
	}

	private void initializeView() {
		mChatList = (ListView) findViewById(R.id.chatList);
		mSendButton = (ImageButton) findViewById(R.id.sendButton);
		mEditText = (EditText) findViewById(R.id.chatEdit);
		mSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				mEditText.setText("");
				mChatList.setSelection(mChatAdapter.getCount() - 1);
			}
		});

		mChatAdapter = new GenericAdapter<ItemChat>(getApplicationContext(),
				new ChatViewGencerator());

		mChangeListButton = (ImageButton) findViewById(R.id.changeButton);
		mChangeListButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeState();
				if (state == VIEW_ALL_STATE)
					mChangeListButton
							.setBackgroundResource(R.drawable.btn_viewanswers);
				else {
					mChangeListButton
							.setBackgroundResource(R.drawable.btn_viewall);
				}
			}
		});

		mChatList.setAdapter(mChatAdapter);

	}

	protected void changeState() {
		if (state == VIEW_ALL_STATE)
			state = VIEW_ANSWER_STATE;
		else
			state = VIEW_ALL_STATE;

	}

	public void onBackPressed(View v) {
		finish();
	}

	@Override
	public void addItemChat(ItemChat item) {
		mChatAdapter.add(item);

		mChatList.setSelection(mChatAdapter.getCount() - 1);
	}
	
	public void onAnswer(View v) {
		Log.i(TAG, "onAnswer");
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
		intent.putExtra("android.speech.extra.GET_AUDIO", true);
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

		startActivityForResult(intent, ACTION_SPEECH_TO_TEXT);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == ACTION_SPEECH_TO_TEXT) {
//			getOps().onAnswer(data, mIdRoom);
		}
	}


}