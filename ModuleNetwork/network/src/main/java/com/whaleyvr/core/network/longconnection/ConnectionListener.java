package com.whaleyvr.core.network.longconnection;

import com.mw.persistent.connect.info.UserInfo;

/**
 * Created by mafei on 2017/3/16.
 */

public interface ConnectionListener {
    void onReceive(String s);

    void onDeviceLogin(String s);

    void onUserLogin(UserInfo userInfo);

    void onUserLogout(UserInfo userInfo);

    void unAvailable(int i, String s);

    void available();
}
