package com.fiepi.moebooru.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.fiepi.moebooru.R;

/**
 * Created by fiepi on 11/15/17.
 */

public class FixedImageView extends AppCompatImageView{

    private int mWidthWeight = 1;
    private int mHeightWeight = 1;

    public FixedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedImageView);
        mWidthWeight = a.getInteger(R.styleable.FixedImageView_widthWeight, 1);
        mHeightWeight = a.getInteger(R.styleable.FixedImageView_heightWeight, 1);
        a.recycle();
    }

    public void setWidthAndHeightWeight(int widthWeight, int heightWeight){
        mWidthWeight = widthWeight;
        mHeightWeight = heightWeight;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = this.getMeasuredWidth();
        int h = w * mHeightWeight / mWidthWeight;
        setMeasuredDimension(w + getPaddingLeft() + getPaddingRight(), h + getPaddingTop() + getPaddingBottom());
    }
}
