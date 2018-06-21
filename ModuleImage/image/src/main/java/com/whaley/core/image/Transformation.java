package com.whaley.core.image;

import android.graphics.Bitmap;

/**
 * Created by yangzhi on 16/8/3.
 */
public interface Transformation {

    public Bitmap transform(Bitmap resource, int outHeigt, int outWidth);

    public String getKey();
}
