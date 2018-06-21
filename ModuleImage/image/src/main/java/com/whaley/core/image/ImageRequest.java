package com.whaley.core.image;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.whaley.core.utils.DisplayUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangzhi on 16/8/3.
 */
public class ImageRequest {

    public static int DEFUALT_PLACEHOLDER_RESID;

    public static int DEFUALT_ERRPR_RESID;

    public static ImageSizeGetter DEFAULT_IMAGE_SIZE_GETTER;

    /**
     * 缓存所有版本的图像
     */
    public static final int DISK_ALL = 0;
    /**
     * 仅缓存结果图像
     */
    public static final int DISK_RESULT = 1;


    /**
     * 仅缓存原图
     */
    public static final int DISK_SOURCE = 2;


    /**
     * 什么都不缓存
     */
    public static final int DISK_NULL = 3;

    private final Map<String, HashMap<String, String>> cookie;

    private final String url;

    private final int resId;

    private final int height;

    private final int width;

    private final ImageView.ScaleType scaleType;

    private final ImageView imageView;

    private final View progressBar;

    private final Object tag;

    private final int placeholderResId;

    private final int errorResId;

    private final ImageLoaderCallback callback;

    private final Transformation transformation;

    private final Animator animator;

    private final boolean isInitBefore;

    /**
     * 本地缓存策略
     */
    private final int diskcacheStrategy;

    private final boolean isCacheMemory;

    private final boolean isGetResouce;

    private final OnResourceLoadCallback onResourceLoadCallback;

    private final OnLoadFailCallback onLoadFailCallback;

    private final boolean isCircle;

    private final float angle;

    private final boolean isMaxSize;

    private final boolean isArgb8888;


    public ImageRequest(String url, int resId, int height, int width, ImageView.ScaleType scaleType,
                        ImageView imageView, View progressBar, Object tag, int placeholderResId,
                        int errorResId, ImageLoaderCallback callback, Transformation transformation,
                        Animator animator, boolean isInitBefore, int diskcacheStrategy, boolean isCacheMemory,
                        boolean isGetResouce, OnResourceLoadCallback onResourceLoadCallback,
                        OnLoadFailCallback onLoadFailCallback, boolean isCircle, float angle, boolean isMaxSize, boolean isArgb8888,
                        Map<String, HashMap<String, String>> cookie) {
        this.cookie = cookie;
        this.url = url;
        this.resId = resId;

        if (width <= 0 && height <= 0 && imageView != null && imageView.getLayoutParams() != null && imageView.getLayoutParams().width > 0 && imageView.getLayoutParams().height > 0) {
            this.width = imageView.getLayoutParams().width;
            this.height = imageView.getLayoutParams().height;
        } else {
            this.width = width;
            this.height = height;
        }
        this.scaleType = scaleType;
        this.imageView = imageView;
        this.progressBar = progressBar;
        this.tag = tag;
        if (placeholderResId == -1) {
            this.placeholderResId = DEFUALT_PLACEHOLDER_RESID;
        } else {
            this.placeholderResId = placeholderResId;
        }

        if (errorResId == -1) {
            this.errorResId = DEFUALT_ERRPR_RESID;
        } else {
            this.errorResId = errorResId;
        }
        this.callback = callback;
        this.transformation = transformation;
        this.animator = animator;
        this.isInitBefore = isInitBefore;
        this.isCacheMemory = isCacheMemory;
        this.diskcacheStrategy = diskcacheStrategy;
        this.isGetResouce = isGetResouce;
        this.onResourceLoadCallback = onResourceLoadCallback;
        this.onLoadFailCallback = onLoadFailCallback;
        this.isCircle = isCircle;
        this.angle = angle;
        this.isMaxSize = isMaxSize;
        this.isArgb8888 = isArgb8888;
    }

    public Map<String, HashMap<String, String>> getCookie() {
        return cookie;
    }

    public int getDiskcacheStrategy() {
        return diskcacheStrategy;
    }

    public boolean isCacheMemory() {
        return isCacheMemory;
    }

    public boolean isInitBefore() {
        return isInitBefore;
    }

    public Animator getAnimator() {
        return animator;
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public ImageLoaderCallback getCallback() {
        return callback;
    }

    public String getUrl() {
        return url;
    }

    public int getResId() {
        return resId;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public View getProgressBar() {
        return progressBar;
    }

    public Object getTag() {
        return tag;
    }

    public int getPlaceholderResId() {
        return placeholderResId;
    }

    public int getErrorResId() {
        return errorResId;
    }

    public boolean isGetResouce() {
        return isGetResouce;
    }

    public boolean isCircle() {
        return isCircle;
    }


    public float getAngle() {
        return angle;
    }

    public boolean isMaxSize() {
        return isMaxSize;
    }

    public OnResourceLoadCallback getOnResourceLoadCallback() {
        return onResourceLoadCallback;
    }

    public OnLoadFailCallback getOnLoadFailCallback() {
        return onLoadFailCallback;
    }

    public boolean isArgb8888() {
        return isArgb8888;
    }

    public static class RequestManager {
        private Object tag;

        public RequestManager tag(Object tag) {
            this.tag = tag;
            return this;
        }


        public Builder load(Object object) {
            Builder builder = new Builder();
            if (object == null)
                object = "";
            builder.tag(tag);
            builder.load(object);
            return builder;
        }
    }


    public static class Builder {
        private String url;

        private int resId;

        private int height;

        private int width;

        private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

        private ImageView imageView;

        private View progressBar;

        private Object tag;

        private int placeholderResId = -1;

        private int errorResId = -1;

        private ImageLoaderCallback callback;

        private Transformation transformation;

        private Animator animator;

        private Map<String, HashMap<String, String>> cookie;

        private boolean isInitBefore = true;


        private int diskcacheStrategy = ImageRequest.DISK_ALL;

        private boolean isCacheMemory = true;

        private boolean isGetResouce;

        private OnResourceLoadCallback onResourceLoadCallback;

        private boolean isCircle;

        private float angle;

        private OnLoadFailCallback onLoadFailCallback;

        private boolean isMaxSize;

        private boolean isArgb8888;

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }


        public Builder argb8888() {
            this.isArgb8888 = true;
            return this;
        }

        public Builder maxSize(int maxSize) {
            String url = this.url;
            if (DEFAULT_IMAGE_SIZE_GETTER != null) {
                ImageSize imageSize = DEFAULT_IMAGE_SIZE_GETTER.getImageSize(url, maxSize);
                this.width = imageSize.getWidth();
                this.height = imageSize.getHeight();
                this.url = imageSize.getUrl();
                this.isMaxSize = imageSize.isMax();
            } else {
                this.width = maxSize;
                this.height = maxSize;
                this.isMaxSize = true;
            }
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            this.isMaxSize = false;
            return this;
        }


        public Builder small() {
            return maxSize((int) (1f * DisplayUtil.screenW / 4));
        }

        public Builder medium() {
            return maxSize((int) (1f * DisplayUtil.screenW / 2));
        }


        public Builder big() {
            return maxSize(DisplayUtil.screenW);
        }


        public Builder circle() {
            this.isCircle = true;
            return this;
        }

        public Builder corner(float angle) {
            this.angle = angle;
            return this;
        }


        public Builder load(Object object) {
            if (object instanceof String) {
                return load((String) object);
            } else if (object instanceof Integer) {
                return load((int) object);
            } else {
                throw new IllegalArgumentException("load(Object) object must be String or Integer");
            }

        }

        public Builder load(String url) {
            this.url = url;
            this.resId = -1;
            return this;
        }

        public Builder load(int resId) {
            this.resId = resId;
            this.url = null;
            return this;
        }

        public Builder cookie(Map<String, HashMap<String, String>> cookie) {
            this.cookie = cookie;
            return this;
        }

        public Builder placeholder(int placeholderResId) {
            this.placeholderResId = placeholderResId;
            return this;
        }

        public Builder error(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public Builder centerCrop() {
            this.scaleType = ImageView.ScaleType.CENTER_CROP;
            return this;
        }


        public Builder fitCenter() {
            this.scaleType = ImageView.ScaleType.FIT_CENTER;
            return this;
        }

        public Builder fitXY() {
            this.scaleType = ImageView.ScaleType.FIT_XY;
            return this;
        }

        public Builder setScaleType(ImageView.ScaleType scaleType) {
            this.scaleType = scaleType;
            return this;
        }


        public Builder transform(Transformation transformation) {
            this.transformation = transformation;
            return this;
        }


        public Builder animate(Animator animator) {
            this.animator = animator;
            return this;
        }

        public Builder notInitBefore() {
            this.isInitBefore = false;
            return this;
        }


        public Builder diskCacheStrategy(int diskcacheStrategy) {
            this.diskcacheStrategy = diskcacheStrategy;
            if (diskcacheStrategy < 0 || diskcacheStrategy > 3)
                this.diskcacheStrategy = 0;
            return this;
        }

        public Builder skipMemoryCache(boolean skip) {
            this.isCacheMemory = !skip;
            return this;
        }

        public Builder onLoadFail(OnLoadFailCallback onLoadFailCallback) {
            this.onLoadFailCallback = onLoadFailCallback;
            return this;
        }

        public void into(ImageView imageView, View progressBar, ImageLoaderCallback callback) {
            this.progressBar = progressBar;
            this.imageView = imageView;
            this.callback = callback;
            load();
        }


        public void into(ImageView imageView, View progressBar) {
            this.progressBar = progressBar;
            this.imageView = imageView;
            load();
        }


        public void into(ImageView imageView) {
            this.imageView = imageView;
            load();
        }

        public void get(OnResourceLoadCallback<Bitmap> callback) {
            this.isGetResouce = true;
            this.onResourceLoadCallback = callback;
            load();
        }


        private void load() {
            ImageLoader.loadImage(new ImageRequest(url, resId, height, width, scaleType,
                    imageView, progressBar, tag, placeholderResId, errorResId, callback,
                    transformation, animator, isInitBefore, diskcacheStrategy, isCacheMemory,
                    isGetResouce, onResourceLoadCallback, onLoadFailCallback, isCircle, angle,
                    isMaxSize, isArgb8888, cookie));
        }
    }


    public interface ImageSizeGetter {
        ImageSize getImageSize(String url, int maxSize);


    }
}
