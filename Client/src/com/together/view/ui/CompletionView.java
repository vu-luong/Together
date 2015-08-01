package com.together.view.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.together.R;
import com.tokenautocomplete.TokenCompleteTextView;

public class CompletionView extends TokenCompleteTextView<String> {

	public CompletionView(Context context) {
		super(context);
	}
	
	public CompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	@Override
	protected String defaultObject(String word) {
		return word;
	}

	@Override
	protected View getViewForObject(String word) {
		LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.word_token, (ViewGroup)CompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.word)).setText(word);

        return view;
	}
	

}
