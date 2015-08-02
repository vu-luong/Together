package com.together.common;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.together.R;

public abstract class GenericNavigationDrawerActivity<Interface, OpsType extends ConfigurableOps<Interface>>
		extends GenericActivity<Interface, OpsType> implements DrawerCall {

	protected DrawerLayout mDrawerLayout;
	private ViewGroup mContainView;

	public void onCreate(Bundle savedInstanceState, Fragment fragment,
			Class<OpsType> opsType, Interface instance) {
		super.onCreate(savedInstanceState, opsType, instance);
		mContainView = (ViewGroup) findViewById(R.id.container);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int arg0) {
				
			}
			
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				
			}
			
			@Override
			public void onDrawerOpened(View arg0) {
				Log.i(TAG, "on Drawer open");
				enableDisableViewGroup(mContainView, false);
			}
			
			@Override
			public void onDrawerClosed(View arg0) {
				enableDisableViewGroup(mContainView, true);
			}
		});
		

		getFragmentManager().beginTransaction().add(R.id.left_drawer, fragment)
				.commit();
	}
	
	public void openDrawer() {
		mDrawerLayout.openDrawer(Gravity.START);
	}

	@Override
	public void closeDrawer() {
		mDrawerLayout.closeDrawers();
	}
	
	private void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
	    int childCount = viewGroup.getChildCount();
	    for (int i = 0; i < childCount; i++) {
	      View view = viewGroup.getChildAt(i);
	      view.setClickable(enabled);
	      if (view instanceof ViewGroup) {
	        enableDisableViewGroup((ViewGroup) view, enabled);
	      }
	    }
	  }

}
