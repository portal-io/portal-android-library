package com.whaley.core.share.model;

import android.app.Activity;
import android.graphics.Bitmap;

import com.whaley.core.share.SharePanelCallback;
import com.whaley.core.share.ShareListener;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: qxw
 * Date: 2016/11/21
 */

public class ShareParam implements Serializable {
    static AtomicInteger atomicInteger = new AtomicInteger();
    final String from;
    final int shareType;
    final String shareName;
    final String shareCode;
    final String title;
    final String url;
    final ImageModel imageModel;
    final boolean isVideo;
    final int callbackId;
    final boolean isDifferentParams;
    String des;
    ShareListener listener;
    Map<Integer, ShareParam> shareParams;
    boolean isHorizontal;
    boolean isFullscreen;
    private Activity context;
    private int type;
    final SharePanelCallback customizePanelCallback;
    public static SharePanelCallback defaultPanelCallback;

    public ShareParam(Activity context, String from, int shareType, String shareName, String shareCode, int type, String title, String des, String url, ImageModel imageModel, boolean isVideo,
                      boolean isHorizontal, boolean isFullscreen, Map<Integer, ShareParam> shareParams,
                      ShareListener listener, SharePanelCallback customizePanelCallback) {
        this.context = context;
        this.from = from;
        this.shareType = shareType;
        this.shareName = shareName;
        this.shareCode = shareCode;
        this.type = type;
        this.title = title;
        this.des = des;
        this.url = url;
        this.imageModel = imageModel;
        this.listener = listener;
        this.isVideo = isVideo;
        this.isHorizontal = isHorizontal;
        this.isFullscreen = isFullscreen;
        this.shareParams = shareParams;
        this.isDifferentParams = shareParams == null ? false : true;
        this.callbackId = atomicInteger.getAndIncrement();
        this.customizePanelCallback = customizePanelCallback;
    }

    public static ShareParam.Builder createBuilder() {
        return new ShareParam.Builder();
    }


    public SharePanelCallback getCustomizePanelCallback() {
        return customizePanelCallback;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public String getFrom() {
        return from;
    }

    public int getShareType() {
        return shareType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url;
    }


    public ImageModel getImageModel() {
        return imageModel;
    }

    public ShareListener getListener() {
        return listener;
    }

    public void setListener(ShareListener listener) {
        this.listener = listener;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public int getCallbackId() {
        return callbackId;
    }

    public Map<Integer, ShareParam> getShareParams() {
        return shareParams;
    }

    public void setShareParams(Map<Integer, ShareParam> shareParams) {
        this.shareParams = shareParams;
    }

    public boolean isDifferentParams() {
        return isDifferentParams;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public String getShareName() {
        return shareName;
    }

    public String getShareCode() {
        return shareCode;
    }


    public static class Builder {
        private Activity context;
        private String from;
        private int shareType;
        private String shareName;
        private String shareCode;
        private int type;
        private String title;
        private String des;
        private String url;
        private ImageModel imageModel;
        private boolean isHorizontal;
        private boolean isFullscreen;

        private boolean isVideo = false;
        private ShareListener listener;

        private Map<Integer, ShareParam> shareParams;

        private SharePanelCallback customizePanelCallback;

        public ShareParam.Builder setFullscreen(boolean isFullscreen) {
            this.isFullscreen = isFullscreen;
            return this;
        }

        public ShareParam.Builder setHorizontal(boolean isHorizontal) {
            this.isHorizontal = isHorizontal;
            return this;
        }

        public ShareParam.Builder setFrom(String from) {
            this.from = from;
            return this;
        }

        public ShareParam.Builder setContext(Activity context) {
            this.context = context;
            return this;
        }

        public ShareParam.Builder setShareListener(ShareListener listener) {
            this.listener = listener;
            return this;
        }

        public ShareParam.Builder setVideo(boolean isVideo) {
            this.isVideo = isVideo;
            return this;
        }

        public ShareParam.Builder setShareType(int shareType) {
            this.shareType = shareType;
            return this;
        }

        public ShareParam.Builder setShareName(String shareName) {
            this.shareName = shareName;
            return this;
        }

        public ShareParam.Builder setShareCode(String shareCode) {
            this.shareCode = shareCode;
            return this;
        }

        public ShareParam.Builder setType(int type) {
            this.type = type;
            return this;
        }

        public ShareParam.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ShareParam.Builder setDes(String des) {
            this.des = des;
            return this;
        }

        public ShareParam.Builder setUrl(String url) {
            this.url = url;
            return this;
        }


        public ShareParam.Builder setImgModelBitmap(String imgUrl) {
            checkImageModel();
            imageModel.setImgUrl(imgUrl);
            return this;
        }

        public ShareParam.Builder setImgModelBitmap(int resId) {
            checkImageModel();
            imageModel.setResId(resId);
            return this;
        }

        public ShareParam.Builder setImgModelBitmap(Bitmap bitmap) {
            checkImageModel();
            imageModel.setBitmap(bitmap);
            return this;
        }

        private ShareParam.Builder checkImageModel() {
            if (imageModel == null) {
                imageModel = new ImageModel();
            }
            return this;
        }

        public ShareParam.Builder setShareParams(Map<Integer, ShareParam> shareParams) {
            this.shareParams = shareParams;
            return this;
        }

        public ShareParam.Builder setShareParams(SharePanelCallback customizePanelCallback) {
            this.customizePanelCallback = customizePanelCallback;
            return this;
        }


        public ShareParam build() {
            return new ShareParam(context, from, shareType, shareName, shareCode, type, title, des, url, imageModel, isVideo, isHorizontal, isFullscreen, shareParams, listener, customizePanelCallback);
        }
    }

}
