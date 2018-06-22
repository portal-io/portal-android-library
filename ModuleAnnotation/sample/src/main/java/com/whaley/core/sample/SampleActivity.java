package com.whaley.core.sample;

import android.os.Bundle;
import android.widget.Toast;

import com.whaley.core.uiframe.MVPActivity;
import com.whaley.core.uiframe.presenter.PagePresenter;

/**
 * Created by yangzhi on 2017/7/16.
 */

public class SampleActivity<PRESENTER extends SamplePresenter> extends MVPActivity<PRESENTER> implements View{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sample;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        findViewById(R.id.btn_showText).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                getPresenter().getFormatStr();
            }
        });
    }

    @Override
    public void updateText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
