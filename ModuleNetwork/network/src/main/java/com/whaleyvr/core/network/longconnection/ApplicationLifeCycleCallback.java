package com.whaleyvr.core.network.longconnection;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by mafei on 2017/3/16.
 */

public abstract class ApplicationLifeCycleCallback {

    private volatile int activityInstanceCount;
    private long foregroundTimestamp = -1L;

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks
            = new Application.ActivityLifecycleCallbacks() {
        @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (activityInstanceCount == 0) {
                onApplicationEnter();
            }
            activityInstanceCount++;
        }

        @Override public void onActivityStarted(Activity activity) {
            if (foregroundTimestamp < 0) {
                foregroundTimestamp = System.currentTimeMillis();
            }
        }

        @Override public final void onActivityResumed(final Activity activity) {
        }

        @Override public void onActivityPaused(Activity activity) {

        }

        @Override public final void onActivityStopped(Activity activity) {
        }

        @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override public void onActivityDestroyed(Activity activity) {
            activityInstanceCount--;
            if (activityInstanceCount == 0) {
                onApplicationExit(System.currentTimeMillis() - foregroundTimestamp);
                foregroundTimestamp = -1L;
            }
        }
    };

    public ApplicationLifeCycleCallback() {
    }

    public Application.ActivityLifecycleCallbacks build() {
        return activityLifecycleCallbacks;
    }

    protected void onApplicationExit(long duration) {

    }

    protected void onApplicationEnter() {

    }

    protected void onApplicationBroughtToBackground(Activity currentActivity,
            long foregroundTimeMillis) {

    }

    protected void onApplicationBroughtToForground(Activity currentActivity,
            long backgroundTimeMillis) {

    }
}