package com.whaley.core.appcontext;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.File;

/**
 * Created by YangZhi on 2017/2/6 11:33.
 */

public class AppContextProvider {

    private static volatile AppContextProvider instance;

    private Context context;

    private Starter starter;

    private String downloadPath = "download";
    private String moviePath = "movie";
    private String updatePath = "update";
    private String splashPath = "splash";
    private String imagesPath = "images";
    private String imageAlbumPath = "album";
    private String imageThumbPath = "thumb";
    private String cachePath = "cache";
    private String dataPath = "data";
    private String h5Path = "h5";
    private String snapPath = "snap";


    /**
     * 有上层传一个完整的地址
     */
    private String appDirPath;

    protected void checkPath() {
        if (checkExternalStorageState()) {
            checkDownloadMoviePath();
            checkDownloadPath();
            checkUpdatePath();
            checkImageThumbPath();
            checkImageAlbumPath();
            checkCachePath();
            checkH5Path();
        }
    }

    protected String getDownloadPath() {
        return checkDownloadPath();
    }

    protected String getDownloadMoviePath() {
        return checkDownloadMoviePath();
    }

    protected String getUpdatePath() {
        return checkUpdatePath();
    }

    protected String getImagePath() {
        return checkImageThumbPath();
    }

    protected String getSplashPlayPath() {
        return checkSplashPlayPath();
    }


    //获取相册地址
    protected String getImageAlbumPath() {
        return checkImageAlbumPath();
    }


    protected String getCachePath() {
        return checkCachePath();
    }


    //获取H5相关模板本地地址
    protected String getH5Path() {
        return checkH5Path();
    }

    protected String getSnapPath() {
        return checkSnapPath();
    }

    public static AppContextProvider getInstance() {
        if (instance == null) {
            synchronized (AppContextProvider.class) {
                if (instance == null) {
                    instance = new AppContextProvider();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context    上下文
     * @param appDirPath 有上层传一个完整的地址
     */
    void init(Context context, String appDirPath) {
        this.context = context.getApplicationContext();
        this.appDirPath = appDirPath;
    }

    protected String getAppDirPath() {
        return appDirPath;
    }

    public Context getContext() {
        return context;
    }


    public Starter getStarter() {
        if (starter == null) {
            starter = new Starter() {
                @Override
                public void startActivityForResult(Intent intent, int requestCode) {
                    startActivity(intent);
                }

                @Override
                public void startActivity(Intent intent) {
                    getContext().startActivity(intent);
                }

                @Override
                public Context getAttatchContext() {
                    return getContext();
                }

                @Override
                public void transitionAnim(int enterAnim, int outAnim) {

                }

                @Override
                public void finish() {

                }
            };
        }
        return starter;
    }


    /**
     * @return
     */
    private String checkDownloadPath() {
        String path = appDirPath + File.separator + downloadPath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * @return
     */
    private String checkDownloadMoviePath() {
        String path = appDirPath + File.separator + moviePath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private String checkUpdatePath() {
        String path = appDirPath + File.separator + updatePath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private String checkSplashPlayPath() {
        String path = appDirPath + File.separator + splashPath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private String checkImageAlbumPath() {
        String path = appDirPath + File.separator + imagesPath + File.separator + imageAlbumPath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private String checkImageThumbPath() {
        String path = appDirPath + File.separator + imagesPath + File.separator + imageThumbPath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private String checkH5Path() {
        String path = appDirPath + File.separator + dataPath + File.separator + h5Path;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    private String checkCachePath() {
        String path = appDirPath + File.separator + cachePath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private String checkSnapPath() {
        String path = appDirPath + File.separator + snapPath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private boolean checkExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    /**
     * 下载地址
     *
     * @param downloadPath
     */
    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    /**
     * 视频保存地址
     *
     * @param moviePath
     */
    public void setMoviePath(String moviePath) {
        this.moviePath = moviePath;
    }

    /**
     * 升级app保存
     *
     * @param updatePath
     */
    public void setUpdatePath(String updatePath) {
        this.updatePath = updatePath;
    }

    /**
     * 开机启动保存
     *
     * @param splashPath
     */
    public void setSplashPath(String splashPath) {
        this.splashPath = splashPath;
    }

    /**
     * 图片地址
     *
     * @param imagesPath
     */
    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    /**
     * 图片浏览器
     *
     * @param imageAlbumPath
     */
    public void setImageAlbumPath(String imageAlbumPath) {
        this.imageAlbumPath = imageAlbumPath;
    }


    /**
     * 缩略图
     *
     * @param imageThumbPath
     */
    public void setImageThumbPath(String imageThumbPath) {
        this.imageThumbPath = imageThumbPath;
    }

    /**
     * 缓存地址
     *
     * @param cachePath
     */
    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    /**
     * 数据地址
     *
     * @param dataPath
     */
    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    /**
     * h5地址
     *
     * @param h5Path
     */
    public void setH5Path(String h5Path) {
        this.h5Path = h5Path;
    }

    public void setSnapPath(String snapPath) {
        this.snapPath = snapPath;
    }
}
