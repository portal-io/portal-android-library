package com.whaley.core.widget.banner;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.whaley.core.widget.R;
import com.whaley.core.widget.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;

/**
 * Banner 控件
 * 支持 loop循环
 * <p>
 * Created by yangzhi on 2017/9/9.
 */

public class WhaleyBanner extends FrameLayout {

    final int defualtIndicatorGravity = Gravity.BOTTOM;

    BannerAdapter adapter;

    ViewGroup layoutIndicators;

    ViewPager layoutPager;

    ArrayList<ImageView> indicatorViews = new ArrayList<>();

    int normalIndicatorResourceId;

    int selectedIndicatorResourceId;

    IndicatorPageChangeListener indicatorPageChangeListener;

    BannerAdapter.BannerListener bannerListener;

    int indicatorGravity;

    boolean autoChange;

    long autoChangeDuration = 2000;

    boolean isAutoChangePaused;

    Disposable autoChangeDisposable;

    boolean isAttached;

    boolean isUserStoped;

    boolean isIndicatorEnable;

    public WhaleyBanner(Context context) {
        super(context);
        init();
    }

    public WhaleyBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).cloneInContext(getContext()).inflate(getLayoutId(), this, true);
        setViewGroup(getViewGroup());
        setViewPager(getLayoutPager());
        layoutPager.addOnPageChangeListener(onPageChangeListener);
    }

    public ViewGroup getViewGroup() {
        return (ViewGroup) findViewById(R.id.layout_indicators);
    }

    public ViewPager getViewPager() {
       return layoutPager;
    }
    public ViewPager getLayoutPager(){
        return (ViewPager) findViewById(R.id.layout_pager);
    }

    private void setViewGroup(ViewGroup viewGroup) {
        layoutIndicators = viewGroup;
    }

    private void setViewPager(ViewPager pager) {
        layoutPager = pager;
    }

    public int getLayoutId() {
        return R.layout.layout_banner;
    }

    public WhaleyBanner setAdapter(BannerAdapter bannerAdapter) {
        this.adapter = bannerAdapter;
        this.adapter.setBannerListener(bannerListener);
        return this;
    }

    public WhaleyBanner setBannerListener(BannerAdapter.BannerListener bannerListener) {
        this.bannerListener = bannerListener;
        if (this.adapter != null) {
            this.adapter.setBannerListener(bannerListener);
        }
        return this;
    }


    public WhaleyBanner setIndicatorEnable(boolean isIndicatorEnable) {
        this.isIndicatorEnable = isIndicatorEnable;
        return this;
    }

    public WhaleyBanner setIndicatorGravity(int indicatorGravity) {
        this.indicatorGravity = indicatorGravity;
        FrameLayout.LayoutParams layoutParams = (LayoutParams) layoutIndicators.getLayoutParams();
        layoutParams.gravity = indicatorGravity;
        layoutIndicators.requestLayout();
        return this;
    }

    public WhaleyBanner setNormalIndicator(@DrawableRes int normalIndicatorResourceId) {
        this.normalIndicatorResourceId = normalIndicatorResourceId;
        return this;
    }

    public WhaleyBanner setSelectedIndicator(@DrawableRes int selectedIndicatorResourceId) {
        this.selectedIndicatorResourceId = selectedIndicatorResourceId;
        return this;
    }

    public void setData(List datas) {
        layoutPager.setAdapter(adapter);
        this.adapter.setData(datas);
        updateIndicators(datas);
        setCurrentItem(0, false);
        checkAutoStart();
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = adapter.getInnerPosition(item);
        layoutPager.setCurrentItem(realItem, smoothScroll);
    }


    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    public int getCurrentItem() {
        return adapter != null ? adapter.getRealPosition(layoutPager.getCurrentItem()) : 0;
    }


    public void updateIndicators(List datas) {
        layoutIndicators.removeAllViews();
        indicatorViews.clear();
        layoutPager.removeOnPageChangeListener(indicatorPageChangeListener);
        if (!isIndicatorEnable || datas == null || datas.size() <= 1)
            return;
        for (Object o : datas) {
            // 翻页指示的点
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(5, 0, 5, 0);
            if (indicatorViews.isEmpty())
                pointView.setImageResource(selectedIndicatorResourceId);
            else
                pointView.setImageResource(normalIndicatorResourceId);
            indicatorViews.add(pointView);
            layoutIndicators.addView(pointView);
        }

        indicatorPageChangeListener = new IndicatorPageChangeListener(adapter, indicatorViews,
                normalIndicatorResourceId, selectedIndicatorResourceId);
        layoutPager.addOnPageChangeListener(indicatorPageChangeListener);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAutoChangePaused(true);
                disposeAutoChange();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setAutoChangePaused(false);
                checkAutoStart();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public WhaleyBanner setAutoChange(boolean autoChange) {
        this.autoChange = autoChange;
        return this;
    }

    public WhaleyBanner setAutoChangeDuration(long autoChangeDuration) {
        return this;
    }


    public void startAutoChange() {
        isUserStoped = false;
        checkAutoStart();
    }

    private void startInterval() {
        if (autoChangeDisposable != null && !autoChangeDisposable.isDisposed()) {
            return;
        }
        autoChangeDisposable = Observable.interval(autoChangeDuration, TimeUnit.MILLISECONDS)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(@NonNull Long aLong) throws Exception {
                        return !isAutoChangePaused;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        setCurrentItem(getCurrentItem() + 1, true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }

    public void setAutoChangePaused(boolean autoChangePaused) {
        isAutoChangePaused = autoChangePaused;
    }

    public void stopAutoChange() {
        isUserStoped = true;
        disposeAutoChange();
    }

    private void disposeAutoChange() {
        if (autoChangeDisposable != null) {
            autoChangeDisposable.dispose();
            autoChangeDisposable = null;
        }
    }


    private void checkAutoStart() {
        if (!autoChange) {
            return;
        }
        if (adapter == null || adapter.getRealCount() <= 1) {
            return;
        }
        if (isUserStoped)
            return;
        if (isAttached && getVisibility() == VISIBLE) {
            startInterval();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            checkAutoStart();
            return;
        }
        disposeAutoChange();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
        checkAutoStart();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached = false;
        disposeAutoChange();
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {
            int realPosition = adapter.getRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int realPosition = position;
            if (adapter != null) {
                realPosition = adapter.getRealPosition(position);
                if (positionOffset == 0
                        && mPreviousOffset == 0
                        && (position == 0 || position == adapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            mPreviousOffset = positionOffset;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (adapter != null && adapter.getRealCount() > 1) {
                int position = layoutPager.getCurrentItem();
                int realPosition = adapter.getRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE
                        && (position == 0 || position == adapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }
        }
    };

}
