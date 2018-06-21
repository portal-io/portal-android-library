package com.whaleyvr.core.network.longconnection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.mw.persistent.connect.PersistentConnect;
import com.mw.persistent.connect.header.MsgHeader;
import com.mw.persistent.connect.info.UserInfo;

/**
 * Created by mafei on 2017/3/16.
 */

public class ConnectService extends Service {
    private PersistentConnect mPersistentConnect;
    private ConnectionListener mConnectionListener;

    private ConnectBinder mBinder = new ConnectBinder();

    public ConnectService() {
    }

    public static void start(Context context) {
        context.startService(new Intent(context, ConnectService.class));
    }

    @Override public void onCreate() {
        super.onCreate();
        init();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void init() {
        mPersistentConnect = new PersistentConnectImpl();
    }

    public class PersistentConnectImpl extends PersistentConnect {

        @Override public void onReceive(String s) {
            mConnectionListener.onReceive(s);
        }

        @Override public void onDeviceLogin(String s) {
            mConnectionListener.onDeviceLogin(s);
        }

        @Override public void onUserLogin(UserInfo userInfo) {
            mConnectionListener.onUserLogin(userInfo);
        }

        @Override public void onUserLogout(UserInfo userInfo) {
            mConnectionListener.onUserLogout(userInfo);
        }

        @Override public void unAvailable(int i, String s) {
            mConnectionListener.unAvailable(i, s);
        }

        @Override public void available() {
            mConnectionListener.available();
        }
    }

    public class ConnectBinder extends Binder {
        public void connect(final Context context, final String deviceId,
                ConnectionListener listener) {
            if (mPersistentConnect == null) {
                mPersistentConnect = new PersistentConnectImpl();
            }
            mConnectionListener = listener;
            mPersistentConnect.startConnect(context, deviceId);
        }

        public void connect(final Context context, final String deviceId) {
            if (mPersistentConnect == null) {
                mPersistentConnect = new PersistentConnectImpl();
            }
            mPersistentConnect.startConnect(context, deviceId);
        }

        public void send(final MsgHeader msgHeader) {
            if (mPersistentConnect != null) {
                mPersistentConnect.send(msgHeader);
            }
        }

        public void setTcpListener(ConnectionListener listener) {
            mConnectionListener = listener;
        }

        public void close() {
            if (mPersistentConnect != null) {
                mPersistentConnect.close();
            }
        }

        public void stopConnect() {
            if (mPersistentConnect != null) {
                mPersistentConnect.stopConnect();
            }
        }

        public boolean isConnected() {
            return mPersistentConnect != null && mPersistentConnect.isConnected();
        }

        public void reConnect() {
            if (mPersistentConnect != null) {
                mPersistentConnect.reConnect();
            }
        }

        public void loginUser(UserInfo userInfo) {
            if (mPersistentConnect != null) {
                mPersistentConnect.loginUser(userInfo);
            }
        }

        public void logoutUser(UserInfo userInfo) {
            if (mPersistentConnect != null) {
                mPersistentConnect.logoutUser(userInfo);
            }
        }
    }
}
