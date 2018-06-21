package com.whaley.core.router;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by YangZhi on 2017/7/12 23:01.
 */

public class Router {

    private static class HOLD{
        private static final Router INSTANCE = new Router();
    }

    public static Router getInstance(){
        return HOLD.INSTANCE;
    }

    public static void init(Application application) {
        ARouter.init(application);
    }

    public NavigatorRouterMeta buildNavigation(String path){
        return new NavigatorRouterMeta(path);
    }

    public NavigatorRouterMeta buildNavigation(String path, String group){
        return new NavigatorRouterMeta(path, group);
    }

    public NavigatorRouterMeta buildNavigation(Uri url){
        return new NavigatorRouterMeta(url);
    }


    public ExecutorRouterMeta buildExecutor(String path){
        return new ExecutorRouterMeta(path);
    }

    public ExecutorRouterMeta buildExecutor(String path, String group){
        return new ExecutorRouterMeta(path, group);
    }

    public ExecutorRouterMeta buildExecutor(Uri url){
        return new ExecutorRouterMeta(url);
    }



    public ObjRouterMeta buildObj(String path){
        return new ObjRouterMeta(path);
    }

    public ObjRouterMeta buildObj(String path, String group){
        return new ObjRouterMeta(path, group);
    }

    public ObjRouterMeta buildObj(Uri url){
        return new ObjRouterMeta(url);
    }


    public static synchronized void openDebug() {
        ARouter.openDebug();
    }

    public static boolean debuggable() {
        return ARouter.debuggable();
    }


    public static synchronized void openLog() {
        ARouter.openLog();
    }

    public static synchronized void printStackTrace() {
        ARouter.printStackTrace();
    }

    public static synchronized void setExecutor(ThreadPoolExecutor tpe) {
        ARouter.setExecutor(tpe);
    }

    public synchronized void destroy() {
        ARouter.getInstance().destroy();
    }


    public <T> T navigation(Class<? extends T> service) {
        return ARouter.getInstance().navigation(service);
    }

    public Object navigation(Context mContext, RouterMeta routerMeta, int requestCode, RouterCallback callback) {
        return ARouter.getInstance().navigation(mContext, routerMeta.getPostcard(), requestCode, TransFormUtil.tranformToNavigationCallback(callback));
    }

    /**
     * Inject params and services.
     */
    public void inject(Object thiz) {
        ARouter.getInstance().inject(thiz);
    }


}
