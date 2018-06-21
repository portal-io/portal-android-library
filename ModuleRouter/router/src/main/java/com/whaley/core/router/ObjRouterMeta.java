package com.whaley.core.router;

import android.net.Uri;

/**
 * Created by YangZhi on 2017/7/13 16:52.
 */

public class ObjRouterMeta extends RouterMeta {

    public ObjRouterMeta(String path) {
        super(path);
    }

    public ObjRouterMeta(String path, String group) {
        super(path, group);
    }

    public ObjRouterMeta(Uri url) {
        super(url);
    }

    public <R> R getObj(){
        return (R) getPostcard().navigation();
    }
}
