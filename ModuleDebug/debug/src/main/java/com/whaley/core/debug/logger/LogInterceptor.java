package com.whaley.core.debug.logger;

/**
 * Created by YangZhi on 2017/7/5 16:07.
 */

public interface LogInterceptor {
    boolean log(int priority, String tag, String messag);
}
