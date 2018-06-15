package com.whaley.core.debug;

import com.whaley.core.debug.logger.Log;

/**
 * Created by YangZhi on 2017/7/4 21:08.
 */

public class Debug {

    private static boolean isDebug = false;

    public static Log.Option buildLog() {
        return Log.newOption();
    }

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static boolean isDebug() {
        return isDebug || DevelopmentDeviceUtil.isDevelopmentDevice();
    }

}
