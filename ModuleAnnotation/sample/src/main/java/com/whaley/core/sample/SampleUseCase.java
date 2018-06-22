package com.whaley.core.sample;

import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;

/**
 * Created by yangzhi on 2017/7/16.
 */

public class SampleUseCase extends UseCase<String, String>{

    @com.whaley.core.inject.annotation.UseCase
    SecondUseCase useCase;

    public SampleUseCase(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(final String param) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("Format str "+param);
            }
        });
    }
}
