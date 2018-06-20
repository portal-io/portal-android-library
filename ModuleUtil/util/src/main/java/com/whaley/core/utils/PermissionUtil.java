package com.whaley.core.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.whaley.core.appcontext.AppContextProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by YangZhi on 2017/2/6 17:57.
 */

public class PermissionUtil {
    public static boolean checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(AppContextProvider.getInstance().getContext(),
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static boolean requestPermission(Activity activity, String[] permissions, int requestCode) {
        List<String> permissionList = null;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(AppContextProvider.getInstance().getContext(),
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {
                if(permissionList==null){
                    permissionList =  new ArrayList<>();
                }
                permissionList.add(permission);
            }
        }
        if(permissionList!=null&&permissionList.size()>0) {
            String[] strings = new String[permissionList.size()];
            ActivityCompat.requestPermissions(activity, permissionList.toArray(strings),
                    requestCode);
            return true;
        }
        return false;
    }

    public static boolean requestPermission(Activity activity, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(AppContextProvider.getInstance().getContext(),
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
            return true;
        }
        return false;
    }
}
