package com.whaley.core.widget.pullable;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.whaley.core.widget.R;


/**
 * Created by yangzhi on 17/1/11.
 */

public class PullableLayout extends RelativeLayout {

    private static final boolean DEBUG = false;

    private static final String TAG = "Pullable";

    private static final int INVALID_POINTER = -1;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    private static final float DRAG_RATE = 0.7f;

    private float rate = 1f;

    private PullBehavior behavior;

    private PullRecord record = new PullRecord();

    private View header;

    private View target;


    private OnChildScrollUpCallback mChildScrollUpCallback;


    private int mActivePointerId = INVALID_POINTER;

    private int mTouchSlop;


    private VelocityTracker mVelocityTracker;

    private int mMaxVelocity;

    private int mMinVelocity = -1;

    private ValueAnimator valueAnimator;

    private ValueAnimator overpullResetAnimator;


    public PullableLayout(Context context) {
        this(context, null);
    }

    public PullableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PullableLayout);
        int scrollViewId = typedArray.getResourceId(R.styleable.PullableLayout_scrollViewId, -1);
        int headerViewId = typedArray.getResourceId(R.styleable.PullableLayout_headerViewId, -1);
        if (scrollViewId != -1) {
            target = findViewById(headerViewId);
        }
        if (headerViewId != -1) {
            header = findViewById(headerViewId);
        }
        typedArray.recycle();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
//        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity()*2;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (target == null) {
            int childCount = getChildCount();
            if (childCount == 1) {
                target = getChildAt(0);
            } else if (childCount == 2) {
                header = getChildAt(0);
                target = getChildAt(1);
            }
        }
        if (target != null)
            target.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void setTarget(View target) {
        this.target = target;
    }

    public void setBehavior(PullBehavior behavior) {
        this.behavior = behavior;
        if (header == null)
            header = behavior.getHeaderView();
    }

    public View getHeader() {
        return header;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        target.measure(MeasureSpec.makeMeasureSpec(
//                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
//                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
//                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        record.setHeaderHeight(header.getMeasuredHeight());
        mMinVelocity = getMeasuredHeight() / 4;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int pointerIndex = -1;
        boolean ret = false;
        final int action = MotionEventCompat.getActionMasked(ev);
        if (isOnReset() && action == MotionEvent.ACTION_DOWN) {
            stopReset();
        }

//        if (
//                !isEnabled()
//                || isOnReset()
////                || canScrollUp()
//                ) {
//            Log.d(TAG,"superDispatch begin return");
//            super.dispatchTouchEvent(ev);
//            return true;
//        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (DEBUG)
                    Log.d(TAG, "ACTION_DOWN");
                stopFiling();
                mActivePointerId = ev.getPointerId(0);
                record.setBeingDragged(false);
                record.setBeingHorizontalDragged(false);
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    if (DEBUG)
                        Log.d(TAG, "ACTION_DOWN pointerIndex<0");
                    return true;
                }
                record.setInitDownY(ev.getY(pointerIndex));
                record.setLastY(ev.getY(pointerIndex));
                record.setLastX(ev.getX());
                record.setInitDownX(ev.getX());
//                record.setChildDown(false);

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (DEBUG)
                    Log.d(TAG, "ACTION_POINTER_DOWN");
                if (record.isBeingDragged()) {
                    ret = true;
                }
                pointerIndex = MotionEventCompat.getActionIndex(ev);
                if (pointerIndex < 0) {
                    return true;
                }
                mActivePointerId = ev.getPointerId(pointerIndex);
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    if (DEBUG)
                        Log.d(TAG, "ACTION_DOWN pointerIndex<0");
                    return true;
                }
                record.setInitDragY(ev.getY(pointerIndex));
                record.setLastY(ev.getY(pointerIndex));

                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (DEBUG)
                    Log.d(TAG, "ACTION_POINTER_UP");
                if (record.isBeingDragged()) {
                    ret = true;
                }
                onSecondaryPointerUp(ev);
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    if (DEBUG)
                        Log.d(TAG, "ACTION_DOWN pointerIndex<0");
                    return true;
                }
                record.setInitDragY(ev.getY(pointerIndex));
                record.setLastY(ev.getY(pointerIndex));
                break;
            case MotionEvent.ACTION_MOVE: {
                if (mActivePointerId == INVALID_POINTER) {
                    return true;
                }
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return true;
                }
                if (mVelocityTracker == null)
                    mVelocityTracker = VelocityTracker.obtain();
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(500, mMaxVelocity);

                final float y = ev.getY(pointerIndex);
                final float x = ev.getX();
                float offsetY = y - record.getLastY();
                float offsetX = x - record.getLastY();
                startHorizontalDragging(x, y);
                ret = true;
                if (record.isBeingHorizontalDragged() && !record.isBeingDragged()) {
                    ret = false;
                } else {
                    startDragging(y);
                    if (DEBUG)
                        Log.d(TAG, "ACTION_MOVE record.isBeingDragged()=" + record.isBeingDragged());
                    if (record.isBeingDragged()) {
                        ret = true;
                        float scrollY = y - record.getInitDragY();

                        if (DEBUG)
                            Log.d(TAG, "scrollY=" + scrollY + ",initDragY=" + record.getInitDragY() + ",y=" + y + ",isHeadShowFull()" + isHeadShowFull() + ",isHeadOnShow()" + isHeadOnShow() + ",offsetY=" + offsetY + ",currentOverScrollHeight=" + getOverScrollTop());

                        if ((isHeadShowFull() && offsetY > 0) || record.getOverscrollTop() > 0) {
                            caculateRate(offsetY >= 0);
                            float overscrollTop = getOverScrollTop() + offsetY * rate;
                            overscrollTop = Math.max(0, overscrollTop);
                            callbackOverPull(overscrollTop);
                            record.setOnOverPull(true);
                        } else {
                            if (DEBUG)
                                Log.d(TAG, "offsetY=" + offsetY + ",canScrollUp=" + canScrollUp());
                            if (offsetY > 0 && !canScrollUp()) {
                                record.setOnOverPull(false);
                                int transHeight = (int) Math.min(0, getCurrentTransHeight() + offsetY);
                                transHeader(transHeight);
                            } else {
                                record.setOnOverPull(false);
                                if (isHeadOnShow()) {
                                    int transHeight = (int) Math.max(-getMaxTransHead(), getCurrentTransHeight() + offsetY);
                                    transHeader(transHeight);
                                } else {
                                    if (DEBUG)
                                        Log.d(TAG, "superDispatch isChildDown=" + record.isChildDown());
                                    ret = false;
                                    //record.setBeingDragged(false);
                                }
                            }
                        }

                    } else if (Math.abs(y - record.getInitDownY()) > mTouchSlop) {
                        if (DEBUG)
                            Log.d(TAG, "superDispatch isDrag=false");
                        ret = false;
                    }
                }
                record.setLastY(y);
                record.setLastX(x);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (DEBUG)
                    Log.d(TAG, "ACTION_UP");
                if (getOverScrollTop() > 0) {
                    startReset();
                    ret = true;
                } else if (record.isBeingDragged()) {
                    final float velocityY = mVelocityTracker == null ? 0 : mVelocityTracker.getYVelocity();
                    if (!isHeadShowFull() && isHeadOnShow() && Math.abs(velocityY) > mMinVelocity) {
//                        int maxHeight=behavior.maxHeadPullHeight()>0?-behavior.maxHeadPullHeight():-record.getHeaderHeight();
                        int finalMargin = velocityY > 0 ? 0 : -getMaxTransHead();
                        if (isShouldAutoOffset())
                            startFling(finalMargin);
                        else
                            callbackFling();
                    }
                    ret = true;
                }
                record.setBeingDragged(false);
                if (null != mVelocityTracker) {
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                mActivePointerId = INVALID_POINTER;

            }

        }
        if (!ret)
            super.dispatchTouchEvent(ev);
        return true;
    }


    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }


    private void transHeader(int transHeight) {
        if (DEBUG)
            Log.d(TAG, "transHeader transHeight=" + transHeight);
        if (isShouldAutoOffset()) {
            LayoutParams layoutParams = (LayoutParams) header.getLayoutParams();
            layoutParams.topMargin = transHeight;
            header.requestLayout();
        }
        callbackPullUp(transHeight);
    }

    private int getCurrentTransHeight() {
        LayoutParams layoutParams = (LayoutParams) header.getLayoutParams();
        return layoutParams.topMargin;
    }

    private void stopFiling() {
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            if (valueAnimator.isStarted())
                valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    private void startFling(int finalMargin) {
        stopFiling();
        valueAnimator = ValueAnimator.ofInt(getCurrentTransHeight(), finalMargin);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int transHeight = (int) animation.getAnimatedValue();
                transHeader(transHeight);
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }


    private boolean isShouldAutoOffset() {
        return behavior.isShouldAutoOffsetHeader();
    }


    private int getOverScrollTop() {
        return record.getOverscrollTop();
    }

    private void stopReset() {
        if (overpullResetAnimator != null) {
            overpullResetAnimator.removeAllUpdateListeners();
            if (overpullResetAnimator.isStarted())
                overpullResetAnimator.cancel();
        }
    }

    private void startReset() {
        stopReset();
        overpullResetAnimator = ValueAnimator.ofInt(record.getOverscrollTop(), 0);
        overpullResetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int size = (int) animation.getAnimatedValue();
//                int diff=size-record.getOverscrollTop();
                callbackOverPull(size);
            }
        });
        overpullResetAnimator.setDuration(300);
        overpullResetAnimator.start();
    }

    private boolean isOnReset() {
        return overpullResetAnimator != null && overpullResetAnimator.isRunning();
    }


    private int getMaxTransHead() {
        int maxHeadPull = behavior.maxHeadPullHeight();
        return maxHeadPull <= 0 ? record.getHeaderHeight() : maxHeadPull;
    }

    private boolean isHeadOnShow() {
        LayoutParams layoutParams = (LayoutParams) header.getLayoutParams();
        int maxHeadPull = behavior.maxHeadPullHeight();
        return layoutParams.topMargin > -getMaxTransHead();
    }

    private boolean isHeadShowFull() {
        LayoutParams layoutParams = (LayoutParams) header.getLayoutParams();
        return layoutParams.topMargin >= 0;
    }

    private void startDragging(float y) {
        final float yDiff = y - record.getInitDownY();
        if (DEBUG)
            Log.d(TAG, "startDragging yDiff=" + yDiff + ",isHeadOnShow=" + isHeadOnShow() + ",canScrollUp()" + canScrollUp());
        if (Math.abs(yDiff) > mTouchSlop && (isHeadOnShow() || (!canScrollUp() && yDiff > 0)) && !record.isBeingDragged()) {
            record.setInitDragY(record.getInitDownY() + (yDiff > 0 ? mTouchSlop : -mTouchSlop));
            record.setBeingDragged(true);
        }
    }

    private void startHorizontalDragging(float x, float y) {
        final float yDiff = y - record.getInitDownY();
        final float xDiff = x - record.getInitDownX();
        if (DEBUG)
            Log.d(TAG, "startHorizontalDragging yDiff=" + yDiff + ",xDiff=" + xDiff + ",mTouchSlop" + mTouchSlop);
        if (Math.abs(xDiff) > mTouchSlop && Math.abs(xDiff) > Math.abs(yDiff)) {
            record.setBeingHorizontalDragged(true);
        }
    }


    private void callbackPullUp(int offsetY) {
        behavior.onPull(offsetY);
    }


    private void caculateRate(boolean isScrollDown) {
        int radioHeight = 100;

        int radio = (int) (1f * record.getOverscrollTop() / radioHeight);
        if (DEBUG)
            Log.e(TAG, "radio=" + radio + ",isScrollDown=" + isScrollDown);
        rate = 1f;

        if (isScrollDown) {
            for (int i = 0; i < radio; i++) {
                if (isScrollDown) {
                    rate = Math.max(0f, rate * DRAG_RATE);
                }
            }
        }
    }

    private void callbackOverPull(float overscrollTop) {
        behavior.onOverPullDown((int) overscrollTop);
        record.setOverscrollTop((int) overscrollTop);

        record.getOverscrollTop();


    }


    private void callbackFling() {
        behavior.startFling();
    }


    protected boolean canScrollUp() {
        if (mChildScrollUpCallback != null) {
            return mChildScrollUpCallback.canChildScrollUp(this, target);
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (target instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) target;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(target, -1) || target.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(target, -1);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopFiling();
    }

    public void setChildScrollUpCallback(OnChildScrollUpCallback mChildScrollUpCallback) {
        this.mChildScrollUpCallback = mChildScrollUpCallback;
    }

    public interface OnChildScrollUpCallback {
        boolean canChildScrollUp(PullableLayout parent, @Nullable View child);
    }
}
