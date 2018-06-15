# APPContext 模块
#### 该模块为获取全局context相关模块

##### 已支持功能 :


##### 使用示例

* 依赖
    ```
    compile 'com.whaley.core:appcontext:0.0.1'
    ```

* 自定义 Application 中初始化

    ```java
        @Override
        public void onCreate() {
            super.onCreate();
            静态创建内部实例化
            AppContextInit.appContextInit(getApplicationContext(), "一个完整的地址");

        }
    ```
* 使用方法
 ```java
    AppContextProvider.getInstance().getContext() //获取全局context
    AppContextProvider.getInstance().getAppDirPath() //获取全局的AppDirPath地址
 ```