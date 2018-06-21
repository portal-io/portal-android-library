package com.whaley.core.widget.refresh;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * 使用方法: 再ListView外层加次自定义Layout
 * 注意: 1. 只有下拉刷新的情况  不需要setAdapter(ListView listView,ListAdapter adapter)方法
 * 2. 只有上拉加载  不需要嵌套此Layout  直接使用ListViewLoadMoreHelper
 * <p/>
 * <p/>
 * Created by yangzhi on 16/5/19.
 */
public class RefreshLayout extends SwipeRefreshLayout implements ILoadMoreView,IRefreshView{
    private static final long DEFAULT_MIN_DURATION=1000;

    private LoadMoreView loadMoreView;

    private long startRefreshTime;

    private long minDuration=DEFAULT_MIN_DURATION;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable stopRefreshRunable=new Runnable() {
        @Override
        public void run() {
            setRefreshing(false);
        }
    };

    public RefreshLayout(Context context) {
        super(context);
        initViews();
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
    }

    public LoadMoreView setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter, boolean isBottomBar) {
        loadMoreView = new RecyclerViewLoadMoreView(recyclerView, adapter, isBottomBar);
        return loadMoreView;
    }

    public void setListener(final com.whaley.core.widget.refresh.OnRefreshListener onRefreshListener) {
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                startRefreshTime=System.currentTimeMillis();
                if (onRefreshListener != null)
                    onRefreshListener.onRefresh();
            }
        });
    }

    @Override
    public void startRefresh() {
        setRefreshing(true);
    }

    @Override
    public void stopRefresh(boolean isSucess){
        long duration=System.currentTimeMillis()-startRefreshTime;
        if(duration<minDuration){
            long diff= minDuration-duration;
            handler.postDelayed(stopRefreshRunable,diff);
        }else
            stopRefreshRunable.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(stopRefreshRunable);
    }

    @Override
    public void setHasMore(boolean isHasMore, boolean isRefresh) {
        if (getLoadMoreView() != null)
            getLoadMoreView().setHasMore(isHasMore, isRefresh);
    }

    @Override
    public void stopLoadMore(boolean isSuccess) {
        if (getLoadMoreView() != null)
            getLoadMoreView().stopLoadMore(isSuccess);
    }

    @Override
    public void startLoadMore() {
        if (getLoadMoreView() != null)
            getLoadMoreView().startLoadMore();
    }

    public void setMinDuration(long minDuration) {
        this.minDuration = minDuration;
    }

    public LoadMoreView getLoadMoreView() {
        return loadMoreView;
    }
}
