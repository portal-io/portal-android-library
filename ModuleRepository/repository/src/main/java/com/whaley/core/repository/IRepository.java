package com.whaley.core.repository;

/**
 * Created by YangZhi on 2017/6/29 15:23.
 */

public interface IRepository {
    <T> T obtainService(Class<T> remoteService);
}
