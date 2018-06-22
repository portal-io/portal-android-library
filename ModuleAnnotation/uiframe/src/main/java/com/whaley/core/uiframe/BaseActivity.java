package com.whaley.core.uiframe;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.whaley.core.uiframe.view.LoadingView;
import com.whaley.core.uiframe.view.ToastView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by YangZhi on 2017/7/10 15:08.
 */

public abstract class BaseActivity extends FragmentActivity implements ToastView, LoadingView{

    protected static String TAG;

    private ProgressDialog progressDialog;

    private Unbinder unbinder;

    public BaseActivity(){
        TAG = getClass().getSimpleName();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        onViewCreated(savedInstanceState);
    }

    protected void initView(Bundle savedInstanceState){
        setContentView(getLayoutId());
    }

    protected void onViewCreated(Bundle savedInstanceState){
        bindViews();
    }

    protected abstract int getLayoutId();

    protected void bindViews() {
        unBindViews();
        unbinder= ButterKnife.bind(this);
    }

    protected void unBindViews() {
        if(unbinder!=null) {
            unbinder.unbind();
            unbinder=null;
        }
    }

    @Override
    public void showLoading(String text) {

    }

    @Override
    public void removeLoading() {

    }

    @Override
    public void showToast(String text) {

    }
}
