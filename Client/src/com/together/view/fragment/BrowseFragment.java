package com.together.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.together.R;
import com.together.common.GenericFragment;
import com.together.pojo.ItemBrowse;
import com.together.presenter.BrowsePresenter;
import com.together.view.ui.BrowseViewGenerator;
import com.together.view.ui.GenericAdapter;

public class BrowseFragment extends
		GenericFragment<BrowsePresenter.View, BrowsePresenter> implements
		BrowsePresenter.View {
	
	
	private GenericAdapter<ItemBrowse> mBrowseAdapter;
	private ListView mBrowseList;
	
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
		
		mBrowseList = (ListView) view.findViewById(R.id.browseList);
		mBrowseAdapter = new GenericAdapter<ItemBrowse>(getApplicationContext(), 
				new BrowseViewGenerator());
		
		mBrowseList.setAdapter(mBrowseAdapter);
		
		mBrowseAdapter.add(new ItemBrowse());
		mBrowseAdapter.add(new ItemBrowse());
		mBrowseAdapter.add(new ItemBrowse());
		mBrowseAdapter.add(new ItemBrowse());
		mBrowseAdapter.add(new ItemBrowse());
		mBrowseAdapter.add(new ItemBrowse());
	}

}
