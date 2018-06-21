package com.whaley.core.widget.refresh;

/**
 * Created by yangzhi on 16/5/19.
 */
public interface ILoadMoreView {

    void setHasMore(boolean isHasMore, boolean isRefresh);

    void stopLoadMore(boolean isSuccess);

    void startLoadMore();

}
