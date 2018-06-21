package com.whaley.core.widget.refresh;

import android.view.View;
import android.view.ViewGroup;

/**
 * 需要下拉刷新或上拉加载数据的Adapter
 * Created by yangzhi on 15/11/28.
 */
public interface LoadingPullableAdapter {

    /**
     * 当数据加载完成并成功的时候回调
     */
    void onSuccess();

    /**
     * 当数据加载完成并失败的时候回调
     */
    void onFailue();

    /**
     * 当正在加载数据中的回调
     */
    void onLoadingData();

    /**
     * 设置是否还有更多数据
     * @param hasMore 是否还有更多数据
     * @param isRefresh 是否是下拉刷新时设置
     */
    void setHasMoreData(boolean hasMore, boolean isRefresh);
}
