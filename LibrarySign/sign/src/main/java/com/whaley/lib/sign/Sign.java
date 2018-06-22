package com.whaley.lib.sign;

import android.content.Context;

/**
 * Created by dell on 2017/2/13.
 */

public class Sign {

    public static native String getPaySign(Context context);

    public static native String getShowSign(Context context);

    public static native String getWhaleyvrSign(Context context);

    public static native String getTestUserHistorySign(Context context);

    public static native String getUserHistorySign(Context context);

    public static native String getBISign(Context context);

    public static native String getTestWhaleyvrThirdPaySign(Context context);

    public static native String getWhaleyvrThirdPaySign(Context context);

    static {
        System.loadLibrary("sign_whaleyvr-jni");
    }

}
