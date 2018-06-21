package com.whaley.core.widget.uiview;

import android.view.View;

/**
 * Author: qxw
 * Date: 2017/3/10
 */

public interface UIViewHolder {
    <R extends View> R getRootView();

    boolean isInited();

    void setInited();

    int getViewType();

    void setViewType(int viewType);

    int getPosition();

    void setPosition(int position);

    void bindData(UIViewModel viewModel);

    <R extends UIViewModel> R getData();
}
