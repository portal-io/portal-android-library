package com.whaley.core.widget.uiview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Author: qxw
 * Date: 2017/3/10
 */

public class UIView extends FrameLayout{

    public static UIAdapter defulatAdapter;

    UIAdapter adapter;

    View rootView;

    UIViewHolder rootUIViewHolder;

    private UIViewModel uiViewModel;


    public UIView(@NonNull Context context) {
        super(context);
    }

    public UIView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UIView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        checkRootView();
    }

    private boolean checkRootView(){
        checkAdapter();
        if(rootView==null&&adapter!=null){
            rootUIViewHolder =adapter.onCreateView(this, uiViewModel.getType());
            rootView= rootUIViewHolder.getRootView();
            addView(rootView);
            update();
            return true;
        }
        return false;
    }


    private void checkAdapter(){
        if(adapter==null){
            adapter=defulatAdapter;
        }
    }

    public void update(){
        if(!checkRootView())
            adapter.onBindView(rootUIViewHolder, uiViewModel,0);
    }




    public void setUIViewModel(UIViewModel uiViewModel) {
        this.uiViewModel = uiViewModel;
    }

    public void setAdapter(UIAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public UIViewHolder getRootUIViewHolder() {
        return rootUIViewHolder;
    }
}
