package com.whaley.core.share;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.image.OnLoadFailCallback;
import com.whaley.core.image.OnResourceLoadCallback;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.NetworkUtils;
import com.whaley.core.utils.StrUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Author: qxw
 * Date: 2016/11/21
 */

public class UMShareManager implements ShareManager, ShareConstants {

    static final ConcurrentMap<Integer, ShareListener> listenerMap = new ConcurrentHashMap<>();

    private boolean isShare;

    private Context getContext() {
        return AppContextProvider.getInstance().getContext();
    }

    @Override
    public void share(ShareParam shareParam) {
        if (shareParam == null) {
            return;
        }
        if (shareParam.getType() == TYPE_ALL) {
            Activity context = shareParam.getContext();
            shareParam.setContext(null);
            ShareListener shareListener = shareParam.getListener();
            if (shareListener != null) {
                listenerMap.put(shareParam.getCallbackId(), shareListener);
                shareParam.setListener(null);
            }
            if (shareParam.getShareParams() != null) {
                ShareUtil.putShareParams(shareParam.getCallbackId(), shareParam.getShareParams());
                shareParam.setShareParams(null);
            }
            if (shareParam.getCustomizePanelCallback() != null) {
                shareParam.getCustomizePanelCallback().panelCallback(context, shareParam);
                return;
            }
            if (ShareParam.defaultPanelCallback != null) {
                ShareParam.defaultPanelCallback.panelCallback(context, shareParam);
            }
            return;
        }
        if (shareParam.getType() == TYPE_WEIXIN_CIRCLE || shareParam.getType() == TYPE_WEIXIN) {
            if (!ShareUtil.isInstallWeixin(shareParam.getContext()))
                return;
        }
        if (shareParam.getType() == TYPE_QQ || shareParam.getType() == TYPE_QZONE) {
            if (!ShareUtil.isInstallQQ(shareParam.getContext()))
                return;
            Activity context = shareParam.getContext();
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);
                return;
            }
        }
        isShare = false;
        switch (shareParam.getType()) {
            case TYPE_QQ:
                performShare(shareParam, SHARE_MEDIA.QQ);
                break;
            case TYPE_SINA:
                performShareSine(shareParam);
                break;
            case TYPE_WEIXIN:
                performShare(shareParam, SHARE_MEDIA.WEIXIN);
                break;
            case TYPE_WEIXIN_CIRCLE:
                performShare(shareParam, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case TYPE_QZONE:
                performShare(shareParam, SHARE_MEDIA.QZONE);
                break;
        }
    }


    /**
     * @param shareParam
     * @author qxw
     * @time 2016/11/24 19:36
     */
    private void performShare(ShareParam shareParam, SHARE_MEDIA platform) {
        final ShareAction shareAction = new ShareAction(shareParam.getContext());
        UMWeb web = new UMWeb(shareParam.getUrl());
        web.setTitle(shareParam.getTitle());
        web.setDescription(shareParam.getDes());
//        web.setThumb(new UMImage(getContext(), shareParam.getBitmap()));
        shareAction.withMedia(web);
        if (shareParam.getImageModel() == null) {
            if (ShareUtil.getShareDefaultImg() != 0) {
                web.setThumb(new UMImage(AppContextProvider.getInstance().getContext(), ShareUtil.getShareDefaultImg()));
                shareAction.withMedia(web);
                performShare(shareAction, platform, shareParam);
                return;
            }
            performShare(shareAction, platform, shareParam);
            return;
        }
        if (shareParam.getImageModel().isImgUrlData()) {
            shareImageUrl(shareAction, platform, shareParam, web);
        } else if (shareParam.getImageModel().isResData()) {
            web.setThumb(new UMImage(AppContextProvider.getInstance().getContext(), shareParam.getImageModel().getResId()));
            shareAction.withMedia(web);
            performShare(shareAction, platform, shareParam);
        } else {
            web.setThumb(new UMImage(AppContextProvider.getInstance().getContext(), shareParam.getImageModel().getBitmap()));
            shareAction.withMedia(web);
            performShare(shareAction, platform, shareParam);
        }
//        performShare(shareAction, platform, shareParam);
//        if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
//            web.setTitle(getWeixinCircleTitle(shareParam));
//        } else {
//            web.setTitle(getTitle(shareParam));
//        }
//        String description = getText(shareParam);
//        if (StrUtil.isEmpty(description)) {
//            description = "众多人气综艺、体育内容每周更新VR版，只在微鲸VR ~";
//        }
//        web.setDescription(description);
//        if (shareParam.getImageModel().isImgUrlData()) {
//            shareImageUrl(shareAction, platform, shareParam, web);
//        } else if (shareParam.getImageModel().isResData()) {
//            web.setThumb(new UMImage(AppContextProvider.getInstance().getContext(), shareParam.getImageModel().getResId()));
//            shareAction.withMedia(web);
//            performShare(shareAction, platform, shareParam);
//        } else {
//            web.setThumb(new UMImage(AppContextProvider.getInstance().getContext(), shareParam.getImageModel().getBitmap()));
//            shareAction.withMedia(web);
//            performShare(shareAction, platform, shareParam);
//        }
    }


    /**
     * 新浪分享
     *
     * @param shareParam
     * @author qxw
     * @time 2016/11/24 19:36
     */
    public void performShareSine(final ShareParam shareParam) {
        final ShareAction shareAction = new ShareAction(shareParam.getContext());
        shareAction.withText(shareParam.getDes());
        if (shareParam.getImageModel() == null) {
            performShare(shareAction, SHARE_MEDIA.SINA, shareParam);
            return;
        }
        if (shareParam.getImageModel().isImgUrlData()) {
            if (StrUtil.isEmpty(shareParam.getImageModel().getImgUrl())) {//以防图片地址为空的情况
                performShare(shareAction, SHARE_MEDIA.SINA, shareParam);
                return;
            }
            imageLoader(shareParam, SHARE_MEDIA.SINA, shareAction, shareParam.getImageModel().getImgUrl(), 720);
        } else if (shareParam.getImageModel().isResData()) {
            shareAction.withMedia(new UMImage(AppContextProvider.getInstance().getContext(), shareParam.getImageModel().getResId()));
            performShare(shareAction, SHARE_MEDIA.SINA, shareParam);
        } else {
            shareAction.withMedia(new UMImage(AppContextProvider.getInstance().getContext(), shareParam.getImageModel().getBitmap()));
            performShare(shareAction, SHARE_MEDIA.SINA, shareParam);
        }
    }


    /**
     * @param shareAction
     * @param platform
     * @param shareParam
     * @author qxw
     * @time 2016/11/24 19:35
     */
    private void performShare(ShareAction shareAction, final SHARE_MEDIA platform, final ShareParam shareParam) {
        final ShareListener shareListener = shareParam.getListener();
        shareAction.setPlatform(platform).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.onResult(shareParam.getType());
                }
                if (SHARE_MEDIA.SINA.equals(platform)) {
                    UMShareAPI.get(getContext()).deleteOauth(shareParam.getContext(), platform, null);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (shareListener != null) {
                    shareListener.onError(shareParam.getType(), throwable);
                }
                if (SHARE_MEDIA.SINA.equals(platform)) {
                    UMShareAPI.get(getContext()).deleteOauth(shareParam.getContext(), platform, null);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.onCancel(shareParam.getType());
                }
                if (SHARE_MEDIA.SINA.equals(platform)) {
                    UMShareAPI.get(getContext()).deleteOauth(shareParam.getContext(), platform, null);
                }
            }
        }).share();
    }

    /**
     * url分享友盟压缩过慢
     *
     * @param shareAction
     * @param platform
     * @param shareParam
     * @param web
     */
    private void shareImageUrl(final ShareAction shareAction, final SHARE_MEDIA platform, final ShareParam shareParam, final UMWeb web) {
        if (StrUtil.isEmpty(shareParam.getImageModel().getImgUrl())) {
            performShare(shareAction, platform, shareParam);
            return;
        }
        imageLoader(shareParam, platform, shareAction, shareParam.getImageModel().getImgUrl(), 100, web);
//        ImageLoader.with(shareParam.getContext()).load(shareParam.getImageModel().getImgUrl()).size(100, 100).diskCacheStrategy(ImageRequest.DISK_NULL).onLoadFail(new OnLoadFailCallback() {
//            @Override
//            public void onLoadFail(Throwable throwable) {
//                if (NetworkUtils.isNetworkAvailable()) {
//                    web.setThumb(new UMImage(AppContextProvider.getInstance().getContext(), shareParam.getImageModel().getImgUrl()));
//                    shareAction.withMedia(web);
//                    performShare(shareAction, platform, shareParam);
//                } else {
//                    Toast.makeText(AppContextProvider.getInstance().getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).get(new OnResourceLoadCallback<Bitmap>() {
//            @Override
//            public void onResourceLoaded(Bitmap bitmap) {
//                web.setThumb(new UMImage(getContext(), bitmap));
//                shareAction.withMedia(web);
//                performShare(shareAction, platform, shareParam);
//            }
//        });
    }

    private void imageLoader(final ShareParam shareParam, SHARE_MEDIA platform, final ShareAction shareAction, final String url, final int size) {
        imageLoader(shareParam, platform, shareAction, url, size, null);
    }

    private void imageLoader(final ShareParam shareParam, final SHARE_MEDIA platform, final ShareAction shareAction, final String url, final int size, final UMWeb web) {
        ImageLoader.with(shareParam.getContext()).load(url).size(size, size).diskCacheStrategy(ImageRequest.DISK_NULL)
                .onLoadFail(new OnLoadFailCallback() {
                    @Override
                    public void onLoadFail(Throwable throwable) {
                        if (isShare) {
                            return;
                        }
                        if (NetworkUtils.isNetworkAvailable()) {
                            String[] str = url.split("zoom/");
                            if (str.length > 1) {
                                imageLoader(shareParam, platform, shareAction, str[0], size, web);
                            } else {
                                Toast.makeText(AppContextProvider.getInstance().getContext(), "分享失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AppContextProvider.getInstance().getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .get(new OnResourceLoadCallback<Bitmap>() {
                    @Override
                    public void onResourceLoaded(Bitmap bitmap) {
                        if (web != null) {
                            web.setThumb(new UMImage(AppContextProvider.getInstance().getContext(), bitmap));
                            shareAction.withMedia(web);
                        } else {
                            shareAction.withMedia(new UMImage(AppContextProvider.getInstance().getContext(), bitmap));
                        }
                        performShare(shareAction, platform, shareParam);
                        isShare = true;
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10086) {
            ShareListener shareListener = listenerMap.get(data.getIntExtra("CallbackId", -1));
            if (resultCode == Activity.RESULT_OK) {
                if (shareListener != null) {
                    shareListener.onResult(data.getIntExtra("type", -1));
                }
            }
            listenerMap.remove(data.getIntExtra("CallbackId", -1));
        }
    }

}
