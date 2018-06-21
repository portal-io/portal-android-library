package com.whaley.core.sample;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by YangZhi on 2017/7/13 20:59.
 */

@Route(path = "/sample/service")
public class Service implements IProvider{
    private String name;

    public Service(String name){
        this.name = name;
    }

    public void say(Context context){
        Toast.makeText(context,"Service say",Toast.LENGTH_LONG).show();
    }

    @Override
    public void init(Context context) {

    }
}
