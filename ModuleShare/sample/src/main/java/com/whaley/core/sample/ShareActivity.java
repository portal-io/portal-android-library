package com.whaley.core.sample;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.umeng.socialize.UMShareAPI;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.ShareListener;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;

import java.util.Map;

/**
 * Created by dell on 2016/8/5.
 */
public class ShareActivity extends Activity {


    private final static int ANIMATION_DURATION = 200;

    Intent resultIntent;

    private ShareParam shareParam;

    private boolean isHorizontal;

    private boolean isFullscreen;

    private static Map<Integer, ShareParam> shareParamsMap;

    public static void setShareParamsMap(Map<Integer, ShareParam> shareParamsMap) {
        ShareActivity.shareParamsMap = shareParamsMap;
    }

    public static void launch(Activity context, ShareParam data) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra("data", data);
        context.startActivityForResult(intent, 10086);
    }

    public static void launch(Activity context) {
        Intent intent = new Intent(context, ShareActivity.class);
        context.startActivityForResult(intent, 10086);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
//        shareParam = (ShareParam) getIntent().getSerializableExtra("data");
//        if (shareParam != null) {
//            isHorizontal = shareParam.isHorizontal();
//            if (isHorizontal) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            }
//            isFullscreen = shareParam.isFullscreen();
//            if (isFullscreen) {
//                WindowManager.LayoutParams params = getWindow().getAttributes();
//                params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                getWindow().setAttributes(params);
//            }
//        }
//        if (shareParam.isDifferentParams()) {
//            shareParamsMap = ShareUtil.getShareParams(shareParam.getCallbackId());
//            if (shareParamsMap == null)
//                finish();
//        }

        findViewById(R.id.rl_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        findViewById(R.id.qq_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(ShareConstants.TYPE_QQ);
            }
        });
        findViewById(R.id.qq_zone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(ShareConstants.TYPE_QZONE);
            }
        });
        findViewById(R.id.sina_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(ShareConstants.TYPE_SINA);
            }
        });
        findViewById(R.id.weixin_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(ShareConstants.TYPE_WEIXIN);
            }
        });
        findViewById(R.id.weixin_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(ShareConstants.TYPE_WEIXIN_CIRCLE);
            }
        });
//        measure();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    //
//    @Override
//    protected void setViews() {
//
////        shareParam.setListener(new ShareParam.ShareListener() {
////            @Override
////            public void onResult(int type) {
////                onFinishResult(type);
////            }
////
////            @Override
////            public void onError(int type, Throwable var2) {
////                finish();
////            }
////
////            @Override
////            public void onCancel(int type) {
////                finish();
////            }
////        });
//        weixinFriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                share(ShareConstants.TYPE_WEIXIN, shareParam.getFrom());
////                BILogServiceManager.getInstance().recordLog(getLogInfoBuilder().setEventId(BIConstants.CHILD_EXTEND_WECHAT));
//            }
//        });
//        weixinCircle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                share(ShareConstants.TYPE_WEIXIN_CIRCLE, shareParam.getFrom());
////                ShareUtil.share(getRealShareParam(ShareConstants.TYPE_WEIXIN_CIRCLE));
////                BILogServiceManager.getInstance().recordLog(getLogInfoBuilder().setEventId(BIConstants.CHILD_EXTEND_MOMENTS));
//            }
//        });
//        qqZone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                share(ShareConstants.TYPE_QZONE, shareParam.getFrom());
////                ShareUtil.share(getRealShareParam(ShareConstants.TYPE_QZONE));
////                BILogServiceManager.getInstance().recordLog(getLogInfoBuilder().setEventId(BIConstants.CHILD_EXTEND_QQ));
//            }
//        });
//        qqFriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                share(ShareConstants.TYPE_QQ, shareParam.getFrom());
////                ShareUtil.share(getRealShareParam(ShareConstants.TYPE_QQ));
////                BILogServiceManager.getInstance().recordLog(getLogInfoBuilder().setEventId(BIConstants.CHILD_EXTEND_QQ));
//            }
//        });
//        sinaWeibo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                share(ShareConstants.TYPE_SINA, shareParam.getFrom());
////                ShareUtil.share(getRealShareParam(ShareConstants.TYPE_SINA));
////                BILogServiceManager.getInstance().recordLog(getLogInfoBuilder().setEventId(BIConstants.CHILD_EXTEND_WEIBO));
//            }
//        });
//        shareCopy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                copy(shareParam.getFrom());
////                BILogServiceManager.getInstance().recordLog(getLogInfoBuilder().setEventId(BIConstants.CHILD_EXTEND_CHAIN));
//            }
//        });
//        backIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
////        measure();
//    }

    private void copy(String from) {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(shareParam.getUrl());
//        showToast(getString(R.string.copy_link));
        onFinishResult(7);
    }

    private void share(int type) {
        ShareUtil.share(getRealShareParam(type));
    }

    private void share(int type, String from) {
        ShareUtil.share(getRealShareParam(type));
    }


    private ShareParam getRealShareParam(int type) {
        ShareParam shareParam;
        if (shareParamsMap == null) {
            shareParam = this.shareParam;
            shareParam.setContext(this);
            shareParam.setType(type);
        } else {
            shareParam = shareParamsMap.get(type);
            shareParam.setContext(this);
        }
        shareParam.setListener(shareListener);
        return shareParam;
    }

    private void onFinishResult(int type) {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("CallbackId", shareParam.getCallbackId());
        setResult(RESULT_OK, intent);
        this.resultIntent = intent;
        finish();
    }

    private ShareListener shareListener = new ShareListener() {
        @Override
        public void onResult(int type) {
//            showToast("分享成功");
            onFinishResult(type);
        }

        @Override
        public void onError(int type, Throwable var2) {
//            showToast("分享失败");
            finish();
        }

        @Override
        public void onCancel(int type) {
//            showToast("分享取消");
        }
    };


//    int mesureHeight;
//
//    private void measure() {
//        ViewTreeObserver vto = rlShare.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                rlShare.getViewTreeObserver().removeOnGlobalLayoutListener(this);
////                recommend_layout_y = rlShare.getY();
//                mesureHeight = rlShare.getMeasuredHeight();
//                showRecommendIcon();
//            }
//        });
//    }

//    private void showRecommendIcon() {
//        ObjectAnimator anim = ObjectAnimator.ofFloat(rlShare, "translationY", mesureHeight, 0);
//        AnimatorSet set = new AnimatorSet();
//        set.play(anim);
//        set.setDuration(ANIMATION_DURATION);
//        set.setInterpolator(new AccelerateDecelerateInterpolator());
//        set.start();
//    }
//
//    private void hideRecommendIcon() {
//        ObjectAnimator anim = ObjectAnimator.ofFloat(rlShare, "translationY", 0, mesureHeight);
//        AnimatorSet set = new AnimatorSet();
//        set.play(anim);
//        set.setDuration(ANIMATION_DURATION);
//        set.setInterpolator(new AccelerateDecelerateInterpolator());
//        set.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
////                backIcon.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
////                if (null != recommendLayout) {
////                    recommendLayout.setVisibility(View.GONE);
////                }
//                finish();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        set.start();
//    }


//    @Override
//    protected void cancelToast() {
//    }

    @Override
    public void finish() {
        if (resultIntent == null) {
            Intent intent = new Intent();
            intent.putExtra("CallbackId", shareParam.getCallbackId());
            setResult(RESULT_CANCELED, intent);
        }
        if (shareParamsMap != null) {
            ShareUtil.removeShareParams(shareParam.getCallbackId());
        }
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareUtil.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public LogInfoParam.Builder getLogInfoBuilder() {
//        return LogInfoParam.createBuilder()
//                .setCurrentPageId(shareParam.getFrom())
//                .putEventPropKeyValue(BIConstants.EVENT_PROP_KEY_NAME, shareParam.getTitle())
//                .putEventPropKeyValue(BIConstants.EVENT_PROP_KEY_SHARE_URL, shareParam.getUrl());
//    }
}
