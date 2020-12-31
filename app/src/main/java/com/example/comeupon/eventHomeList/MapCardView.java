package com.example.comeupon.eventHomeList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.cardview.widget.CardView;

public class MapCardView extends CardView {
    public MapCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}