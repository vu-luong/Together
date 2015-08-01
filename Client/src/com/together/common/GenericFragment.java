package com.together.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class GenericFragment<Interface, OpsType extends ConfigurableOps<Interface>>
		extends LifecycleLoggingFragment {

	private OpsType mOpsInstance;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set retain instance
		setRetainInstance(true);
	}

	public void onViewCreated(View view, Bundle savedInstanceState,
			Class<OpsType> opsType, Interface instance) {
		// Call super
		super.onViewCreated(view, savedInstanceState);

		// If this method returns true it's the first time the
		// Activity has been created.
		if (savedInstanceState == null) {
			Log.d(TAG, "First time in");

			// Initialize the GenericActivity fields.
			initialize(opsType, instance);
		} else {

			// This check shouldn't be necessary under normal
			// circumstances, but it's better to lose state than to
			// crash!
			if (mOpsInstance == null)
				// Initialize the GenericActivity fields.
				initialize(opsType, instance);
			else
				// Inform it that the runtime configuration change has
				// completed.
				mOpsInstance.onConfiguration(instance, false);
		}

	}

	/**
	 * Initialize the GenericActivity fields.
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws
	 */
	private void initialize(Class<OpsType> opsType, Interface instance) {
		// Create the OpsType object.
		try {
			mOpsInstance = opsType.newInstance();
		} catch (java.lang.InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// Put the OpsInstance into the RetainedFragmentManager under
		// the simple name.
		// Perform the first initialization.
		mOpsInstance.onConfiguration(instance, true);
	}

	/**
	 * Return the initialized OpsType instance for use by the application.
	 */
	public OpsType getOps() {
		return mOpsInstance;
	}

	/**
	 * Return the Activity context.
	 */
	public Context getActivityContext() {
		return getActivity();
	}

	/**
	 * Return the Application context.
	 */
	public Context getApplicationContext() {
		return getActivity().getApplicationContext();
	}

	
}
