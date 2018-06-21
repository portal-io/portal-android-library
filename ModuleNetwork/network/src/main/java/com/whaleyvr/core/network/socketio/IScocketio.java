package com.whaleyvr.core.network.socketio;

/**
 * Created by dell on 2017/7/10.
 */

public interface IScocketio {

    void onConnected(Object... args);

    void onDisconnected(Object... args);

}
