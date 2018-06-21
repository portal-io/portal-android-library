package com.whaley.core.sample;

import android.app.Activity;
import android.app.Application;

import com.umeng.socialize.Config;
import com.whaley.core.appcontext.AppContextInit;
import com.whaley.core.share.SharePanelCallback;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;

/**
 * Author: qxw
 * Date: 2017/7/7
 */

public class SampleApplication extends Application {


    private final static String BUGLY_APP_ID = "318344312b";
    private final static String WX_APP_ID = "wx451cc87ab867c81c"; //"wx8d194f350eac172b"
    private final static String WX_APP_KEY = "a4fd98ac68407ef273695f6b5ef74969"; //"b141a2bba3905f9eecaa94adf78f526c"
    private final static String WB_APP_ID = "3689115682";
    private final static String WB_APP_KEY = "f7a77155f368b015cd7e75ebd6201ab4";
    private final static String QQ_APP_ID = "1105403128";
    private final static String QQ_APP_KEY = "6BVvOaFFhcKYGT8J";
    private final static String UMENG_APP_ID = "5768e8b767e58e9de40011e1";

    {
        Config.DEBUG = true;
        //微信 appid appsecret
        ShareUtil.setWeixin(WX_APP_ID, WX_APP_KEY);
        //新浪微博 appkey appsecret
        ShareUtil.setSinaWeibo(WB_APP_ID, WB_APP_KEY, "https://itunes.apple.com/us/app/vr-guan-jia/id963637613?l=zh&ls=1&mt=8");
        // QQ和Qzone appid appkey
        ShareUtil.setQQ(QQ_APP_ID, QQ_APP_KEY);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContextInit.appContextInit(getApplicationContext(), "ssss");
        ShareParam.defaultPanelCallback = new SharePanelCallback() {
            @Override
            public void panelCallback(Activity context, ShareParam shareParam) {
                ShareActivity.setShareParamsMap(shareParam.getShareParams());
                ShareActivity.launch(context);
            }
        };

    }
}
