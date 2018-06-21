package com.whaley.core.sample;

import android.app.Application;

import com.whaley.core.appcontext.AppContextInit;

/**
 * Author: qxw
 * Date: 2017/7/6
 */

public class VRApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppContextInit.appContextInit(getApplicationContext(), "");
    }
}
