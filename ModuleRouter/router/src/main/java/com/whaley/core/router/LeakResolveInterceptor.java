package com.whaley.core.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * Created by YangZhi on 2017/10/13 14:21.
 */
@Interceptor(priority = Integer.MAX_VALUE)
public final class LeakResolveInterceptor implements IInterceptor {

    @Override
    public void init(Context context) {

    }

    @Override
    public final void process(Postcard postcard, final InterceptorCallback callback) {
        if(postcard.getTag() ==null || !(postcard.getTag() instanceof RouterTag)){
            callback.onContinue(postcard);
            return;
        }
        postcard.setTag(null);
    }
}
