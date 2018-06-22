package com.whaley.core.bi.model;

/**
 * Author: qxw
 * Date: 2016/11/16
 */

public class EventPropModel {
    private String name;//事件内容名称
    private String code;//事件的code


    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
