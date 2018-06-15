package com.whaley.core.debug.logger;

import android.os.Handler;
import android.os.HandlerThread;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.DiskLogStrategy;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


/**
 * 日志打印类
 * Created by YangZhi on 2017/7/4 21:09.
 */

public class Log {

    /**
     * 创建一个配置对象
     * @return
     */
    public static Option newOption(){
        return new Option();
    }

    public static void i(String message){
        Logger.i(message);
    }

    public static void i(String tag,String message){
        Logger.t(tag).i(message);
    }

    public static void d(String message){
        Logger.d(message);
    }

    public static void d(String tag,String message){
        Logger.t(tag).d(message);
    }


    public static void v(String message){
        Logger.v(message);
    }

    public static void v(String tag,String message){
        Logger.t(tag).v(message);
    }


    public static void w(String message){
        Logger.w(message);
    }

    public static void w(String tag,String message){
        Logger.t(tag).w(message);
    }

    public static void e(String message){
        Logger.e(message);
    }

    public static void e(String tag,String message){
        Logger.t(tag).e(message);
    }

    public static void e(Throwable throwable,String message){
        Logger.e(throwable,message);
    }

    public static void e(String tag,Throwable throwable,String message){
        Logger.t(tag).e(throwable,message);
    }


    public static class Option{

        private boolean isShowThread = true;
        private int methodCount = 2;
        private int methodOffset = 0;
        private String tag = "whaleyvr";
        private boolean isLogDisk = false;
        private String logDirPath;
        private LogInterceptor logInterceptor;
        private boolean isDisable;

        private Option(){}

        /**
         * 是否显示线程信息
         * @param isShowThread 是否显示线程
         * @return 配置对象
         */
        public Option showThreadInfo(boolean isShowThread){
            this.isShowThread = isShowThread;
            return this;
        }

        /**
         * 设置显示调用的方法数
         * @param count 方法数量
         * @return 配置对象
         */
        public Option methodCount(int count){
            this.methodCount = count;
            return this;
        }

        /**
         * 设置显示的方法偏移
         * @param offset 偏移量
         * @return 配置对象
         */
        public Option methodOffset(int offset){
            this.methodOffset = offset;
            return this;
        }

        /**
         * 设置全局 TAG
         * @param tag 全局 TAG 值
         * @return 配置对象
         */
        public Option tag(String tag){
            this.tag = tag;
            return this;
        }

        /**
         * 设置是否记录到本地
         * @param logDirPath 记录到本地的文件夹地址
         * @return 配置对象
         */
        public Option logDisk(String logDirPath){
            this.logDirPath = logDirPath;
            this.isLogDisk = true;
            return this;
        }

        /**
         * 设置打印的拦截器
         * @param logInterceptor 打印的拦截器
         * @return 配置对象
         */
        public Option intercept(LogInterceptor logInterceptor){
            this.logInterceptor = logInterceptor;
            return this;
        }

        /**
         * 设置不可用
         * @return 配置对象
         */
        public Option disable(){
            this.isDisable=true;
            return this;
        }

        public void build(){
            Logger.clearLogAdapters();
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(isShowThread)  // (Optional) Whether to show thread info or not. Default true
                    .methodCount(methodCount)         // (Optional) How many method line to show. Default 2
                    .methodOffset(methodOffset)        // (Optional) Hides internal method calls up to offset. Default 5
                    .logStrategy(new LogcatLogStrategy(){
                        @Override
                        public void log(int priority, String tag, String message) {
                            if(!logInterceptor.log(priority,tag,message)) {
                                super.log(priority, tag, message);
                            }
                        }
                    }) // (Optional) Changes the log strategy to print out. Default LogCat
                    .tag(tag)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                    .build();
            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
                @Override
                public boolean isLoggable(int priority, String tag) {
                    return !isDisable;
                }
            });

            if(!isLogDisk || logDirPath == null || logDirPath.equals("")) {
                return;
            }

            HandlerThread ht = new HandlerThread("AndroidFileLogger." + logDirPath);
            ht.start();
            Handler handler = new WriteHandler(ht.getLooper(), logDirPath);
            DiskLogStrategy diskLogStrategy = new DiskLogStrategy(handler);

            formatStrategy = CsvFormatStrategy.newBuilder()
                    .logStrategy(diskLogStrategy)
                    .tag(tag)
                    .build();
            Logger.addLogAdapter(new DiskLogAdapter(formatStrategy){
                @Override
                public boolean isLoggable(int priority, String tag) {
                    return !isDisable;
                }
            });

        }
    }
}
