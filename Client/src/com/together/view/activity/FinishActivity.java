package com.together.view.activity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import com.together.R;
import com.together.common.GenericActivity;
import com.together.pojo.ItemFinish;
import com.together.presenter.FinishPresenter;
import com.together.view.ui.FinishViewGenerator;
import com.together.view.ui.GenericAdapter;

public class FinishActivity extends GenericActivity<
		FinishPresenter.View, FinishPresenter> 
		implements FinishPresenter.View {

	private ListView mRankList;
	private GenericAdapter<ItemFinish> mAdapter;
	private GridView mWordGrid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_finishmission);
		
		// Initialize view
		initializeView();
		
		super.onCreate(savedInstanceState, FinishPresenter.class, this);
	}

	private void initializeView() {
		mRankList = (ListView) findViewById(R.id.rankList);
		mAdapter = new GenericAdapter<ItemFinish>(
				getActivityContext(), new FinishViewGenerator());
		
		mRankList.setAdapter(mAdapter);
		
		mWordGrid = (GridView) findViewById(R.id.wordGrid);
		
		mAdapter.add(new ItemFinish());
		mAdapter.add(new ItemFinish());
		mAdapter.add(new ItemFinish());
		mAdapter.add(new ItemFinish());
		
	}
	
}
