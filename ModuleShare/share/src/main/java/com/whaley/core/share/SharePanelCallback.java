package com.whaley.core.share;

import android.app.Activity;

import com.whaley.core.share.model.ShareParam;

/**
 * Author: qxw
 * Date: 2017/7/6
 */

public interface SharePanelCallback {
    void panelCallback(Activity context, ShareParam shareParam);
}
