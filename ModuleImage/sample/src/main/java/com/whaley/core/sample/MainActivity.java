package com.whaley.core.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

public class MainActivity extends AppCompatActivity {

    ImageRequest.RequestManager imageRequest;
    private static final String IMAGE_URL = "http://test-image.tvmore.com.cn/image/get-image/10000004/14950139881710293977.jpg/zoom/768/1152";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageRequest = ImageLoader.with(this);
        ImageView imageView1 = (ImageView) findViewById(R.id.iv_1);
        imageRequest.load(IMAGE_URL).centerCrop().small().circle().into(imageView1);
        ImageView imageView2 = (ImageView) findViewById(R.id.iv_2);
        imageRequest.load(IMAGE_URL).centerCrop().medium().into(imageView2);
    }
}
