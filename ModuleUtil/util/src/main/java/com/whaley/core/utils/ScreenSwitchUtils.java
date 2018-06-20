package com.whaley.core.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.OrientationEventListener;

public class ScreenSwitchUtils {

    // 是否是竖屏
    public boolean isPortrait = true;
    public OnSensorListener onSensorListener;
    private OrientationSensorListener listener;
    private boolean isEnable = false;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 888:
                    int i = msg.arg1;
                    if((75 <= i && i <= 105) || (255 <= i && i <= 285)) {
                        if (isPortrait) {
                            isPortrait = false;
                            if (onSensorListener != null) {
                                onSensorListener.onSensorHorizontal();
                            }
                        }
                    } else if((165 <= i && i <= 195) || 345 <= i || (0 <= i && i <= 15)){
                        if (!isPortrait) {
                            isPortrait = true;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 开始监听
     */
    public void start() {
        if(!isEnable) {
            isEnable = true;
            listener.enable();
        }
    }

    /**
     * 停止监听
     */
    public void stop() {
        if(isEnable) {
            isEnable = false;
            listener.disable();
        }
    }

    public ScreenSwitchUtils(Context context) {
        listener = new OrientationSensorListener(context);
        listener.init(mHandler);
    }

    /**
     * 重力感应监听者
     */
    public class OrientationSensorListener extends OrientationEventListener {

        private Handler rotateHandler;

        public OrientationSensorListener(Context context) {
            super(context);
        }

        public void init(Handler handler) {
            rotateHandler = handler;
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (rotateHandler != null) {
                rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
            }
        }
    }

    public void setOnSensorListener(OnSensorListener onSensorListener) {
        this.onSensorListener = onSensorListener;
    }

    public interface OnSensorListener {
        void onSensorHorizontal();
    }

}
