package com.whaley.core.bi.model;

/**
 * Author: qxw
 * Date: 2016/11/18
 */

public class CurrentPagePropModel {
    private String playMethod;
    private String playClarity;
    private boolean isFullScreen;

    public String getPlayMethod() {
        return playMethod;
    }

    public void setPlayMethod(String playMethod) {
        this.playMethod = playMethod;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    public String getPlayClarity() {
        return playClarity;
    }

    public void setPlayClarity(String playClarity) {
        this.playClarity = playClarity;
    }
}
