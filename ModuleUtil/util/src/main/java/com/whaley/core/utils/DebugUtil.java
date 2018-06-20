package com.whaley.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;

import com.orhanobut.logger.Logger;

/**
 * Created by dell on 2016/11/3.
 */

public class DebugUtil {

    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {
            Logger.e(e, "isApkDebugable");
        }
        return false;
    }

    public final static String TEST_API = "test_api";

    public static void setTestApi(Context mContext, boolean status) {
        SharedPreferences preferences = mContext.getSharedPreferences(
                TEST_API, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(TEST_API, status);
        editor.commit();
    }

    public static boolean getTestApi(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(
                TEST_API, Context.MODE_PRIVATE);
        return preferences.getBoolean(TEST_API, false);
    }


}
