package com.whaley.core.sample;

import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by YangZhi on 2017/7/18 18:02.
 */

public class SecondUseCase extends UseCase{

    public SecondUseCase(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable buildUseCaseObservable(Object object) {
        return null;
    }
}
