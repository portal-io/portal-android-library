package com.whaleyvr.core.network.longconnection;

import android.content.ComponentName;
import android.os.IBinder;

/**
 * Created by dell on 2017/7/10.
 */

public interface ConnectionStatusListener {

    void onServiceConnected(ConnectService.ConnectBinder connectBinder);
    void onServiceDisconnected();

}
