package com.whaley.core.utils;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.AppFileStorage;

import java.io.File;

/**
 * Created by YangZhi on 2017/5/11 17:34.
 */

public class DevelopmentDeviceUtil {

    private static Boolean isDevelopmentDevice;

    public static boolean isDevelopmentDevice() {
        if (isDevelopmentDevice == null) {
            String path = AppFileStorage.getAppDirPath() + File.separator + "develop.info";
            isDevelopmentDevice = FileUtils.isFileExist(path);
        }
        return isDevelopmentDevice;
    }
}
