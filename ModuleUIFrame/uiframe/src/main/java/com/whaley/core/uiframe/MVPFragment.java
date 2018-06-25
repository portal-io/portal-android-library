package com.whaley.core.uiframe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.whaley.core.inject.InjectUtil;
import com.whaley.core.inject.annotation.Presenter;
import com.whaley.core.uiframe.presenter.PagePresenter;
import com.whaley.core.uiframe.view.PageView;

/**
 * Created by YangZhi on 2017/7/10 18:29.
 */

public abstract class MVPFragment<PRESENTER extends PagePresenter> extends BaseFragment implements PageView {

    @Presenter
    private PRESENTER presenter;

    private boolean isInjected;

    public MVPFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInject();
        getPresenter().onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().onViewCreated(getArguments(), savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        getPresenter().onViewDestroyed();
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        checkInject();
        getPresenter().onAttached();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getPresenter().onDetached();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        getPresenter().onStop();
    }

    @Override
    public boolean onNewIntent(Intent intent) {
        getPresenter().onNewIntent(intent);
        return true;
    }

    @Override
    public boolean onBackPressed() {
        return getPresenter().onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getPresenter().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public final PRESENTER getPresenter() {
        if (presenter == null) {
            presenter = onCreatePresenter();
            InjectUtil.getInstance().inject(presenter);
        }
        return presenter;
    }

    public void setPresenter(PRESENTER presenter) {
        this.presenter = presenter;
        InjectUtil.getInstance().inject(presenter);
    }

    protected PRESENTER onCreatePresenter() {
        return null;
    }

    private void checkInject() {
        if (!isInjected) {
            onInject();
            isInjected = true;
        }
    }

    protected void onInject() {
        InjectUtil.getInstance().inject(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getPresenter().onSaveInstanceState(outState);
    }
}
