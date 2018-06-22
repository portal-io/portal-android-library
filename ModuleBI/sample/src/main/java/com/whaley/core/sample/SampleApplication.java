package com.whaley.core.sample;

import android.app.Application;

import com.whaley.core.appcontext.AppContextInit;
import com.whaley.core.bi.BILogServiceManager;
import com.whaley.core.bi.UserModelProvider;
import com.whaley.core.bi.model.UserModel;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.logger.LogInterceptor;

/**
 * Author: qxw
 * Date: 2017/7/7
 */

public class SampleApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AppContextInit.appContextInit(getApplicationContext(), "ssss");
        setBIServiceManager();
        Debug.buildLog()
                .showThreadInfo(true) // 日志是否打印的当前线程
                .methodCount(2) // 日志打印该条日志的调用栈方法层数
                .methodOffset(0) // 日志打印该条日志的调用栈方法偏移层数
//                .logDisk(getLogDir()) // 如果需要打印到本地 则提供一个打印到本地的文件夹路径
                .tag("Sample") // 日志打印的全局 Tag
                .intercept(new LogInterceptor() {  // 日志打印的拦截器 在这里可以配置是否拦截本次打印
                    @Override
                    public boolean log(int priority, String tag, String messag) {
                        return false;
                    }
                })
//                .disable(BuildConfig.DEBUG) // 设置关闭日志打印的条件
                .build();
        Debug.setDebug(true);
    }


    private void setBIServiceManager() {
        BILogServiceManager.getInstance().initData("暴雪", new UserModelProvider() {
            UserModel userModel = new UserModel();

            @Override
            public UserModel getUserModel() {
                userModel.setAccount_id("123");
                userModel.setUsreId("5458");
                return userModel;
            }
        });
    }
}
