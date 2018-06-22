package com.whaley.core.sample;

import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.repository.RemoteRepository;
import com.whaley.core.uiframe.presenter.PagePresenter;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by yangzhi on 2017/7/16.
 */

public class SamplePresenter extends PagePresenter<View>{

    @Repository(type = Repository.LOCAL)
    private SampleMemoryRepository repository;


    @UseCase
    private SampleUseCase useCase;

    public SamplePresenter(View view) {
        super(view);
    }

    public void getFormatStr(){
        useCase.execute(new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {
                getUIView().updateText(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        },"哈哈哈");
    }

}
