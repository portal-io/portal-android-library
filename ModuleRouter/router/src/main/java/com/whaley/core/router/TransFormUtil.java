package com.whaley.core.router;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;

/**
 * Created by YangZhi on 2017/7/13 17:16.
 */

public class TransFormUtil {
    public static NavigationCallback tranformToNavigationCallback(final RouterCallback callback){
        return new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {
                RouterTag routerTag = (RouterTag) postcard.getTag();
                callback.onFound(routerTag.getRouterMeta());
            }

            @Override
            public void onLost(Postcard postcard) {
                RouterTag routerTag = (RouterTag) postcard.getTag();
                callback.onLost(routerTag.getRouterMeta());
            }

            @Override
            public void onArrival(Postcard postcard) {
                RouterTag routerTag = (RouterTag) postcard.getTag();
                callback.onArrival(routerTag.getRouterMeta());
            }

            @Override
            public void onInterrupt(Postcard postcard) {
                RouterTag routerTag = (RouterTag) postcard.getTag();
                callback.onInterrupt(routerTag.getRouterMeta());
            }
        };
    }
}
