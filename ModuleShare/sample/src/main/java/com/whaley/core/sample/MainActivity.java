package com.whaley.core.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.image.OnLoadFailCallback;
import com.whaley.core.image.OnResourceLoadCallback;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.ShareListener;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;


public class MainActivity extends AppCompatActivity {

    String SHARE_TEST_URL = "http://vrh5.test.moguv.com/app-share-h5/";
    String SHARE_URL = true ? SHARE_TEST_URL : "http://vrh5.moguv.com/app-share-h5/";
    String SHARE_APP_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.whaley&ckey=CK1340670182054";
    String SHARE_TOPIC_URL = SHARE_URL + "index.html?code=%1$s";
    String SHARE_PACKAGE_URL = SHARE_URL + "itempack.html?code=%1$s";
    String SHARE_LIVE_DETAIL = SHARE_URL + "liveProgram.html?code=%1$s";
    String SHARE_DETAIL = SHARE_URL + "viewthread.html?code=%1$s";
    String SHARE_3D_DETAIL = SHARE_URL + "viewthread3D.html?code=%1$s";
    String SHARE_TV = SHARE_URL + "tv.html?code=%1$s";
    ShareParam.Builder shareParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareParam.setType(ShareConstants.TYPE_WEIXIN);
                ShareUtil.share(shareParam.build());
            }
        });
        findViewById(R.id.tv_pengyouquan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareParam.setType(ShareConstants.TYPE_WEIXIN_CIRCLE);
                ShareUtil.share(shareParam.build());
            }
        });
        findViewById(R.id.tv_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareParam.setType(ShareConstants.TYPE_QQ);
                ShareUtil.share(shareParam.build());
            }
        });
        findViewById(R.id.tv_kongjian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareParam.setType(ShareConstants.TYPE_QZONE);
                ShareUtil.share(shareParam.build());
            }
        });
        findViewById(R.id.tv_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareParam.setType(ShareConstants.TYPE_SINA);
                ShareUtil.share(shareParam.build());
            }
        });
        findViewById(R.id.tv_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareParam.setType(ShareConstants.TYPE_ALL);
                ShareUtil.share(shareParam.build());
            }
        });

        shareParam = ShareParam.createBuilder()
                .setTitle("标题")
                .setContext(this)
                .setDes("内容")
                .setUrl(SHARE_TV)
                .setShareType(1)
                .setImgModelBitmap("http://image.aginomoto.com/image/get-image/10000004/15099531093374801559.jpg/zoom/1080/608")
                .setShareListener(new ShareListener() {
                    @Override
                    public void onResult(int type) {

                    }

                    @Override
                    public void onError(int type, Throwable var2) {

                    }

                    @Override
                    public void onCancel(int type) {

                    }
                });
//        imageLoader("http://test-image.tvmore.com.cn/image/get-image/10000004/14979436721777492459.jpg/zoom/710/319", 100);

    }

//    private void imageLoader(final String url, final int size) {
//        ImageLoader.with(this).load(url).size(size, size).diskCacheStrategy(ImageRequest.DISK_NULL)
//                .onLoadFail(new OnLoadFailCallback() {
//                    @Override
//                    public void onLoadFail(Throwable throwable) {
//                        String[] str = url.split("zoom/");
//                        if (str.length > 1) {
//                            imageLoader(str[0], size);
//                        }
//                    }
//                })
//                .get(new OnResourceLoadCallback<Bitmap>() {
//                    @Override
//                    public void onResourceLoaded(Bitmap bitmap) {
//                        shareParam.setBitmap(bitmap);
//                    }
//                });
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareUtil.onActivityResult(requestCode, resultCode, data);
    }
}
