package com.whaley.core.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.whaley.core.appcontext.AppContextProvider;

/**
 * 网络可用的判断和配置
 */
public class NetworkUtils {

    public final static int NONE = 0;
    public final static int WIFI = 1;
    public final static int MOBILE = 2;

    public static class NetworkChangedEvent {
        private final int currntNetWorkState;

        public NetworkChangedEvent(int currntNetWorkState) {
            this.currntNetWorkState = currntNetWorkState;
        }

        public int getCurrntNetWorkState() {
            return currntNetWorkState;
        }
    }

    private static BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mobNetInfo != null && wifiNetInfo != null &&
                    !mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                // 网络不可以用
//				EventController.postEvent(new NetworkChangedEvent(NONE));
            } else {
                if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
//					EventController.postEvent(new NetworkChangedEvent(WIFI));
                } else {
//					EventController.postEvent(new NetworkChangedEvent(MOBILE));
                }
            }
        }
    };

    private static boolean isRegistNetWorkChanged;

    public static void registNetWorkChanged() {
        if (!isRegistNetWorkChanged) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            AppContextProvider.getInstance().getContext().registerReceiver(broadcastReceiver, filter);
            isRegistNetWorkChanged = true;
        }
    }

    public static void unRegistNetWorkChanged() {
        if (isRegistNetWorkChanged) {
            AppContextProvider.getInstance().getContext().unregisterReceiver(broadcastReceiver);
            isRegistNetWorkChanged = false;
        }
    }

    /**
     * 获取当前网络状态(是否可用)
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        boolean netWorkStatus = false;
        ConnectivityManager connManager = (ConnectivityManager) AppContextProvider.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            netWorkStatus = networkInfo.isAvailable();
        }
        return netWorkStatus;
    }

    /**
     * 获取3G或者WIFI网络
     *
     * @return
     */
    public static int getNetworkState() {
        ConnectivityManager connManager = (ConnectivityManager) AppContextProvider.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        State state;
        NetworkInfo networkInfo;
        if (null != connManager) {
            //Wifi网络判断
            networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (null != networkInfo) {
                state = networkInfo.getState();
                if (state == State.CONNECTED || state == State.CONNECTING) {
                    return WIFI;
                }
            }

            //3G网络判断
            networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != networkInfo) {
                state = networkInfo.getState();
                if (state == State.CONNECTED || state == State.CONNECTING) {
                    return MOBILE;
                }
            }
        }
        return NONE;
    }

    public static boolean isWiFiActive() {
        if (WIFI == getNetworkState()) {
            return true;
        } else {
            return false;
        }
    }
}
