package com.whaley.core.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;


import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.AppFileStorage;
import com.whaley.core.debug.logger.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;
import java.util.UUID;

/**
 * Created by YangZhi on 2016/9/6 14:02.
 */
public class AppUtil {

    private static String deviceID;
    private static final String PRE_DEVICE_ID = "pre_device_id";
    private static String filePath = File.separator + ".deviceID";

    public static String getDeviceModel() {
        return android.os.Build.MANUFACTURER;
    }

    public static String getVersionRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getDeviceId() {
        if (!StrUtil.isEmpty(deviceID)) {
            return deviceID;
        }
        deviceID = getSharedDeviceID();
        if (!StrUtil.isEmpty(deviceID)) {
            return deviceID;
        }
        deviceID = getFileDeviceID();
        if (!StrUtil.isEmpty(deviceID)) {
            return deviceID;
        }
        saveDeviceID(getUUID());
        return deviceID;
    }


    public static String getUniquePsuedoID() {
        String serial = getSerial();
        if (StrUtil.isEmpty(serial) || "Unknown".equals(serial)) {
            return "";
        }
        return new UUID(getDevIDShort().hashCode(), serial.hashCode()).toString();
    }


    public static String getMackUUID() {
        String androidID;
        String uuuid = null;
        if (StrUtil.isEmpty(uuuid)) {
            androidID = getAndroidID();
            try {
                if (!"02:00:00:00:00:00".equals(androidID)) {
                    uuuid = UUID.nameUUIDFromBytes(androidID.getBytes("utf-8")).toString();
                } else {
                    uuuid = UUID.randomUUID().toString();
                }
            } catch (Exception e) {
                uuuid = UUID.randomUUID().toString();
            }
        }
        return uuuid;
    }

    public static String getAndroidID() {
        return Settings.Secure.getString(AppContextProvider.getInstance().getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDevIDShort() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
        return m_szDevIDShort;
    }

    public static String getSerial() {
        return Build.SERIAL;
    }

    public static String getUniversalID() {
        String androidID;
        String uuuid = null;
        if (StrUtil.isEmpty(uuuid)) {
            androidID = getAndroidID();
            try {
                if (!StrUtil.isEmpty(androidID) && !"9774d56d682e549c".equals(androidID)) {
                    uuuid = UUID.nameUUIDFromBytes(androidID.getBytes("utf-8")).toString();
                }
            } catch (Exception e) {
            }
        }
        return uuuid;
    }


    public static String getDeviceToken() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) AppContextProvider.getInstance().getContext().getSystemService(Application.TELEPHONY_SERVICE);
            String deviceId = telephonyManager.getDeviceId();
            return deviceId == null ? "" : deviceId;
        } catch (Exception e) {
            Log.e(e, "getDeviceToken");
        }
        return "";
    }


    public static String getMacAddress() {
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:00";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:00";
        }
        return macAddress;
    }

    /**
     * Checks if the application is being sent in the background (i.e behind
     * another application's Activity).
     *
     * @param context the context
     * @return <code>true</code> if another application will be above this one.
     */
    public static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                Log.d("Background App:");
                return true;


            } else {
                Log.d("Foreground App:");
            }
        }

        return false;
    }


    private static String getUUID() {
        String uuid = null;
        uuid = getUniversalID();
        if (!StrUtil.isEmpty(uuid)) {
            return uuid;
        }
        uuid = getUniquePsuedoID();
        if (!StrUtil.isEmpty(uuid)) {
            return uuid;
        }
        uuid = getMackUUID();
        return uuid;
    }

    private static void saveDeviceID(String uuid) {
        deviceID = MD5Util.getMD5String(uuid);
        saveSharedDeviceID(deviceID);
        saveFileDeviceID(deviceID);
    }

    private static String getFileDeviceID() {
        try {
            return FileUtils.readFile(getPath() + filePath, "UTF-8").toString();
        } catch (Exception e) {
            return null;
        }

    }

    private static String getPath() {
        //首先判断是否有外部存储卡，如没有判断是否有内部存储卡，如没有，继续读取应用程序所在存储
        String phonePicsPath = AppFileStorage.getAppDirPath();
        if (phonePicsPath == null) {
            phonePicsPath = getContext().getFilesDir().getAbsolutePath();
        }
        return phonePicsPath;
    }

    private static void saveFileDeviceID(String deviceID) {
        try {
            String ExternalSdCardPath = AppFileStorage.getAppDirPath() + filePath;
            FileUtils.writeFile(ExternalSdCardPath, deviceID);
        } catch (Exception e) {
        }
        try {
            String InnerPath = getContext().getFilesDir().getAbsolutePath() + filePath;
            FileUtils.writeFile(InnerPath, deviceID);
        } catch (Exception e) {

        }

    }

    private static void saveSharedDeviceID(String deviceID) {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_DEVICE_ID,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PRE_DEVICE_ID, deviceID);
        editor.commit();
    }

    private static String getSharedDeviceID() {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_DEVICE_ID,
                Context.MODE_PRIVATE);
        return preferences.getString(PRE_DEVICE_ID, "");
    }


    private static Context getContext() {
        return AppContextProvider.getInstance().getContext();
    }
}
