package com.together.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.together.R;
import com.together.common.LifecycleLoggingActivity;
import com.together.common.Utils;
import com.together.view.fragment.NewGrammarFragment;
import com.together.view.fragment.NewPronounceFragment;
import com.together.view.fragment.NewVocabularyFragment;

public class NewMissionActivity extends LifecycleLoggingActivity {
	
	
	public static Intent makeIntent(Context context) {
		return new Intent(context, NewMissionActivity.class);
	}
	
	private static final String PRONOUNCE_FRAGMENT_TAG = "pronounce";
	private static final String VOCABULARY_FRAGMENT_TAG = "vocabulary";
	private static final String GRAMMAR_FRAGMENT_TAG = "grammar";


	private RelativeLayout mPronounceButton;
	private RelativeLayout mVocabularyButton;
	private RelativeLayout mGrammarButton;
	
	/**
	 * 
	 */
	
	private Fragment mPronounceFragment;
	private Fragment mVocabularyFragment;
	private Fragment mGrammarFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newmission);
		
		// Initialize view
		initializeView();
		
		// Set up inner fragment reference
		setupFragmentReference(savedInstanceState);
		
		
	}
	
	private void setupFragmentReference(Bundle savedInsanceState) {
		if (savedInsanceState == null) {

			mPronounceFragment = new NewPronounceFragment();
			mVocabularyFragment = new NewVocabularyFragment();
			mGrammarFragment = new NewGrammarFragment();

			getFragmentManager()
					.beginTransaction()
					.add(R.id.fragment_container_newmission, mPronounceFragment,
							PRONOUNCE_FRAGMENT_TAG).commit();
		} else {
//			mBrowseFragment = getFragmentManager().findFragmentByTag(
//					BROWSER_FRAGMENT_TAG);
//			mCompleteFragment = getFragmentManager().findFragmentByTag(
//					COMPLETE_FRAGMENT_TAG);
//			mExecuteFragment = getFragmentManager().findFragmentByTag(
//					EXECUTE_FRAGMENT_TAG);
//
//			if (mBrowseFragment == null)
//				mBrowseFragment = new HomeBrowseFragment();
//			if (mCompleteFragment == null)
//				mCompleteFragment = new HomeCompleteFragment();

		}
	}

	private void initializeView() {
		mPronounceButton = (RelativeLayout) findViewById(R.id.pronounceButton);
		mVocabularyButton = (RelativeLayout) findViewById(R.id.vocabularyButton);
		mGrammarButton = (RelativeLayout) findViewById(R.id.grammarButton);
		
		
		//TODO --
		mPronounceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchToPronounceFragment();
				mPronounceButton.setBackgroundResource(R.drawable.tab1);
				mVocabularyButton.setBackgroundResource(R.drawable.tab2);
				mGrammarButton.setBackgroundResource(R.drawable.tab2);
			}
		});
		
		
		mVocabularyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchToVocabularyFragment();
				
				mPronounceButton.setBackgroundResource(R.drawable.tab2);
				mVocabularyButton.setBackgroundResource(R.drawable.tab1);
				mGrammarButton.setBackgroundResource(R.drawable.tab2);
			}
		});
		
		
		mGrammarButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchToGrammarFragment();
				
				mPronounceButton.setBackgroundResource(R.drawable.tab2);
				mVocabularyButton.setBackgroundResource(R.drawable.tab2);
				mGrammarButton.setBackgroundResource(R.drawable.tab1);
			}
		});
		
		
	}
	
	public void onBackPressed(View v){
		finish();
	}
	
	public void switchToPronounceFragment() {
		Utils.switchToFragment(this, mPronounceFragment, 
				R.id.fragment_container_newmission, PRONOUNCE_FRAGMENT_TAG, 
				mVocabularyFragment, mGrammarFragment);
	}
	
	public void switchToVocabularyFragment(){
		Utils.switchToFragment(this, mVocabularyFragment, 
				R.id.fragment_container_newmission, VOCABULARY_FRAGMENT_TAG, 
				mPronounceFragment, mGrammarFragment);
	}
	
	public void switchToGrammarFragment(){
		Utils.switchToFragment(this, mGrammarFragment, 
				R.id.fragment_container_newmission, GRAMMAR_FRAGMENT_TAG, 
				mVocabularyFragment, mPronounceFragment);
	}
	
	
}
