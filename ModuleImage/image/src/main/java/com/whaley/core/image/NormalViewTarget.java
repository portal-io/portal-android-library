package com.whaley.core.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.orhanobut.logger.Logger;
import com.whaley.core.appcontext.AppContextProvider;

/**
 * Created by yangzhi on 17/1/21.
 */

public class NormalViewTarget extends BitmapImageViewTarget {

    final ImageRequest request;

    public NormalViewTarget(ImageRequest imageRequest) {
        super(imageRequest.getImageView());
        this.request = imageRequest;
    }


    @Override
    public void getSize(SizeReadyCallback cb) {
        if (request.getWidth() > 0 && request.getHeight() > 0) {
            cb.onSizeReady(request.getWidth(), request.getHeight());
        } else {
            super.getSize(cb);
        }
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        if (!resource.isRecycled()) {
            super.onResourceReady(resource, glideAnimation);
        }
    }


    @Override
    public void setDrawable(Drawable drawable) {
        super.setDrawable(drawable);

    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
        if (request.isInitBefore()) {
            super.onLoadCleared(placeholder);
        }
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
        if (request.isInitBefore()) {
            super.onLoadStarted(placeholder);

        }
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {

        try {
            String[] str = request.getUrl().split("/zoom/");
            if (str.length > 1) {
                GlideImageLoader.failedCache.put(request.getUrl(), str[0]);
                ImageLoader.loadImage(new ImageRequest(
                                str[0],
                                request.getResId(),
                                request.getHeight(),
                                request.getWidth(),
                                request.getScaleType(),
                                request.getImageView(),
                                request.getProgressBar(),
                                request.getTag(),
                                request.getPlaceholderResId(),
                                request.getErrorResId(),
                                request.getCallback(),
                                request.getTransformation(),
                                request.getAnimator(),
                                request.isInitBefore(),
                                request.getDiskcacheStrategy(),
                                request.isCacheMemory(),
                                request.isGetResouce(),
                                request.getOnResourceLoadCallback(),
                                request.getOnLoadFailCallback(),
                                request.isCircle(),
                                request.getAngle(),
                                request.isMaxSize(),
                                request.isArgb8888(),
                                request.getCookie()
                        )
                );

            } else {
                super.onLoadFailed(e, errorDrawable);
            }
        } catch (Exception ex) {
            super.onLoadFailed(e, errorDrawable);
        }
        Logger.e(e, "onLoadFailed url=" + request.getUrl());
    }

    @Override
    protected void setResource(Bitmap resource) {
        if (resource.isRecycled()) {
            onLoadFailed(new Exception("Bitmap is recycled"), AppContextProvider.getInstance().getContext().getResources().getDrawable(request.getErrorResId()));
            return;
        }
        if (request.isCircle()) {
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(AppContextProvider.getInstance().getContext().getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            view.setImageDrawable(circularBitmapDrawable);
        } else if (request.getAngle() > 0) {
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(AppContextProvider.getInstance().getContext().getResources(), resource);
            circularBitmapDrawable.setCornerRadius(request.getAngle());
            circularBitmapDrawable.setAntiAlias(true);
            view.setImageDrawable(circularBitmapDrawable);
        } else {
            super.setResource(resource);
        }

    }
}
