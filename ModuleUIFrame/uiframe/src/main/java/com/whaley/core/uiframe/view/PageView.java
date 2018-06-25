package com.whaley.core.uiframe.view;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.whaley.core.uiframe.presenter.Presenter;

/**
 * Created by YangZhi on 2017/7/10 15:58.
 */

public interface PageView extends ToastView, LoadingView, UIView{
    EmptyDisplayView getEmptyDisplayView();
}
