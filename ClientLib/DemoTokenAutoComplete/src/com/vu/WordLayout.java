package com.vu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WordLayout extends LinearLayout {

	public WordLayout(Context context) {
		super(context);
	}
	
	public WordLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        TextView v = (TextView)findViewById(R.id.word);
        
        if (selected) {
            v.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.close_x, 0);
        } else {
            v.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }
    }
	
}
