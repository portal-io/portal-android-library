# Debug 模块
#### 该模块为调试与日志记录相关模块

##### 已支持功能 :
* Locat 日志格式化
* 日志记录到本地
* Crash 日志记录到本地

##### 使用示例

* 依赖
    ```
    compile 'com.whaley.core:debug:0.1.0'
    ```

* 自定义 Application 中初始化

    ```java
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
    ```
* 使用 Log 静态方法打印
    ```java
            Log.i("打印 i");
            Log.i(TAG,"打印 i 带 TAG");

            Log.d("打印 d");
            Log.d(TAG,"打印 d 带 TAG");

            Log.v("打印 v");
            Log.v(TAG,"打印 v 带 TAG");

            Log.w("打印 w");
            Log.w(TAG,"打印 w 带 TAG");

            Log.e("打印 e");
            Log.e(TAG,"打印 e 带 TAG");
            Throwable throwable = new NullPointerException("空指针");
            Log.e(throwable,"打印 e 带 Error");
            Log.e(TAG,throwable,"打印 e 带 TAG , Error");
    ```