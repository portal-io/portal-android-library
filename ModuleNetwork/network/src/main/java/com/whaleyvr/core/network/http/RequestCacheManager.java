package com.whaleyvr.core.network.http;

/**
 * Created by YangZhi on 2017/2/14 20:02.
 */

public interface RequestCacheManager {
    boolean shouldCache(String url);
}
