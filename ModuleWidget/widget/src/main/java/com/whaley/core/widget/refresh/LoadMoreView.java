package com.whaley.core.widget.refresh;

/**
 * Created by yangzhi on 16/5/19.
 */
public abstract class LoadMoreView<T> implements ILoadMoreView {

    private boolean isHasMore = false;

    private final LoadingPullableAdapter loadingPullableAdapter;

    private boolean isLoading;

    private OnLoadMoreListener listener;

    public LoadMoreView(LoadingPullableAdapter loadingPullableAdapter){
        this.loadingPullableAdapter = loadingPullableAdapter;
    }

    public void setListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public abstract void addOnScrollListener(T onScrollListener);

    @Override
    public void setHasMore(boolean isHasMore, boolean isRefresh) {
        this.isHasMore = isHasMore;
        getLoadingPullableAdapter().setHasMoreData(isHasMore, isRefresh);
    }

    /**
     * 停止加载更多
     *
     * @param isSuccess
     */
    @Override
    public void stopLoadMore(boolean isSuccess) {
        if (isSuccess)
            getLoadingPullableAdapter().onSuccess();
        else
            getLoadingPullableAdapter().onFailue();
        isLoading = false;
    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        getLoadingPullableAdapter().onLoadingData();
        isLoading = true;
        if (listener != null) {
            listener.onLoadMore();
        }
    }

    public OnLoadMoreListener getListener() {
        return listener;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isHasMore() {
        return isHasMore;
    }

    public LoadingPullableAdapter getLoadingPullableAdapter() {
        return loadingPullableAdapter;
    }
}
