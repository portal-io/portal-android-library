package com.whaley.core.share.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Author: qxw
 * Date: 2016/11/21
 */

public class ImageModel implements Serializable {
    private Bitmap bitmap;
    private String imgUrl;
    private int resId = -1;

    public boolean isBitmapData() {
        return bitmap != null;
    }

    public boolean isImgUrlData() {
        return imgUrl != null;
    }

    public boolean isResData() {
        return resId > -1;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        imgUrl = null;
        resId = -1;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        bitmap = null;
        resId = -1;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
        bitmap = null;
        imgUrl = null;
    }
}
