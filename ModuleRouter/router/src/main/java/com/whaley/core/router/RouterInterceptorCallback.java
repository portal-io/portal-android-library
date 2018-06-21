package com.whaley.core.router;

/**
 * The callback of interceptor.
 *
 * @author Alex <a href="mailto:zhilong.liu@aliyun.com">Contact me.</a>
 * @version 1.0
 * @since 16/8/4 17:36
 */
public interface RouterInterceptorCallback {

    /**
     * Continue process
     *
     * @param routerMeta route meta
     */
    void onContinue(RouterMeta routerMeta);

    /**
     * Interrupt process, pipeline will be destory when this method called.
     *
     * @param exception Reson of interrupt.
     */
    void onInterrupt(Throwable exception);
}
