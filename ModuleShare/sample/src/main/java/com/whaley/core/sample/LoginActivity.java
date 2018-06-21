package com.whaley.core.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whaley.core.appcontext.AppContextProvider;
import java.util.Map;

/**
 * Author: qxw
 * Date:2017/8/25
 * Introduction:
 */

public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        findViewById(R.id.rl_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        findViewById(R.id.qq_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlatformInfo(SHARE_MEDIA.QQ);
            }
        });

        findViewById(R.id.sina_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlatformInfo(SHARE_MEDIA.SINA);
            }
        });
        findViewById(R.id.weixin_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlatformInfo(SHARE_MEDIA.WEIXIN);
            }
        });
//        measure();
    }


//    private static SHARE_MEDIA getPlatform(String type) {
//        switch (type) {
//            case UserConstants.TYPE_AUTH_QQ:
//                return SHARE_MEDIA.QQ;
//            case UserConstants.TYPE_AUTH_WX:
//                return SHARE_MEDIA.WEIXIN;
//            default:
//                return SHARE_MEDIA.SINA;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(AppContextProvider.getInstance().getContext()).onActivityResult(requestCode, resultCode, data);
    }

    public void getPlatformInfo(SHARE_MEDIA type) {
        UMShareAPI.get(AppContextProvider.getInstance().getContext()).getPlatformInfo(this, type, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                if (oauthListener != null) {
//                    oauthListener.onOauthComplete(type, map);
//                }
                deleteOauth(share_media);

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//                if (oauthListener != null) {
//                    oauthListener.onOauthFaile(throwable);
//                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
//                if (oauthListener != null) {
//                    oauthListener.onOauthCancel();
//                }
            }
        });
    }

    private void deleteOauth(SHARE_MEDIA share_media) {
        UMShareAPI.get(AppContextProvider.getInstance().getContext()).deleteOauth(this, share_media, null);
    }

}
