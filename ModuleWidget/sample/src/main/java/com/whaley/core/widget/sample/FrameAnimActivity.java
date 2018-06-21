package com.whaley.core.widget.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.whaley.core.widget.imageview.FrameAnimImageView;

/**
 * Created by YangZhi on 2017/8/3 15:15.
 */

public class FrameAnimActivity extends AppCompatActivity {

    public static final int[] LOGO_ANIM_RES_ARR = new int[]{
            R.mipmap.icon_logo_anim_1,
            R.mipmap.icon_logo_anim_2,
            R.mipmap.icon_logo_anim_3,
            R.mipmap.icon_logo_anim_4,
            R.mipmap.icon_logo_anim_5,
            R.mipmap.icon_logo_anim_6,
            R.mipmap.icon_logo_anim_7,
            R.mipmap.icon_logo_anim_8,
            R.mipmap.icon_logo_anim_9,
            R.mipmap.icon_logo_anim_10
    };

    FrameAnimImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frameanim);
        imageView = (FrameAnimImageView) findViewById(R.id.frameanimimageview);
        imageView.setImageRes(LOGO_ANIM_RES_ARR);
        imageView.setSwitchDuration(40);
        imageView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageView.stop();
    }
}
