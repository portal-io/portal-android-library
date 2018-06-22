package com.whaley.core.bi.model;

/**
 * Author: qxw
 * Date: 2017/2/16
 */

public class UserModel {
    String account_id;
    String usreId;
    String systemName = "Android";

    public UserModel() {

    }

    public UserModel(String account_id, String usreId, String systemName) {
        this.account_id = account_id;
        this.usreId = usreId;
        this.systemName = systemName;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getUsreId() {
        return usreId;
    }

    public void setUsreId(String usreId) {
        this.usreId = usreId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
