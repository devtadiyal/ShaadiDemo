package com.example.medcords.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Low sensitivity SwipeRefreshLayout
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    private float mInitialDownY;
    private int mTouchSlop = 40;

    public MySwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float yDiff = ev.getY() - mInitialDownY;
                if (yDiff < mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
           * @return returns sensitivity value
     */
    public int getTouchSlop() {
        return mTouchSlop;
    }

    /**
           * Set pull-down sensitivity
     *
           * @param mTouchSlop dip value
     */
    public void setTouchSlop(int mTouchSlop) {
        this.mTouchSlop = mTouchSlop;
    }

}

