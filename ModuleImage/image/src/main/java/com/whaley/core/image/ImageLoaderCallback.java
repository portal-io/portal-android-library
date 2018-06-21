package com.whaley.core.image;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by yangzhi on 16/8/3.
 */
public interface ImageLoaderCallback {
    public void onSuccess(String url, Bitmap bitmap, File file);

    public void onFailue(Throwable error);

    public void onProgressChanged(double current, double total);
}
