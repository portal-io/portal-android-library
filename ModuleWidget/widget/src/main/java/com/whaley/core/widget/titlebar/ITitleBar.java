package com.whaley.core.widget.titlebar;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by YangZhi on 2017/7/24 13:06.
 */

public interface ITitleBar {
    void setTitleText(CharSequence titleText);

    void setCenterView(View view);

    void setRightIcon(@DrawableRes int resId);

    void setRightText(CharSequence rightText);

    void setRightView(View view);

    void setLeftIcon(@DrawableRes int resId);

    void setLeftText(CharSequence leftText);

    void setLeftView(View view);

    void setBackgroundColor(@ColorInt int color);

    void setBackgroundResource(@DrawableRes int resId);

    void setBackgroundDrawable(Drawable drawable);

    void setContainerHeight(int height);

    void setTitleBarListener(TitleBarListener listener);

    View getContainer();

    View getLeftView();

    View getRightView();

    View getCenterView();

    void setBottomLineColor(@ColorInt int color);

    void setBottomLineResource(@ColorRes int resId);


    void hideBottomLine();

    void showBottomLine();

    void setBottomLineHeight(int height);

    void setPaddingStatus(boolean paddingStatus);

    String getTitleText();

    String getLeftText();

    String getRightText();

    void setRightViewVisibility(int visibility);

    void setLeftViewVisibility(int visibility);

    void setVisibility(int visibility);
}
