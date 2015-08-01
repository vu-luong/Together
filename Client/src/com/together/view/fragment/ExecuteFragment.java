package com.together.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.together.R;
import com.together.common.GenericFragment;
import com.together.pojo.Mission;
import com.together.presenter.ExecutePresenter;
import com.together.view.activity.SpeakingActivity;
import com.together.view.ui.GenericAdapter;
import com.together.view.ui.MissionViewGenerator;

public class ExecuteFragment extends
		GenericFragment<ExecutePresenter.View, ExecutePresenter> implements
		ExecutePresenter.View, OnItemClickListener {
	
	private TextView mCompleteButton;
	private TextView mExecuteButton;
	private ImageView mUnderComplete;
	private ImageView mUnderExecute;
	
	private GenericAdapter<Mission> mExecutingAdapter;
	private ListView mExecutingList;
	
	private CallBack mCallBack;
	
	public interface CallBack {
		void switchToCompleteFragment();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallBack = (CallBack) activity;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_execute, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mCompleteButton = (TextView) view.findViewById(R.id.button_completed);
		mUnderComplete = (ImageView)view.findViewById(R.id.underLineCompleted);
		mUnderExecute = (ImageView) view.findViewById(R.id.underLineExcuting);
		mExecutingList = (ListView) view.findViewById(R.id.executingList);
		
		mExecutingList.setOnItemClickListener(this);
		
		mExecutingAdapter = new GenericAdapter<Mission>(getApplicationContext(), 
				new MissionViewGenerator());
		mExecutingList.setAdapter(mExecutingAdapter);
		
		mCompleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCallBack.switchToCompleteFragment();
			}
		});
		
		mExecuteButton = (TextView) view.findViewById(R.id.button_executing);
		
		super.onViewCreated(view, savedInstanceState, 
				ExecutePresenter.class, this);
		
		
		// Get my mission
		getOps().getMyMission();
	}
	
	
	@Override
	public void addItemMission(Mission itemRoom) {
		mExecutingAdapter.add(itemRoom);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Intent intent = SpeakingActivity.makeIntent(getApplicationContext(), 
				mExecutingAdapter.getItem(position).getId());
		startActivity(intent);
		
	}
	
	
	
		

}
