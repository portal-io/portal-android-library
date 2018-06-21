# APPContext 模块
#### 该模块为获取全局context相关模块

##### 已支持功能 :


##### 使用示例

* 依赖
    ```
      compile 'com.whaley.core:image:0.0.2'
      compile 'com.whaley.core:appcontext:0.0.1'
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
            静态创建内部实例化（图片处理需要用到context）
            AppContextInit.appContextInit(getApplicationContext(), "一个完整的地址");
        }
    ```
* 使用方法
 ```java
     获取 ImageRequest.RequestManager实例;
     ImageRequest.RequestManager imageRequest=ImageLoader.with(this);
     异步加载图片
     imageRequest
     .load(IMAGE_URL) //图片地址url，bitmap
     .centerCrop()   //图片裁剪方式，和ImageView 的scaleType值一样 默认同等方法有 fitCenter() 和fitXY() 还可以自己设置setScaleType(ScaleType scaleType);
     .small()       //图片大小 small() 屏幕宽度的1/4  medium()屏幕宽度的1/2  big()屏幕宽度 也可以自己设置大小size(int width, int height);
     .circle()     //圆形图片设置
     .into(imageView1); //显示图片的ImageView;

     其他设置
     cookie(Map<String, HashMap<String, String>> cookie)  //根据cookie来显示图片
     error(int errorResId)  //错误图片设置
     placeholder(int placeholderResId) //图片占位图
 ```