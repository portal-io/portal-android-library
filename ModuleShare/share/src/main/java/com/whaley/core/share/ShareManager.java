package com.whaley.core.share;

import android.content.Intent;

import com.whaley.core.share.model.ShareParam;


/**
 * Author: qxw
 * Date: 2016/11/21
 */

public interface ShareManager {
    void share(ShareParam shareParam);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
