package com.whaley.core.uiframe.utils;

import android.app.Activity;

import com.whaley.core.uiframe.view.ToastView;

/**
 * Created by YangZhi on 2017/7/10 16:32.
 */

public class UIUtil {

    public static  <R> R getView(Activity activity) {
        if (activity != null)
            return null;
        try {
            return (R) activity;
        } catch (ClassCastException e) {
        }
        return null;
    }
}
