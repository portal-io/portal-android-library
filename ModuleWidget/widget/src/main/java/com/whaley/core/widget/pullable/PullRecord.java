package com.whaley.core.widget.pullable;

/**
 * Created by yangzhi on 17/1/12.
 */

public class PullRecord {

    private int headerHeight;

    private float lastY;
    private float lastX;

    private float initDownY;

    private float initDownX;

    private float initDragY;

    private boolean isBeingDragged;

    private boolean isBeingHorizontalDragged;

    private boolean isOnOverPull;

    private boolean isChildDown;

    private int overscrollTop;

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public float getLastX() {
        return lastX;
    }

    public void setInitDownY(float initDownY) {
        this.initDownY = initDownY;
    }

    public float getInitDownY() {
        return initDownY;
    }

    public void setInitDownX(float initDownX) {
        this.initDownX = initDownX;
    }

    public float getInitDownX() {
        return initDownX;
    }

    public void setInitDragY(float initDragY) {
        this.initDragY = initDragY;
    }

    public float getInitDragY() {
        return initDragY;
    }

    public void setBeingDragged(boolean beingDragged) {
        isBeingDragged = beingDragged;
    }

    public boolean isBeingDragged() {
        return isBeingDragged;
    }


    public void setBeingHorizontalDragged(boolean beingHorizontalDragged) {
        isBeingHorizontalDragged = beingHorizontalDragged;
    }

    public boolean isBeingHorizontalDragged() {
        return isBeingHorizontalDragged;
    }

    public boolean isOnOverPull() {
        return isOnOverPull;
    }

    public void setOnOverPull(boolean onOverPull) {
        isOnOverPull = onOverPull;
    }

    public void setChildDown(boolean childDown) {
        isChildDown = childDown;
    }

    public boolean isChildDown() {
        return isChildDown;
    }


    public void setOverscrollTop(int overscrollTop) {
        this.overscrollTop = overscrollTop;
    }

    public int getOverscrollTop() {
        return overscrollTop;
    }
}
