package com.together.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.together.R;
import com.together.common.GenericFragment;
import com.together.pojo.ItemCompleted;
import com.together.presenter.CompletedPresenter;
import com.together.view.ui.CompletedAdapter;

public class CompleteFragment extends
		GenericFragment<CompletedPresenter.View, CompletedPresenter> implements
		CompletedPresenter.View {
	
	private CallBack mCallBack;
	private TextView mCompleteButton;
	private TextView mExecuteButton;
	private ImageView mUnderComplete;
	private ImageView mUnderExecute;
	
	private CompletedAdapter mCompletedAdapter;
	private ListView mCompletedList;
	
	public interface CallBack {
		void switchToExecuteFragment();
	}

	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallBack = (CallBack) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_completed, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState, CompletedPresenter.class,
				this);
		
		mCompleteButton = (TextView) view.findViewById(R.id.button_completed);
		mUnderComplete = (ImageView)view.findViewById(R.id.underLineCompleted);
		mUnderExecute = (ImageView) view.findViewById(R.id.underLineExcuting);
		mCompletedList = (ListView) view.findViewById(R.id.completedList);
		
		mCompletedAdapter = new CompletedAdapter(getApplicationContext());
		mCompletedList.setAdapter(mCompletedAdapter);
		
		mCompletedAdapter.add(new ItemCompleted());
		mCompletedAdapter.add(new ItemCompleted());
		mCompletedAdapter.add(new ItemCompleted());
		mCompletedAdapter.add(new ItemCompleted());
		mCompletedAdapter.add(new ItemCompleted());
		
		
		mExecuteButton = (TextView) view.findViewById(R.id.button_executing);
		mExecuteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallBack.switchToExecuteFragment();
				
			}
		});
	}
	
	
	

}
