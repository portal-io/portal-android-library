package com.whaley.core.appcontext;

import android.content.Context;

/**
 * Author: qxw
 * Date: 2017/7/6
 */

public class AppContextInit {

    public static void appContextInit(Context context, String appDirPath) {
        AppContextProvider.getInstance().init(context, appDirPath);
    }

}
