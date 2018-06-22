package com.whaley.core.uiframe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.whaley.core.inject.InjectUtil;
import com.whaley.core.inject.annotation.Presenter;
import com.whaley.core.uiframe.presenter.PagePresenter;
import com.whaley.core.uiframe.view.PageView;

/**
 * Created by YangZhi on 2017/7/10 16:30.
 */

public abstract class MVPActivity<PRESENTER extends PagePresenter> extends BaseActivity implements PageView {

    @Presenter
    PRESENTER presenter;

    public MVPActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        InjectUtil.getInstance().inject(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle arguments = getIntent().getExtras();
        getPresenter().onCreate(arguments, savedInstanceState);
        getPresenter().onAttached();
        super.initView(savedInstanceState);
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        getPresenter().onViewCreated(arguments, savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPresenter().onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onViewDestroyed();
        getPresenter().onDetached();
        getPresenter().onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getPresenter().onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPresenter().onNewIntent(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getPresenter().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (getPresenter().onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public Activity getAttachActivity() {
        return this;
    }

    public final PRESENTER getPresenter() {
        if( presenter == null ){
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
}
