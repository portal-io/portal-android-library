package com.whaley.core.appcontext;

import android.content.Context;
import android.content.Intent;

/**
 * Created by yangzhi on 16/8/15.
 */
public interface Starter {
    void startActivityForResult(Intent intent, int requestCode);

    void startActivity(Intent intent);

    Context getAttatchContext();


    void transitionAnim(int enterAnim, int outAnim);

    void finish();

}
