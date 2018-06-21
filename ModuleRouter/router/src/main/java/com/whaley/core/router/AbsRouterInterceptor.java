package com.whaley.core.router;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * Created by YangZhi on 2017/7/13 16:29.
 */

public abstract class AbsRouterInterceptor implements IInterceptor{

    @Override
    public final void process(final Postcard postcard, final InterceptorCallback callback) {
        if(postcard.getTag() ==null || !(postcard.getTag() instanceof RouterTag)){
            callback.onContinue(postcard);
            return;
        }
        RouterTag routerTag = (RouterTag) postcard.getTag();
        RouterMeta routerMeta = routerTag.getRouterMeta();
        process(routerMeta, new RouterInterceptorCallback() {
            @Override
            public void onContinue(RouterMeta routerMeta) {
                callback.onContinue(routerMeta.getPostcard());
            }

            @Override
            public void onInterrupt(Throwable exception) {
                postcard.setTag(null);
                callback.onInterrupt(exception);
            }
        });
    }

    public abstract void process(RouterMeta routerMeta, RouterInterceptorCallback callback);
}
