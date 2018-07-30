package com.alice.core.views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by yuzhijun on 2016/7/13.
 */
public class MyScrollerView extends ScrollView {

    private OnScrollListener onScrollListener;

    public MyScrollerView(Context context) {
        this(context, null);
    }

    public MyScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }
    }

    /**
     *
     * @author xiaanming
     */
    public interface OnScrollListener {
        /**
         *
         * @param scrollY 、
         */
        public void onScroll(int scrollY);
    }
}
