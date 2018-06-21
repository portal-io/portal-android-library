package com.whaley.core.repository;

import com.google.common.base.Preconditions;
import com.whaleyvr.core.network.http.HttpManager;

/**
 * Created by YangZhi on 2017/6/29 15:25.
 */

public class RemoteRepository implements IRepository {

    private static final class Holder{
        private static final RemoteRepository instance = new RemoteRepository();
    }

    public static RemoteRepository getInstance() {
        return Holder.instance;
    }


    private RemoteRepository() {
    }

    @Override
    public <T> T obtainService(Class<T> remoteService) {
        Preconditions.checkNotNull(remoteService);
        return HttpManager.getInstance().getRetrofit(remoteService).create(remoteService);
    }
}
