package com.whaley.core.widget.uiview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by YangZhi on 2017/3/14 13:43.
 */

public abstract class BaseUIAdapter<T extends UIViewHolder, H extends UIViewModel> implements UIAdapter<T, H>, UIViewClickSetter {

    T viewHolder;
    OnUIViewClickListener onUIViewClickListener;


    public void setOnUIViewClickListener(OnUIViewClickListener onUIViewClickListener) {
        this.onUIViewClickListener = onUIViewClickListener;
    }

    public OnUIViewClickListener getOnUIViewClickListener() {
        return onUIViewClickListener;
    }

    @Override
    public final T onCreateView(ViewGroup parent, int type) {
        viewHolder = onCreateViewHolder(parent, type);
        viewHolder.setViewType(type);
        return viewHolder;
    }

    public abstract T onCreateViewHolder(ViewGroup parent, int type);

    @Override
    public void onBindView(final T viewHolder, H uiViewModel, int position) {
        viewHolder.setPosition(position);
        viewHolder.bindData(uiViewModel);
        if (!viewHolder.isInited()) {
            if (onInitViewHolder(viewHolder, uiViewModel, position)) {
                viewHolder.setInited();
            }
        }
        onBindViewHolder(viewHolder, uiViewModel, position);
        setViewClick();
    }


    @Override
    public void onAttach() {

    }

    public void onDettatch(){

    }

    @Override
    public void onRecycled() {
        setOnUIViewClickListener(null);
    }

    @Override
    public void onDestroy() {

    }

    protected void setViewClick() {
        if (getOnUIViewClickListener() != null && viewHolder.getData().isCanClick()) {
            viewHolder.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewClick();
                }
            });
        }
    }

    protected void onViewClick() {
        getOnUIViewClickListener().onViewClick(viewHolder);
    }

    public boolean onInitViewHolder(T viewHolder, H uiViewModel, int position) {
        return false;
    }

    public abstract void onBindViewHolder(T viewHolder, H uiViewModel, int position);


    @Override
    public T getViewHolder() {
        return viewHolder;
    }
}
