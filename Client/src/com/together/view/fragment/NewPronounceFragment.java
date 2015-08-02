package com.together.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.together.R;
import com.together.common.GenericFragment;
import com.together.presenter.NewPronouncePresenter;
import com.together.view.ui.CompletionView;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

public class NewPronounceFragment extends
		GenericFragment<NewPronouncePresenter.View, NewPronouncePresenter>
		implements NewPronouncePresenter.View,
		TokenCompleteTextView.TokenListener {

	private CompletionView mCompletionView;
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<CharSequence> mSpinnerAdapter;
	private Spinner mSpinner;
	private String[] words;
	private List<String> savedWords = new ArrayList<String>();;

	private ImageButton mCreateTopicButton;
	private ImageButton mBackButton;
	private TextView mTextView;
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_newmission_pronounce,
				container, false);
	}

	private void initializeView() {
		mCreateTopicButton = (ImageButton) getActivity().findViewById(
				R.id.createTopicButton);
		mBackButton = (ImageButton) getActivity().findViewById(R.id.backButton);
		mTextView = (TextView) getActivity().findViewById(R.id.memberText);
		// Typeface type = Typeface
		// .createFromAsset(
		// getActivity().getAssets(),"fonts/OpenSans-Light.ttf");
		// mTextView.setTypeface(type);

		mCreateTopicButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mCreateTopicButton
							.setBackgroundResource(R.drawable.btn_create_topic);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					mCreateTopicButton
							.setBackgroundResource(R.drawable.btn_create_topic_hou);
					createTopic(v);
					return true;
				}
				return false;

			}
		});

		mSpinner = (Spinner) getActivity().findViewById(R.id.planets_spinner);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		mSpinnerAdapter = ArrayAdapter.createFromResource(
				getApplicationContext(), R.array.number_array,
				R.drawable.spinner_item);
		// Specify the layout to use when the list of choices appears
		// mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mSpinner.setAdapter(mSpinnerAdapter);

		words = new String[0];

		adapter = new FilteredArrayAdapter<String>(getApplicationContext(),
				R.layout.word_layout, words) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {

					LayoutInflater l = (LayoutInflater) getApplicationContext()
							.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
					convertView = l
							.inflate(R.layout.word_layout, parent, false);
				}

				String s = getItem(position);
				((TextView) convertView.findViewById(R.id.word)).setText(s);

				return convertView;
			}

			@Override
			protected boolean keepObject(String word, String mask) {
				mask = mask.toLowerCase();
				return word.toLowerCase().startsWith(mask)
						|| word.toLowerCase().startsWith(mask);
			}
		};

		mCompletionView = (CompletionView) getActivity().findViewById(
				R.id.wordView);
		mCompletionView.setAdapter(adapter);

		mCompletionView.setTokenListener(this);
		mCompletionView
				.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);

	}

	@Override
	public void addWord(String word) {
		mCompletionView.addObject(word);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState,
				NewPronouncePresenter.class, this);

		initializeView();

	}

	public void createTopic(View v) {
		int i =  Integer.valueOf(mSpinner.getSelectedItem().toString());
		
		getOps().createNewMission(savedWords, i);
		
	}

	private void updateTokenConfirmation() {
		StringBuilder sb = new StringBuilder("Current tokens:\n");
		for (Object token : mCompletionView.getObjects()) {
			sb.append(token.toString());
			sb.append("\n");
		}
	}

	@Override
	public void onTokenAdded(Object token) {
		updateTokenConfirmation();
		savedWords.add(token.toString());
	}

	@Override
	public void onTokenRemoved(Object token) {
		for (String s : savedWords) {
			if (s.equals(token.toString())) {
				savedWords.remove(s);
				break;
			}
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getOps().unRegister();
		super.onDestroy();
	}
	
	
	@Override
	public void completedNewRoom() {
		getActivity().finish();
	}

}
