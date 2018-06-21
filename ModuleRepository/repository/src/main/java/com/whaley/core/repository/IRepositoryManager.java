package com.whaley.core.repository;

/**
 * Created by YangZhi on 2017/6/28 19:41.
 */

public interface IRepositoryManager {
    /**
     * 根据传入的Class获取对应的Remote service
     *
     * @param remoteService
     * @param <T>
     * @return
     */
    <T> T obtainRemoteService(Class<T> remoteService);

    /**
     * 根据传入的Class获取对应的memory service
     *
     * @param memoryService
     * @param <T>
     * @return
     */
    <T> T obtainMemoryService(Class<T> memoryService);


    /**
     * 根据传入的Class获取对应的local service
     *
     * @param localService
     * @param <T>
     * @return
     */
    <T> T obtainLocalService(Class<T> localService);
}
