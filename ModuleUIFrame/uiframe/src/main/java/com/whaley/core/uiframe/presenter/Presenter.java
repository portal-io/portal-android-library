package com.whaley.core.uiframe.presenter;

import com.whaley.core.uiframe.view.UIView;

/**
 * Created by YangZhi on 2017/7/10 15:49.
 */

public interface Presenter<UIVIEW extends UIView> {
    UIVIEW getUIView();
}
