package com.whaleyvr.core.network.http.exception;

/**
 * Created by yangzhi on 16/8/5.
 */
public class NetworkErrorException extends Throwable {
    public NetworkErrorException() {
    }

    public NetworkErrorException(String message) {
        super(message);
    }

    public NetworkErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkErrorException(Throwable cause) {
        super(cause);
    }
}
