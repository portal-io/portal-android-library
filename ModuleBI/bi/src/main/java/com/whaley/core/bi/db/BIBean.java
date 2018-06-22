package com.whaley.core.bi.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Author: qxw
 * Date: 2016/2/16
 */
@Entity
public class BIBean {
    @Id
    public String itemid;
    public String metadata;
    public String logInfo;
    public String getLogInfo() {
        return this.logInfo;
    }
    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }
    public String getMetadata() {
        return this.metadata;
    }
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
    public String getItemid() {
        return this.itemid;
    }
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }
    @Generated(hash = 1843087892)
    public BIBean(String itemid, String metadata, String logInfo) {
        this.itemid = itemid;
        this.metadata = metadata;
        this.logInfo = logInfo;
    }
    @Generated(hash = 1752432983)
    public BIBean() {
    }
}
