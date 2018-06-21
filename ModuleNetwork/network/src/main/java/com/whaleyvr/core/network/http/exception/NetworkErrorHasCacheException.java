package com.whaleyvr.core.network.http.exception;

/**
 * Created by dell on 2017/2/6.
 */

public class NetworkErrorHasCacheException extends Throwable{

    public NetworkErrorHasCacheException() {
    }

    public NetworkErrorHasCacheException(String message) {
        super(message);
    }

    public NetworkErrorHasCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkErrorHasCacheException(Throwable cause) {
        super(cause);
    }

}
