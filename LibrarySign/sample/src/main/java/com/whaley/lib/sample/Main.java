package com.whaley.lib.sample;


import android.app.Activity;
import android.os.Bundle;

import com.whaley.lib.sign.Sign;

/**
 * Author: qxw
 * Date:2017/8/15
 * Introduction:
 */

public class Main extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String s = Sign.getShowSign(this);
    }
}
