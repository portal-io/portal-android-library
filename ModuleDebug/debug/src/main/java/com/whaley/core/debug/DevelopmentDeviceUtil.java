package com.whaley.core.debug;

import android.text.TextUtils;

import com.whaley.core.appcontext.AppFileStorage;

import java.io.File;

/**
 * Author: qxw
 * Date:2017/8/30
 * Introduction:
 */

public class DevelopmentDeviceUtil {

    private static Boolean isDevelopmentDevice;

    public DevelopmentDeviceUtil() {
    }

    public static boolean isDevelopmentDevice() {
        if (isDevelopmentDevice == null) {
            String path = AppFileStorage.getAppDirPath() + File.separator + "develop.info";
            isDevelopmentDevice = Boolean.valueOf(isFileExist(path));
        }
        return isDevelopmentDevice.booleanValue();
    }


    private static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }
}
