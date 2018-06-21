package com.whaley.core.widget.uiview;

import android.view.ViewGroup;

/**
 * Author: qxw
 * Date: 2017/3/10
 */

public interface UIAdapter<T extends UIViewHolder,H extends UIViewModel> {

    T onCreateView(ViewGroup parent, int type);

    void onBindView(T viewHolder, H uiViewModel, int position);

    T getViewHolder();

    void onAttach();

    void onDettatch();

    void onRecycled();

    void onDestroy();
}
