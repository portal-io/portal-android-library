package com.whaleyvr.core.network.longconnection;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.mw.persistent.connect.header.MsgHeader;
import com.mw.persistent.connect.info.UserInfo;
import com.whaley.core.appcontext.AppContextProvider;

/**
 * Created by mafei on 2017/3/16.
 */

public class Connection extends ApplicationLifeCycleCallback {
    private static Connection ourInstance = new Connection();

    //private ConnectionListener mConnectionListener;

    private Context sApplication;

    public ConnectionStatusListener getConnectionStatusListener() {
        return connectionStatusListener;
    }

    public void setConnectionStatusListener(ConnectionStatusListener connectionStatusListener) {
        this.connectionStatusListener = connectionStatusListener;
    }

    private ConnectionStatusListener connectionStatusListener;

    //private String deviceId;
    //private Context mContext;
    private ConnectService.ConnectBinder mConnectBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
            mConnectBinder = (ConnectService.ConnectBinder) service;
            if(connectionStatusListener!=null){
                connectionStatusListener.onServiceConnected(mConnectBinder);
            }
        }

        @Override public void onServiceDisconnected(ComponentName name) {
            mConnectBinder = null;
            if(connectionStatusListener!=null){
                connectionStatusListener.onServiceDisconnected();
            }
        }
    };

    private Connection() {
        sApplication = AppContextProvider.getInstance().getContext();
    }

    public static Connection getInstance() {
        return ourInstance;
    }

    @Override protected void onApplicationExit(long duration) {
        sApplication.unbindService(mServiceConnection);
    }

    @Override protected void onApplicationEnter() {
        sApplication.bindService(new Intent(sApplication, ConnectService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    public void setupConnectAction(ConnectionListener connectionListener) {
        if (mConnectBinder == null) {
            return;
        }
        mConnectBinder.setTcpListener(connectionListener);
    }

    public void sendMsg(MsgHeader msgHeader) {
        if (mConnectBinder != null) {
            mConnectBinder.send(msgHeader);
        }
    }

    public void connect(Context context, String deviceId, ConnectionListener listener) {
        if (mConnectBinder != null) {
            mConnectBinder.connect(context, deviceId, listener);
        }
    }

    public void connect(Context context, String deviceId) {
        if (mConnectBinder != null) {
            mConnectBinder.connect(context, deviceId);
        }
    }

    public void logoutUser(UserInfo userInfo) {
        if (mConnectBinder != null) {
            mConnectBinder.logoutUser(userInfo);
        }
    }

    public void loginUser(UserInfo userInfo) {
        if (mConnectBinder != null) {
            mConnectBinder.loginUser(userInfo);
        }
    }

    public void logoutDevice() {
        if (mConnectBinder != null) {
            mConnectBinder.close();
        }
    }

    public void stopConnect() {
        if (mConnectBinder != null) {
            mConnectBinder.stopConnect();
        }
    }

    public boolean isConnected() {
        return mConnectBinder != null && mConnectBinder.isConnected();
    }

    public void reConnect() {
        if (mConnectBinder != null) {
            mConnectBinder.reConnect();
        }
    }
}
