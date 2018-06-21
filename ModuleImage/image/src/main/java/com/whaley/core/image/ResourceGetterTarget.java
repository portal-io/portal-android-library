package com.whaley.core.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.orhanobut.logger.Logger;
import com.whaley.core.appcontext.AppContextProvider;

/**
 * Created by yangzhi on 17/1/21.
 */

public class ResourceGetterTarget extends SimpleTarget<Bitmap> {

    private ImageRequest request;

    private OnLoadFailCallback onLoadFailCallback;

    public ResourceGetterTarget(ImageRequest request,int witdh,int height){
        super(witdh,height);
        this.request=request;
    }


    public void setOnLoadFailCallback(OnLoadFailCallback onLoadFailCallback) {
        this.onLoadFailCallback = onLoadFailCallback;
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        if (onLoadFailCallback != null)
            onLoadFailCallback.onLoadFail(e);
        Logger.e(e, "loadImage");
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
        if (resource.isRecycled()) {
            onLoadFailed(new Exception("Bitmap is recycled"), AppContextProvider.getInstance().getContext().getResources().getDrawable(request.getErrorResId()));
            return;
        }
        if (request.getOnResourceLoadCallback() != null)
            request.getOnResourceLoadCallback().onResourceLoaded(resource);

    }

}

