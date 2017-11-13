package com.fiepi.moebooru.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.fiepi.moebooru.R;

/**
 * 宽度随屏幕变化，高度和宽度保持比例一致的图片控件
 * <p>
 * 宽度：高度 = widthWeight：heightWeight
 * <p>
 * 作者：余天然 on 2017/6/20 上午11:36
 */
public class AutoHeightImageView extends AppCompatImageView {
    private int widthWeight = 1;
    private int heightWeight = 1;

    public AutoHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoHeightImageView);
        widthWeight = a.getInteger(R.styleable.AutoHeightImageView_widthWeight, 1);
        heightWeight = a.getInteger(R.styleable.AutoHeightImageView_heightWeight, 1);
        a.recycle();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = this.getMeasuredWidth();
        int h = w * heightWeight / widthWeight;
        setMeasuredDimension(w + getPaddingLeft() + getPaddingRight(), h + getPaddingTop() + getPaddingBottom());
    }
}