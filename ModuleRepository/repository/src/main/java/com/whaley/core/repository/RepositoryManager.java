package com.whaley.core.repository;


/**
 * 数据仓储管理对象
 * Created by YangZhi on 2017/6/28 19:46.
 */

public class RepositoryManager implements IRepositoryManager{

    private static volatile RepositoryManager INSTANCE;

    private final IRepository remoteRepository;

    private final IRepository localRepository;

    private final IRepository memoryRepository;

    public static RepositoryManager create(IRepository memoryRepository) {
       return new RepositoryManager(RemoteRepository.getInstance(),LocalRepository.getInstance(),memoryRepository);
    }

    public static RepositoryManager create(IRepository remoteRepository, IRepository localRepository,IRepository memoryRepository) {
        return new RepositoryManager(remoteRepository,localRepository,memoryRepository);
    }

    private RepositoryManager(IRepository remoteRepository, IRepository localRepository,IRepository memoryRepository) {
        if(remoteRepository == null){
            this.remoteRepository = RemoteRepository.getInstance();
        }else {
            this.remoteRepository = remoteRepository;
        }
        if(localRepository == null){
            this.localRepository = LocalRepository.getInstance();
        }else {
            this.localRepository = localRepository;
        }
        this.memoryRepository = memoryRepository;
    }

    @Override
    public <T> T obtainRemoteService(Class<T> remoteService) {
        return remoteRepository.obtainService(remoteService);
    }

    @Override
    public <T> T obtainMemoryService(Class<T> memoryService) {
        return memoryRepository.obtainService(memoryService);
    }

    @Override
    public <T> T obtainLocalService(Class<T> localService) {
        return localRepository.obtainService(localService);
    }
}
