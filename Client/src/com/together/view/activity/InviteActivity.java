package com.together.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.together.R;
import com.together.common.GenericActivity;
import com.together.pojo.ItemInvite;
import com.together.presenter.InvitePresenter;
import com.together.view.ui.GenericAdapter;
import com.together.view.ui.InviteViewGenerator;

public class InviteActivity extends GenericActivity<
			InvitePresenter.View, InvitePresenter> 
			implements InvitePresenter.View {
	
	private EditText mSearchEdit;
	private ListView mFriendList;
	private ImageButton mDoneButton;
	private GenericAdapter<ItemInvite> mAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_invite);
		
		// Initialize view
		initializeView();
		
		super.onCreate(savedInstanceState, InvitePresenter.class, this);
	}


	private void initializeView() {
			mSearchEdit = (EditText) findViewById(R.id.searchEdit);
			mFriendList = (ListView) findViewById(R.id.friendList);
			mDoneButton = (ImageButton) findViewById(R.id.doneButton);
			mAdapter = new GenericAdapter<ItemInvite>(getApplicationContext(), 
						new InviteViewGenerator());
			mFriendList.setAdapter(mAdapter);
			
			mAdapter.add(new ItemInvite());
			mAdapter.add(new ItemInvite());
			mAdapter.add(new ItemInvite());
			
			mDoneButton.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN ) {
		                   mDoneButton.setBackgroundResource(R.drawable.btn_done);
		                }
		                if (event.getAction() == MotionEvent.ACTION_UP ) {
		                	mDoneButton.setBackgroundResource(R.drawable.btn_done_hou);
		                	
		                    return true;
		                }
		                return false;
				}
			});
			
	}
	
	
}
