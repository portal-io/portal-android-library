package com.whaley.core.widget.titlebar;

import android.view.View;

public interface TitleBarListener {
    void onLeftClick(View view);

    void onTitleClick(View view);

    void onRightClick(View view);
}