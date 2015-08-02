package com.together.common;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public abstract class LifecycleLoggingFragment extends Fragment {
	
	protected final String TAG = getClass().getSimpleName();
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		Log.d(TAG, "onAttach(): Attached to Activity");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
            // The fragment is being re-created. Use the
            // savedInstanceState bundle for initializations either
            // during onCreate or onRestoreInstanceState().
            Log.d(TAG, "onCreate(): activity re-created");

        } else {
            // Fragment is being created anew. No prior saved
            // instance state information available in Bundle object.
            Log.d(TAG, "onCreate(): activity created anew");
        }

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated(): Activity created");
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.d(TAG, "onViewCreated()");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop()");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		Log.d(TAG, "onDetach()");
	}
	
}
