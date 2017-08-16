package com.arifian.bakingapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.arifian.bakingapp.R;

/**
 * Created by faqih on 27/05/17.
 */

public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(getMeasuredWidth() > getMeasuredHeight())
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        else
            setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
    }
}
