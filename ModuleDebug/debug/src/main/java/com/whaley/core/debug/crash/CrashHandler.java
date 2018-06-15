package com.whaley.core.debug.crash;

import android.content.Context;
import android.os.Process;

import com.whaley.core.debug.logger.Log;

/**
 * Created by YangZhi on 2017/7/5 19:48.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler{

    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private static class Holder{
        private static final CrashHandler INSTANCE = new CrashHandler();
    }

    public static CrashHandler getInstance(){
        return Holder.INSTANCE;
    }

    public void init(){
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e(t.getName()+" is Crashed");
        Log.e(e,"Crash on " + t.toString());
        //如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }
}
