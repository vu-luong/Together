package com.vu;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

public class MainActivity extends Activity implements
		TokenCompleteTextView.TokenListener {

	private CompletionView mCompletionView;
	ArrayAdapter<String> adapter;
	String[] words;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		words = new String[0];
		
		adapter = new FilteredArrayAdapter<String>(this,
				R.layout.word_layout, words) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {

					LayoutInflater l = (LayoutInflater) getContext()
							.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
					convertView = l.inflate(R.layout.word_layout, parent,
							false);
				}

				String p = getItem(position);
				((TextView) convertView.findViewById(R.id.word)).setText(p);

				return convertView;
			}

			@Override
			protected boolean keepObject(String word, String mask) {
				mask = mask.toLowerCase();
				return word.toLowerCase().startsWith(mask)
						|| word.toLowerCase().startsWith(mask);
			}
		};
		
		
		
		mCompletionView = (CompletionView) findViewById(R.id.searchView);
		mCompletionView.setAdapter(adapter);
		
		mCompletionView.setTokenListener(this);
		mCompletionView
				.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
		
	}


	@Override
	public void onTokenAdded(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTokenRemoved(Object arg0) {
		// TODO Auto-generated method stub
		
	}
}
