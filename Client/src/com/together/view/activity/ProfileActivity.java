package com.together.view.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.Profile;
import com.together.R;
import com.together.common.GenericActivity;
import com.together.presenter.ProfilePresenter;
import com.together.view.fragment.NavigationFragment;
import com.together.view.ui.CircularImageView;
import com.tokenautocomplete.FilteredArrayAdapter;

public class ProfileActivity extends
		GenericActivity<ProfilePresenter.View, ProfilePresenter> implements
		ProfilePresenter.View {

	private String avaUrl = "";
	private CircularImageView avaView;
	private Spinner mSpinner;
	private ArrayAdapter<CharSequence> mSpinnerAdapter;

	private DrawerLayout mDrawerLayout;

	public static Intent makeIntent(Context context, String avaUrl) {
		return new Intent(context, ProfileActivity.class).putExtra("avaUrl",
				avaUrl);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_profile);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		getFragmentManager().beginTransaction()
				.add(R.id.left_drawer, new NavigationFragment()).commit();

		// Initialize view
		initializeView();

		super.onCreate(savedInstanceState, ProfilePresenter.class, this);
	}

	private void initializeView() {
		avaUrl = getIntent().getStringExtra("avaUrl");
		avaView = (CircularImageView) findViewById(R.id.avatarView);
		new GetAva().execute(avaUrl);

		mSpinner = (Spinner) findViewById(R.id.levelSpinner);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		mSpinnerAdapter = ArrayAdapter.createFromResource(
				getApplicationContext(), R.array.EnglishLevel,
				R.drawable.spinner_item);
		// Specify the layout to use when the list of choices appears
		// mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mSpinner.setAdapter(mSpinnerAdapter);

	}

	public class GetAva extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			return getBitmapFromURL(params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			avaView.setImageBitmap(result);
		}
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			// Log exception
			return null;
		}
	}

	public void closeNavigation(View v) {
		mDrawerLayout.closeDrawers();
		Log.i(TAG, "close navigation");
	}
	
	public void toMyMission(View v) {
		closeNavigation(v);
		
		Intent intent = MainActivity.makeIntent(getApplicationContext());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	public void toMyProfile(View v) {
		closeNavigation(v);
		
	}

}
