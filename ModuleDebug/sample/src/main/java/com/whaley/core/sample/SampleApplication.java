package com.whaley.core.sample;

import android.app.Application;
import android.os.Environment;

import com.orhanobut.logger.BuildConfig;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.crash.CrashHandler;
import com.whaley.core.debug.logger.LogInterceptor;

import java.io.File;

/**
 * Created by YangZhi on 2017/7/6 18:35.
 */

public class SampleApplication extends Application {

    static final String APP_DIR_NAME = "whaleyvr" + File.separator + "sample";

    static final String LOGGER_DIR_NAME = "logger";

    @Override
    public void onCreate() {
        super.onCreate();

        // Application onCreate 中初始化 CrashHandler
        // 当 Crash 发生时能在本地输出日志
        CrashHandler.getInstance().init();

        // 构建 日志对象
        Debug.buildLog()
                .showThreadInfo(true) // 日志是否打印的当前线程
                .methodCount(2) // 日志打印该条日志的调用栈方法层数
                .methodOffset(0) // 日志打印该条日志的调用栈方法偏移层数
                .logDisk(getLogDir()) // 如果需要打印到本地 则提供一个打印到本地的文件夹路径
                .tag("Sample") // 日志打印的全局 Tag
                .intercept(new LogInterceptor() {  // 日志打印的拦截器 在这里可以配置是否拦截本次打印
                    @Override
                    public boolean log(int priority, String tag, String messag) {
                        return false;
                    }
                })
//                .disable(BuildConfig.DEBUG) // 设置关闭日志打印的条件
                .build();
    }


    private String getLogDir(){
        return Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + APP_DIR_NAME + File.separator + LOGGER_DIR_NAME;
    }
}
