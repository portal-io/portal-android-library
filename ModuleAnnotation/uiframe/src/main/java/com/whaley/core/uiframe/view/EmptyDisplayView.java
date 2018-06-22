package com.whaley.core.uiframe.view;


/**
 * Created by yangzhi on 16/8/8.
 */
public interface EmptyDisplayView {

    void showEmpty();

    void showLoading(String loadtext);

    void showError(Throwable error);

    void showContent();

    void setOnRetryListener(OnRetryListener listener);


    interface OnRetryListener {
        void onRetry();
    }

}
