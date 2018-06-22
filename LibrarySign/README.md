# LibrarySignKey 模块
#### 该模块为jni模块

##### 已支持功能 :
  获取MD5加密的签名

##### 使用示例

* 依赖
    ```
    compile 'com.whaley.lib:sign:0.0.1'
    ```
* 修改jni文件
  1:修改 sample目录下的jni文件的sign_whaleyvr.cpp
  2:so库生成
  3:将so库移植到sign目录下的jniLibs下（一一对应）
* so文件生成方法（studio 下载好ndk）
  命令行：项目目录\sample\src\main>ndk-build

* 上传maven
  先将sample目录下的jni文件删除再上传