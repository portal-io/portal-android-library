package com.whaley.core.share;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.share.model.ShareParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: qxw
 * Date: 2016/11/25
 */

public class ShareUtil {
    static ShareManager shareManager;
    private static int shareDefaultImg;
    static Map<Integer, Map<Integer, ShareParam>> shareParamsData = new HashMap<>();

    //微信 appid appsecret
    public static void setWeixin(String WX_APP_ID, String WX_APP_KEY) {
        PlatformConfig.setWeixin(WX_APP_ID, WX_APP_KEY);
    }

    //新浪微博 appkey appsecret
    public static void setSinaWeibo(String WB_APP_ID, String WB_APP_KEY, String backUrl) {
        PlatformConfig.setSinaWeibo(WB_APP_ID, WB_APP_KEY, backUrl);
    }

    // QQ和Qzone appid appkey
    public static void setQQ(String QQ_APP_ID, String QQ_APP_KEY) {
        PlatformConfig.setQQZone(QQ_APP_ID, QQ_APP_KEY);
    }


    public static void share(ShareParam shareParam) {
        if (shareManager == null) {
            shareManager = new UMShareManager();
        }
        shareManager.share(shareParam);
    }

    public static void setShareDefaultImg(int shareImg) {
        shareDefaultImg = shareImg;
    }

    public static int getShareDefaultImg() {
        return shareDefaultImg;
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(getContext()).onActivityResult(requestCode, resultCode, data);
        if (shareManager != null) {
            shareManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static void putShareParams(int id, Map<Integer, ShareParam> shareParams) {
        shareParamsData.put(id, shareParams);
    }

    public static void removeShareParams(int id) {
        shareParamsData.remove(id);
    }


    public static Map<Integer, ShareParam> getShareParams(int id) {
        return shareParamsData.get(id);
    }


    /**
     * @author qxw
     * 判断微信是否安装
     * @time 2016/11/24 16:43
     */

    public static boolean isInstallWeixin(Activity activity) {
        if (!UMShareAPI.get(getContext()).isInstall(activity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
            Toast.makeText(getContext(), getContext().getString(R.string.share_no_weixin), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    /**
     * @author qxw
     * 判断qq是否安装
     * @time 2016/11/24 16:45
     */
    public static boolean isInstallQQ(Activity activity) {
        if (!UMShareAPI.get(getContext()).isInstall(activity, SHARE_MEDIA.QQ)) {
            Toast.makeText(getContext(), getContext().getString(R.string.share_no_qq), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static Context getContext() {
        return AppContextProvider.getInstance().getContext();
    }
}
