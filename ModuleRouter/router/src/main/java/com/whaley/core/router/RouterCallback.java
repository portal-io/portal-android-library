package com.whaley.core.router;


/**
 * Created by YangZhi on 2017/7/12 23:05.
 */

public interface RouterCallback {

    /**
     * Callback when find the destination.
     *
     * @param routerMeta meta
     */
    void onFound(RouterMeta routerMeta);

    /**
     * Callback after lose your way.
     *
     * @param routerMeta meta
     */
    void onLost(RouterMeta routerMeta);

    /**
     * Callback after navigation.
     *
     * @param routerMeta meta
     */
    void onArrival(RouterMeta routerMeta);

    /**
     * Callback on interrupt.
     *
     * @param routerMeta meta
     */
    void onInterrupt(RouterMeta routerMeta);
}
