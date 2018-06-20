package com.whaley.core.sample;

import android.app.Application;
import android.os.Environment;

import com.whaley.core.appcontext.AppContextInit;

import java.io.File;

/**
 * Author: qxw
 * Date:2017/9/12
 * Introduction:
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppContextInit.appContextInit(getApplicationContext(), Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "whaleyvr");
    }
}
