package com.whaley.core.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.Target;
import com.orhanobut.logger.Logger;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.StrUtil;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangzhi on 16/8/3.
 */
public class GlideImageLoader {

    static boolean isLoadNetwork;

    public static LruCache<String, String> failedCache = new LruCache<>(15);

    public static ImageRequest.RequestManager with(Object tag) {
        ImageRequest.RequestManager requestManager = new ImageRequest.RequestManager();
        requestManager.tag(tag);
        return requestManager;
    }

    public static long lastTime;

    public static void LogTime(String tag) {
        long time = System.currentTimeMillis();
        Logger.v("ImageLoader", "loadImage time " + tag + (time - lastTime));
        lastTime = time;
    }

    public static void loadImage(final ImageRequest request) {

        try {
            final String url;
            if (!StrUtil.isEmpty(request.getUrl())) {
                String failedUrl = failedCache.get(request.getUrl());
                if (!StrUtil.isEmpty(failedUrl)) {
                    url = failedUrl;
                } else {
                    url = request.getUrl();
                }
            } else {
                url = null;
            }
            BitmapTypeRequest drawableTypeRequest = getRequest(request);
            if (drawableTypeRequest == null)
                return;
            if (request.isArgb8888())
                drawableTypeRequest.format(DecodeFormat.ALWAYS_ARGB_8888);
            final OnLoadFailCallback onLoadFailCallback = request.getOnLoadFailCallback();
            ImageView.ScaleType scaleType = request.getScaleType();
            drawableTypeRequest.placeholder(request.getPlaceholderResId());
            drawableTypeRequest.error(request.getErrorResId());
            if (scaleType != null) {
                if (scaleType == ImageView.ScaleType.CENTER_CROP) {
                    drawableTypeRequest.centerCrop();
                } else if (scaleType == ImageView.ScaleType.FIT_CENTER) {
                    drawableTypeRequest.fitCenter();
                }
            }
            drawableTypeRequest.skipMemoryCache(!request.isCacheMemory());
            DiskCacheStrategy diskCacheStrategy;
            switch (request.getDiskcacheStrategy()) {
                case ImageRequest.DISK_ALL:
                    diskCacheStrategy = DiskCacheStrategy.ALL;
                    break;
                case ImageRequest.DISK_NULL:
                    diskCacheStrategy = DiskCacheStrategy.NONE;
                    break;
                case ImageRequest.DISK_RESULT:
                    diskCacheStrategy = DiskCacheStrategy.RESULT;
                    break;
                case ImageRequest.DISK_SOURCE:
                    diskCacheStrategy = DiskCacheStrategy.SOURCE;
                    break;
                default:
                    diskCacheStrategy = DiskCacheStrategy.SOURCE;
                    break;
            }
            drawableTypeRequest.diskCacheStrategy(diskCacheStrategy);
            final Transformation transformation = request.getTransformation();

            if (transformation != null) {
                drawableTypeRequest.transform(new BitmapTransformation(AppContextProvider.getInstance().getContext()) {
                    @Override
                    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
                        return transformation.transform(toTransform, outWidth, outHeight);
                    }

                    @Override
                    public String getId() {
                        return transformation.getKey();
                    }
                });
            }

            final Animator animator = request.getAnimator();

            if (animator != null) {
                drawableTypeRequest.animate(new ViewPropertyAnimation.Animator() {
                    @Override
                    public void animate(View view) {
                        animator.animate(view);
                    }
                });
            } else {
                drawableTypeRequest.animate(new ViewPropertyAnimation.Animator() {
                    @Override
                    public void animate(View view) {
//                        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0.6f, 1f);
//                        anim.setDuration(300);
//                        anim.setInterpolator(new DecelerateInterpolator());
//                        anim.start();
                    }
                });
            }
            drawableTypeRequest.listener(new RequestListener<Object, Bitmap>() {

                @Override
                public boolean onException(Exception e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Logger.e(e, "onException url=" + url);
                    ImageLoaderCallback callback = request.getCallback();
                    if (callback != null) {
                        callback.onFailue(e);
                    } else if (request.getProgressBar() != null) {
                        request.getProgressBar().setVisibility(View.GONE);
                    }
                    try {
                        if (!TextUtils.isEmpty(url)) {
                            File file = new File(url);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                    } catch (Exception e1) {

                    }
                    return false;
                }


                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    if (resource.isRecycled()) {

                        return false;
                    }
                    ImageLoaderCallback callback = request.getCallback();
                    if (callback != null) {
                        callback.onSuccess(url, resource, null);
                    } else if (request.getProgressBar() != null) {
                        request.getProgressBar().setVisibility(View.GONE);
                    }
                    return false;
                }
            });

            Target target;

            if (request.isGetResouce()) {
                int width = Integer.MIN_VALUE;
                int height = Integer.MIN_VALUE;
                if (request.getWidth() > 0 && request.getHeight() > 0) {
                    width = request.getWidth();
                    height = request.getHeight();
                }
                ResourceGetterTarget resourceGetterTarget = new ResourceGetterTarget(request, width, height);
                resourceGetterTarget.setOnLoadFailCallback(onLoadFailCallback);
                target = drawableTypeRequest.into(resourceGetterTarget);
            } else {
                NormalViewTarget normalViewTarget = new NormalViewTarget(request);
                target = drawableTypeRequest.into(normalViewTarget);
            }
        } catch (Exception e) {
            Logger.e(e, "loadImage");
            if (request != null) {
                if (request.getCallback() != null) {
                    request.getCallback().onFailue(e);
                }
            }
        }
    }

    ;

    public static void clearView(View view) {
        Glide.clear(view);
    }


    public static void onHideChanged(Object tag, boolean isHide) {
//        EventController.postEvent(new HideChangedEvent(tag,isHide));


    }

    public static void pauseRequests(Object tag) {
        RequestManager requestManager = withTag(tag);
        if (requestManager != null)
            requestManager.pauseRequests();
    }

    public static void resumeRequests(Object tag) {
        RequestManager requestManager = withTag(tag);
        if (requestManager != null)
            requestManager.resumeRequests();
    }


    public static void cancelRequests(Object tag) {
        RequestManager requestManager = withTag(tag);
        if (requestManager != null)
            requestManager.onDestroy();
    }

    public static boolean isPause(Object tag) {
        RequestManager requestManager = withTag(tag);
        if (requestManager != null)
            return requestManager.isPaused();
        return false;
    }

    public static void onLowMemory() {
        Glide.with(AppContextProvider.getInstance().getContext()).onLowMemory();
    }

    public static void onTrimMemory(int level) {
        Glide.with(AppContextProvider.getInstance().getContext()).onTrimMemory(level);
    }


    ;


    private static BitmapTypeRequest getRequest(ImageRequest request) {
        Object tag = request.getTag();
        RequestManager requestManager = withTag(tag);
        if (requestManager == null)
            return null;
        String url = request.getUrl();
        int resId = request.getResId();

        DrawableTypeRequest drawableTypeRequest = null;
        if (url != null) {
            String failedUrl = failedCache.get(request.getUrl());
            if (!StrUtil.isEmpty(failedUrl)) {
                url = failedUrl;
            }
            Map<String, HashMap<String, String>> cookie = request.getCookie();
            if (cookie != null) {
                URL _url;
                try {
                    _url = new URL(url);
                } catch (Exception e) {
                    return requestManager.load(url).asBitmap();
                }
                String host = _url.getHost();
                Map<String, String> cookieMaps = cookie.get(host);
                if (cookieMaps != null && (url.startsWith("http") || url.startsWith("www"))) {
                    LazyHeaders.Builder builder = new LazyHeaders.Builder();
                    StringBuilder sb = new StringBuilder();

                    for (String key : cookieMaps.keySet()) {
                        sb.append(key);
                        sb.append("=");
                        sb.append(cookieMaps.get(key));
                        sb.append("; ");
                    }
                    builder.addHeader("Cookie", sb.toString());
                    builder.addHeader("Accept-Encoding", "gzip");
                    GlideUrl glideUrl = new GlideUrl(url, builder.build());
                    drawableTypeRequest = requestManager.load(glideUrl);

                } else
                    drawableTypeRequest = requestManager.load(url);
            } else
                drawableTypeRequest = requestManager.load(url);
        } else if (resId > 0) {
            drawableTypeRequest = requestManager.load(resId);
        } else {
            throw new NullPointerException("Url or ResId can not be empty,You need to call load() method!");
        }
        return drawableTypeRequest.asBitmap();
//        return drawableTypeRequest;
    }


    private static RequestManager withTag(Object tag) {
        RequestManager requestManager = null;

        if (tag == null) {
            throw new NullPointerException("Tag can not be empty,You need to call tag() method!");
        }
        if (tag instanceof Fragment) {
            Fragment fragment = (Fragment) tag;
            if (fragment.getActivity() != null)
                requestManager = Glide.with(fragment);
        } else if (tag instanceof android.app.Fragment) {
            android.app.Fragment fragment = (android.app.Fragment) tag;
            if (fragment.getActivity() != null)
                requestManager = Glide.with(fragment);
        } else if (tag instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) tag;
            requestManager = Glide.with(fragmentActivity);
        } else if (tag instanceof Activity) {
            Activity activity = (Activity) tag;
            requestManager = Glide.with(activity);
        } else if (tag instanceof Context) {
            Context context = (Context) tag;
            requestManager = Glide.with(context);
        } else {
            throw new IllegalArgumentException("ImageRequest Tag is error Instance,tag only can Activity,FragmentActivity,Fragment and Context!");
        }
        return requestManager;
    }
}
