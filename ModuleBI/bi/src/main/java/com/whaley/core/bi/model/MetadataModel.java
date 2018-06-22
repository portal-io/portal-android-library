package com.whaley.core.bi.model;

/**
 * Author: qxw
 * Date: 2016/11/2
 */

public class MetadataModel {
    //        "userId":"字符串",             // 用户的userId，唯一区别每个用户，eg：32位字符串
//            "accountId":"字符串",          // 用户注册的账号id，eg: 手机号等，根据实际情况
//            "apkVersion":"字符串",         // 微鲸VR的apk版本号，eg：2.3.0
//            "productModel":"字符串",       // 用户的终端型号
//            "romVersion":"字符串",         // 系统型号
//            "uploadTime":"长整型",         // 日志上传时间
//            "networkStatus":"字符串",      // 用户当前网络状态
//            "buildDate":"字符串"           // apk编译的日期
    private String sessionId;
    private String userId;
    private String isRandom;
    private String accountId;
    private String promotionChannel;//推广Apk的渠道标识

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    private String systemName = "Android";
    private String romVersion;
    private String productModel;
    private String buildDate;
    private String apkSeries;
    private String apkVersion;
    //    private String networkStatus;


    public MetadataModel() {
    }


    public MetadataModel(String sessionId, String promotionChannel, String apkVersion, String productModel, String romVersion, String buildDate) {
        this.sessionId = sessionId;
        this.promotionChannel = promotionChannel;
        this.romVersion = romVersion;
        this.productModel = productModel;
        this.buildDate = buildDate;
        this.apkSeries = buildDate;
        this.apkVersion = apkVersion;
//        this.networkStatus = networkStatus;
    }

    public String getApkSeries() {
        return apkSeries;
    }

    public void setApkSeries(String apkSeries) {
        this.apkSeries = apkSeries;
    }

    public String getPromotionChannel() {
        return promotionChannel;
    }

    public void setPromotionChannel(String promotionChannel) {
        this.promotionChannel = promotionChannel;
    }

    public String getIsRandom() {
        return isRandom;
    }

    public void setIsRandom(String isRandom) {
        this.isRandom = isRandom;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getRomVersion() {
        return romVersion;
    }

    public void setRomVersion(String romVersion) {
        this.romVersion = romVersion;
    }


    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }
}
