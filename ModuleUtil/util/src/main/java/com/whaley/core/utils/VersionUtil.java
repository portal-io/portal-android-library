package com.whaley.core.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.whaley.core.appcontext.AppContextProvider;

/**
 * Created by dell on 2017/7/13.
 */

public class VersionUtil {

    public static String getVersionRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getVersionName() {
        PackageManager packageManager = AppContextProvider.getInstance().getContext().getPackageManager();
        PackageInfo packInfo = null;
        String version = "";
        try {
            packInfo = packageManager.getPackageInfo(
                    AppContextProvider.getInstance().getContext().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //
        }
        return version;
    }

    public static int getVersionCode() {
        int versionCode = 0;
        try {
            PackageManager packageManager = AppContextProvider.getInstance().getContext().getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(AppContextProvider.getInstance().getContext().getPackageName(),
                    0);
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            //
        }
        return versionCode;
    }

}
