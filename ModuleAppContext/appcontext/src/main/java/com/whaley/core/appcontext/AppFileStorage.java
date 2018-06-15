package com.whaley.core.appcontext;

/**
 * Author: qxw
 * Date:2017/8/1
 * Introduction:app文件存储
 */

public class AppFileStorage {

    public static void checkPath() {
        AppContextProvider.getInstance().checkPath();
    }

    public static String getAppDirPath() {
        return AppContextProvider.getInstance().getAppDirPath();
    }

    public static String getDownloadPath() {
        return AppContextProvider.getInstance().getDownloadPath();
    }

    public static String getDownloadMoviePath() {
        return AppContextProvider.getInstance().getDownloadMoviePath();
    }

    public static String getUpdatePath() {
        return AppContextProvider.getInstance().getUpdatePath();
    }

    public static String getImagePath() {
        return AppContextProvider.getInstance().getImagePath();
    }

    public static String getSplashPlayPath() {
        return AppContextProvider.getInstance().getSplashPlayPath();
    }


    //获取相册地址
    public String getImageAlbumPath() {
        return AppContextProvider.getInstance().getImageAlbumPath();
    }


    public static String getCachePath() {
        return AppContextProvider.getInstance().getCachePath();
    }


    //获取H5相关模板本地地址
    public static String getH5Path() {
        return AppContextProvider.getInstance().getH5Path();
    }


    public static String getSnapPath() {
        return AppContextProvider.getInstance().getSnapPath();
    }
}
