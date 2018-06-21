package com.whaley.core.widget.imageview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.whaley.core.debug.logger.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by YangZhi on 2017/8/3 14:53.
 */

public class FrameAnimImageView extends WhaleyImageView {

    private int[] imageRes;

    private long switchDuration = 40;

    private int currentImage;


    private boolean isPaused;

    private boolean isStarted;

    private Disposable disposable;

    private boolean isAttached;


    public FrameAnimImageView(Context context) {
        this(context, null);
    }

    public FrameAnimImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameAnimImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setImageRes(int[] imageRes) {
        stop();
        this.imageRes = imageRes;
    }

    public void setSwitchDuration(long duration) {
        this.switchDuration = duration;
        stop();
        checkShowChanged();
    }

    public void start() {
        if (imageRes == null || imageRes.length <= 0) {
            return;
        }
        if (disposable != null) {
            isPaused = false;
            return;
        }
        disposable = Observable.interval(switchDuration, TimeUnit.MILLISECONDS)
                .map(new Function<Long, BitmapContainer>() {
                    @Override
                    public BitmapContainer apply(@NonNull Long aLong) throws Exception {
                        BitmapContainer container = new BitmapContainer();
                        if (isPaused())
                            return container;
                        if (getWidth() == 0 || getHeight() == 0)
                            return container;
                        if (imageRes != null && currentImage < imageRes.length) {
                            int resId = imageRes[currentImage];
                            container.resId = resId;
                            if (currentImage == imageRes.length - 1) {
                                currentImage = 0;
                                return container;
                            }
                            currentImage++;
                        }
                        return container;
                    }
                })
                .doOnNext(new Consumer<BitmapContainer>() {
                    @Override
                    public void accept(@NonNull BitmapContainer bitmapContainer) throws Exception {
                        if (bitmapContainer.isNoneRes())
                            return;
                        Bitmap bitmap = Glide.with(getContext())
                                .load(bitmapContainer.resId)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(getWidth(), getHeight())
                                .get();
                        bitmapContainer.bitmap = bitmap;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BitmapContainer>() {
                    @Override
                    public void onNext(@NonNull BitmapContainer bitmapContainer) {
                        if (!bitmapContainer.isNoneBitmap()) {
                            setImageBitmap(bitmapContainer.bitmap);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(e, "FrameAnimImageView error");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        isStarted = true;
    }

    public void pause() {
        this.isPaused = true;
    }

    public void stop() {
        isStarted = false;
        if (disposable == null)
            return;
        disposable.dispose();
        disposable = null;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public int getCurrentImage() {
        return currentImage;
    }


    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        checkShowChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached = false;
        checkShowChanged();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
        checkShowChanged();
    }

    private void checkShowChanged(){
        if(isShowed()){
            start();
        }else {
            stop();
        }
    }

    private boolean isShowed() {
        return getVisibility() == VISIBLE && isAttached;
    }

    private class BitmapContainer {
        Bitmap bitmap;
        int resId = -1;

        boolean isNoneRes() {
            return this.resId == -1;
        }

        boolean isNoneBitmap() {
            return bitmap == null || bitmap.isRecycled();
        }
    }

}
