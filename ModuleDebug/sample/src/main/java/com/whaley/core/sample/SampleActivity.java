package com.whaley.core.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.whaley.core.debug.logger.Log;

/**
 * Created by YangZhi on 2017/7/5 18:21.
 */

public class SampleActivity extends Activity {

    public static final String TAG = "SampleActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        findViewById(R.id.btn_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new NullPointerException("特定 crash 的空指针");
            }
        });

        Log.i("打印 i");
        Log.i(TAG, "打印 i 带 TAG");

        Log.d("打印 d");
        Log.d(TAG, "打印 d 带 TAG");

        Log.v("打印 v");
        Log.v(TAG, "打印 v 带 TAG");

        Log.w("打印 w");
        Log.w(TAG, "打印 w 带 TAG");

        Log.e("打印 e");
        Log.e(TAG, "打印 e 带 TAG");
        Throwable throwable = new NullPointerException("空指针");
        Log.e(throwable, "打印 e 带 Error");
        Log.e(TAG, throwable, "打印 e 带 TAG , Error");

    }


}
