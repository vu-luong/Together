package com.together.model.cache;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;

public class AvatarCache {
	
	private Map<String, Bitmap> mData = new HashMap<String, Bitmap>();
	private Context mContext;
	
	public AvatarCache(Context context) {
		mContext = context;
	}
	
	public Bitmap getBitmap(String id) {
		return mData.get(id);
	}
	
	
}
