# BI基础模块
#### 该模块为BI统计模块

##### 已支持功能 :
* bi日志存储
* 日志上传

##### 使用示例

* 依赖
    ```
    compile 'com.whaley.core:bi:0.0.1'
    ```
* AndroidManifest 中添加权限
    ```xml
    <uses-permission android:name="android.permission.INTERNET" />
    ```
* 自定义 Application 中初始化

    ```java
        @Override
        public void onCreate() {
            super.onCreate();
            BILogServiceManager.getInstance().initData("渠道号", new UserModelProvider() {
                        UserModel userModel = new UserModel();
                        @Override
                        public UserModel getUserModel() {
                            userModel.setAccount_id("账户id");
                            userModel.setUsreId("设备id");
                            return userModel;
                        }
                    });
        }
    ```

* 使用方法
 ```java
     //配置LogInfoParam.Builder
     LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId("browse_view")
                    .setCurrentPageId("topic")
                    .putCurrentPagePropKeyValue("pageId", "1111111")
                    .putCurrentPagePropKeyValue("pageName", "猜猜我是谁")
                    .setNextPageId("topic");
     //配置记入日志
     BILogServiceManager.getInstance().recordLog(builder);
 ```