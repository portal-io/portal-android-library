package com.whaleyvr.core.network.http.subscriber;

import com.whaleyvr.core.network.http.response.Response;

import io.reactivex.disposables.Disposable;

/**
 * 定义请求结果处理接口
 */

public interface ISubscriber<T extends Response> {

    void doOnSubscribe(Disposable d);

    void doOnError(String errorMsg);

    void doOnNext(T t);

    void doOnCompleted();

}
