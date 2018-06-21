package com.whaley.core.widget.pullable;

import android.view.View;

/**
 * Created by yangzhi on 17/1/11.
 */

public interface PullBehavior {

    void onOverPullDown(int overPullHeight);

    void onPull(int transHeight);

    View getHeaderView();

    int maxHeadPullHeight();

    boolean isShouldAutoOffsetHeader();

    void startFling();

}
