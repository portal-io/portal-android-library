package com.whaley.core.widget.imageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by yangzhi on 2017/7/19.
 */

public class WhaleyImageView extends ImageView{

    public WhaleyImageView(Context context) {
        super(context);
    }

    public WhaleyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WhaleyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        }catch (Exception e){

        }
    }
}
