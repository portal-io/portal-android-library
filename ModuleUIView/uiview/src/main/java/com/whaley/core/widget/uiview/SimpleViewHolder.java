package com.whaley.core.widget.uiview;

import android.view.View;

/**
 * Created by YangZhi on 2017/3/14 14:03.
 */

public class SimpleViewHolder implements UIViewHolder {
    boolean isInited;

    View rootView;

    int viewType;

    int position;

    UIViewModel viewModel;

    public SimpleViewHolder(View rootView){
        this.rootView=rootView;
    }

    @Override
    public <R extends View> R getRootView() {
        return (R) this.rootView;
    }

    @Override
    public boolean isInited() {
        return isInited;
    }

    @Override
    public void setInited() {
        this.isInited=true;
    }

    @Override
    public int getViewType() {
        return viewType;
    }

    @Override
    public void setViewType(int viewType) {
        this.viewType=viewType;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position=position;
    }

    @Override
    public void bindData(UIViewModel viewModel) {
        this.viewModel=viewModel;
    }

    @Override
    public <R extends UIViewModel> R getData() {
        return (R) viewModel;
    }
}
