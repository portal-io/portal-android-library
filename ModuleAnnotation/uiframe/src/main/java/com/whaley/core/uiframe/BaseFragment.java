package com.whaley.core.uiframe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.core.uiframe.utils.UIUtil;
import com.whaley.core.uiframe.view.LoadingView;
import com.whaley.core.uiframe.view.PageView;
import com.whaley.core.uiframe.view.ToastView;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by YangZhi on 2017/7/10 15:08.
 */

public abstract class BaseFragment extends Fragment implements PageView{

    protected static String TAG;

    private View rootView;

    private Unbinder unbinder;

    public BaseFragment(){
        TAG = getClass().getSimpleName();
        setRetainInstance();
    }

    protected void setRetainInstance() {
        if (getParentFragment() == null) {
            setRetainInstance(true);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
    }

    protected abstract int getLayoutId();

    protected void bindViews(View view) {
        this.rootView = view;
        unBindViews();
        unbinder= ButterKnife.bind(this, view);
    }

    protected void unBindViews() {
        if(unbinder!=null) {
            unbinder.unbind();
            this.rootView = null;
            unbinder=null;
        }
    }

    public View getRootView() {
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //解决activity 被系统销毁后恢复fragment的依赖的activity不是同一个activity导致activity is destroyed crash问题
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
//            Logger.e(e,TAG+"onDetach");
        } catch (IllegalAccessException e) {
//            Logger.e(e,TAG+"onDetach");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBindViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showLoading(String text) {
        LoadingView loadingView = UIUtil.getView(getActivity());
        if (loadingView == null)
            return;
        loadingView.showLoading(text);
    }

    @Override
    public void removeLoading() {
        LoadingView loadingView = UIUtil.getView(getActivity());
        if (loadingView == null)
            return;
        loadingView.removeLoading();
    }

    @Override
    public void showToast(String text) {
        ToastView toastView = UIUtil.getView(getActivity());
        if (toastView == null)
            return;
        toastView.showToast(text);
    }


    public boolean onBackPressed() {
        return false;
    }

    public boolean onNewIntent(Intent intent) {
        return false;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void finish() {
        getActivity().finish();
    }

    @Override
    public Activity getAttachActivity() {
        return getActivity();
    }
}
