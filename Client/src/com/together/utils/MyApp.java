package com.together.utils;

import android.app.Application;
import android.util.Log;

public class MyApp extends Application {

	@Override
	public void onCreate() {
		Log.i("TAG", "den day roi");
		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF",
				"fonts/light.ttf"); // font from assets:
												// "assets/fonts/Roboto-Regular.ttf
	}
}