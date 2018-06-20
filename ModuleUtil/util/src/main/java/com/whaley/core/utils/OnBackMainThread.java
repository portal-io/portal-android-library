package com.whaley.core.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Author: qxw
 * Date: 2017/7/11
 */

public class OnBackMainThread {

    public static void onBackMainThread(final OnBackMainThreadListener onBackMainThreadListener) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (onBackMainThreadListener != null)
                    onBackMainThreadListener.onBackMainThread();
            }
        });
    }

    public interface OnBackMainThreadListener {
        void onBackMainThread();
    }

}
