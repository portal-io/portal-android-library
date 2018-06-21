package com.whaley.core.router;

import android.net.Uri;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.enums.RouteType;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.Map;


public class RouterMeta {

    private final Postcard postcard;

    private final RouterTag routerTag;

    RouterMeta(String path){
        postcard = ARouter.getInstance().build(path);
        routerTag =new RouterTag(this);
        postcard.setTag(routerTag);
    }

    RouterMeta(String path, String group){
        postcard = ARouter.getInstance().build(path,group);
        routerTag =new RouterTag(this);
        postcard.setTag(routerTag);
    }

    RouterMeta(Uri url){
        postcard = ARouter.getInstance().build(url);
        routerTag =new RouterTag(this);
        postcard.setTag(routerTag);
    }


    Postcard getPostcard() {
        return postcard;
    }

    public Object getTag() {
        return routerTag.getTag();
    }

    public RouterMeta setTag(Object tag) {
        routerTag.setTag(tag);
        return this;
    }


    public Map<String, Integer> getParamsType() {
        return getPostcard().getParamsType();
    }

    public RouterMeta setParamsType(Map<String, Integer> paramsType) {
        getPostcard().setParamsType(paramsType);
        return this;
    }

    public RouteType getType() {
        return getPostcard().getType();
    }

    public RouterMeta setType(RouteType type) {
        getPostcard().setType(type);
        return this;
    }

    public Class<?> getDestination() {
        return getPostcard().getDestination();
    }

    public RouterMeta setDestination(Class<?> destination) {
        getPostcard().setDestination(destination);
        return this;
    }

    public String getPath() {
        return getPostcard().getPath();
    }

    public RouterMeta setPath(String path) {
        getPostcard().setPath(path);
        return this;
    }

    public String getGroup() {
        return getPostcard().getGroup();
    }

    public RouterMeta setGroup(String group) {
        getPostcard().setGroup(group);
        return this;
    }

    public int getPriority() {
        return getPostcard().getPriority();
    }

    public RouterMeta setPriority(int priority) {
        getPostcard().setPriority(priority);
        return this;
    }

    public int getExtra() {
        return getPostcard().getExtra();
    }

    public RouterMeta setExtra(int extra) {
        getPostcard().setExtra(extra);
        return this;
    }
}