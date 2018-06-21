package com.whaley.core.router;

/**
 * Created by YangZhi on 2017/7/13 16:58.
 */

public class RouterTag {
    private RouterMeta routerMeta;

    private Object tag;

    public RouterTag(RouterMeta routerMeta){
        this.routerMeta = routerMeta;
    }

    public RouterMeta getRouterMeta() {
        return routerMeta;
    }

    public void setRouterMeta(RouterMeta routerMeta) {
        this.routerMeta = routerMeta;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
