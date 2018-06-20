package com.whaley.core.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.whaley.core.utils.AppUtil;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView1 = (TextView) findViewById(R.id.test1);
        TextView textView2 = (TextView) findViewById(R.id.test2);
        TextView textView3 = (TextView) findViewById(R.id.test3);
        TextView textView4 = (TextView) findViewById(R.id.test4);
        TextView textView5 = (TextView) findViewById(R.id.test5);
        TextView textView6 = (TextView) findViewById(R.id.test6);
        TextView textView7 = (TextView) findViewById(R.id.test7);
        TextView textView8 = (TextView) findViewById(R.id.test8);
        TextView textView9 = (TextView) findViewById(R.id.test9);

        textView1.setText("ANDROID_ID=" + AppUtil.getAndroidID());
        textView2.setText("ANDROID_ID_UUID=" + AppUtil.getUniversalID());
        textView3.setText("SERIAL=" + AppUtil.getSerial());
        textView4.setText("DEVID=" + AppUtil.getDevIDShort());
        textView5.setText("UniquePsuedoID=" + AppUtil.getUniquePsuedoID());
        textView6.setText("MacAddress=" + AppUtil.getMacAddress());
        textView7.setText("MacAddressUUID=" + AppUtil.getMackUUID());
        textView8.setText("deviceID=" + AppUtil.getDeviceId());
    }
}
