package com.whaleyvr.core.network.http.response;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Author: qxw
 * Date: 2017/7/13
 */

public class BaseObserver<T> implements Observer<Response<T>> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull Response<T> tResponse) {
        if (tResponse.checkStatus()) {
            onSuccess(tResponse, tResponse.getData());
        } else {
            onStatusError(tResponse.getStatus(), tResponse);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    public void onSuccess(Response<T> response, T date) {
        onSuccess(date);
    }

    public  void onSuccess(T date) {
    }

    public void onStatusError(int status, String msg) {
    }

    public void onStatusError(int status, Response<T> response) {
        onStatusError(status, response.getMsg());
    }
}
