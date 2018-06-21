package com.whaley.core.sample;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.core.router.Executor;

/**
 * Created by YangZhi on 2017/7/13 17:40.
 */
@Route(path = "/sample/task/toast")
public class ToastExecutor implements Executor<Integer>,IProvider{

    private Context context;

    @Override
    public void excute(Integer params, Callback callback) {
        Toast.makeText(context,"执行 Toast 值为"+params,Toast.LENGTH_LONG).show();
        callback.onCall("哈哈");
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }
}
