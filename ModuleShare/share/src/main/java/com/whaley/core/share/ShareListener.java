package com.whaley.core.share;

/**
 * Author: qxw
 * Date: 2017/7/6
 */

public interface ShareListener {
    void onResult(int type);

    void onError(int type, Throwable var2);

    void onCancel(int type);
}
