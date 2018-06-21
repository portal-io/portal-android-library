package com.whaley.core.image;

import android.view.View;

/**
 * Created by yangzhi on 16/8/3.
 */
public interface Animator {
    /**
     * Starts an animation on the given {@link View}.
     *
     * @param view The view to animate.
     */
    void animate(View view);
}
