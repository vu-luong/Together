package com.together.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.together.R;
import com.together.common.LifecycleLoggingActivity;
import com.together.common.Utils;
import com.together.view.fragment.BrowseFragment;
import com.together.view.fragment.CompleteFragment;
import com.together.view.fragment.ExecuteFragment;
import com.together.view.fragment.NavigationFragment;

public class MainActivity extends LifecycleLoggingActivity implements
		ExecuteFragment.CallBack, CompleteFragment.CallBack,
		View.OnTouchListener{


	public static Intent makeIntent(Context context) {
		return new Intent(context, MainActivity.class);
	}
	
	private DrawerLayout mDrawerLayout;
	private float dX, dY;
	private GestureDetector gestureDetector;

	/**
	 * TAG to find Fragments
	 */
	private static final String BROWSER_FRAGMENT_TAG = "Dsad";
	private static final String EXECUTE_FRAGMENT_TAG = "asdds2";
	private static final String COMPLETE_FRAGMENT_TAG = "4334";

	private ImageButton mMissionButton;
	private ImageButton mBrowseButton;
	private ImageButton mCreateButton;

	/**
	 * Fragments that switching back and forth of this activity
	 */
	private Fragment mBrowseFragment;
	private Fragment mCompleteFragment;
	private Fragment mExecuteFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		gestureDetector = new GestureDetector(this, new SingleTapConfirm());

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		getFragmentManager().beginTransaction()
				.add(R.id.left_drawer, new NavigationFragment()).commit();

		// Initialize view
		initializeView();

		// Set up inner fragment reference
		setupFragmentReference(savedInstanceState);

	}

	private void setupFragmentReference(Bundle savedInsanceState) {
		if (savedInsanceState == null) {

			Log.i(TAG, "savedInsanceState null");
			mBrowseFragment = new BrowseFragment();
			mCompleteFragment = new CompleteFragment();
			mExecuteFragment = new ExecuteFragment();

			getFragmentManager()
					.beginTransaction()
					.add(R.id.fragment_container, mExecuteFragment,
							EXECUTE_FRAGMENT_TAG).commit();
		} else {

			Log.i(TAG, "savedInsanceState not null");
			mBrowseFragment = getFragmentManager().findFragmentByTag(
					BROWSER_FRAGMENT_TAG);
			mCompleteFragment = getFragmentManager().findFragmentByTag(
					COMPLETE_FRAGMENT_TAG);
			mExecuteFragment = getFragmentManager().findFragmentByTag(
					EXECUTE_FRAGMENT_TAG);

			if (mBrowseFragment == null)
				mBrowseFragment = new BrowseFragment();
			if (mCompleteFragment == null)
				mCompleteFragment = new CompleteFragment();

		}
	}

	private void initializeView() {
		mCreateButton = (ImageButton) findViewById(R.id.createButton);
		mCreateButton.setOnTouchListener(this);
		

		mMissionButton = (ImageButton) findViewById(R.id.my_mission);
		mMissionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchToExecuteFragment();

				mMissionButton.setBackgroundResource(R.drawable.tab1);
				mBrowseButton.setBackgroundResource(R.drawable.tab2);

			}
		});
		mBrowseButton = (ImageButton) findViewById(R.id.browse_mission);
		mBrowseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switchToBrowseFragment();

				mMissionButton.setBackgroundResource(R.drawable.tab2);
				mBrowseButton.setBackgroundResource(R.drawable.tab1);
			}
		});
	}

	public void switchToBrowseFragment() {
		Utils.switchToFragment(this, mBrowseFragment, R.id.fragment_container,
				BROWSER_FRAGMENT_TAG, mExecuteFragment, mCompleteFragment);
	}

	@Override
	public void switchToExecuteFragment() {
		Utils.switchToFragment(this, mExecuteFragment, R.id.fragment_container,
				EXECUTE_FRAGMENT_TAG, mBrowseFragment, mCompleteFragment);

	}

	@Override
	public void switchToCompleteFragment() {
		Utils.switchToFragment(this, mCompleteFragment,
				R.id.fragment_container, COMPLETE_FRAGMENT_TAG,
				mBrowseFragment, mExecuteFragment);
	}

	public void openNavigation(View v) {
		mDrawerLayout.openDrawer(Gravity.START);
		Log.i(TAG, "open navigation");
	}

	public void closeNavigation(View v) {
		mDrawerLayout.closeDrawers();
		Log.i(TAG, "close navigation");
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			Intent intent = NewMissionActivity
					.makeIntent(getApplicationContext());
			startActivity(intent);
			
            return true;
        } else 
	    switch (event.getActionMasked()) {

	        case MotionEvent.ACTION_DOWN:

	            dX = view.getX() - event.getRawX();
	            dY = view.getY() - event.getRawY();
	            break;

	        case MotionEvent.ACTION_MOVE:

	            view.animate()
	                    .x(event.getRawX() + dX)
	                    .y(event.getRawY() + dY)
	                    .setDuration(0)
	                    .start();
	            break;
	        default:
	            return false;
	    }
	    return true;
	}
	
	private class SingleTapConfirm extends SimpleOnGestureListener {
         @Override
        public boolean onSingleTapUp(MotionEvent e) {
        	return true;
        }
        
    }
	
}
