package com.together.view.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.together.R;
import com.together.common.GenericFragment;
import com.together.pojo.Mission;
import com.together.presenter.BrowsePresenter;
import com.together.view.activity.SpeakingActivity;
import com.together.view.ui.BrowseViewGenerator;
import com.together.view.ui.GenericAdapter;

public class BrowseFragment extends
		GenericFragment<BrowsePresenter.View, BrowsePresenter> implements
		BrowsePresenter.View, BrowseViewGenerator.Callback {
	
	private GenericAdapter<Mission> mBrowseAdapter;
	private ListView mBrowseList;
	private ImageButton mSearchButton;
	private TextView mTittle;
	private EditText mSearchEdit;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_browse, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState, BrowsePresenter.class,
				this); 
		
		
		//initialize view
		mSearchButton = (ImageButton) view.findViewById(R.id.searchButton);
		mTittle = (TextView) view.findViewById(R.id.tittle);
		mSearchEdit = (EditText) view.findViewById(R.id.searchEdit);
		
		mSearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTittle.setVisibility(View.INVISIBLE);
				mSearchEdit.setVisibility(View.VISIBLE);
			}
		});
		
		
		//Init list
		mBrowseList = (ListView) view.findViewById(R.id.browseList);
		mBrowseAdapter = new GenericAdapter<Mission>(getApplicationContext(), 
				new BrowseViewGenerator(this));
		
		mBrowseList.setAdapter(mBrowseAdapter);
		
		//--
	}

	@Override
	public void changeItemMission(List<Mission> list) {
		mBrowseAdapter.setItemList(list);
		
	}

	
	@Override
	public void onDestroy() {
		
		getOps().unRegister();
		
		super.onDestroy();
	}

	public void getMissions() {
		if (getOps() != null) getOps().getMissions();
	}

	@Override
	public void joinRoom(Mission item) {
		Intent intent = SpeakingActivity.makeIntent(getApplicationContext(),
				item.getId());
		startActivity(intent);
		
	}
	
	
	
}
