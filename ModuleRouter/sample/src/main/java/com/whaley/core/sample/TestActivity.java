package com.whaley.core.sample;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

/**
 * Created by YangZhi on 2017/7/13 17:36.
 */

@Route(path = "/sample/test1")
public class TestActivity extends Activity{

    @Autowired(name = "/sample/task/toast")
    Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Router.getInstance().inject(this);
        executor.excute(10,null);
    }
}
