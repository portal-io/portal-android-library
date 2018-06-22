package com.whaley.core.bi.model;

import com.google.gson.reflect.TypeToken;
import com.whaley.core.utils.GsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: qxw
 * Date: 2016/11/16
 */

public class LogInfoParam {
    final String logId;
    final String happenTime;
    final String logVersion;
    final String currentPageId;
    final Map<String, Object> currentPageProp;
    final String eventId;
    final Map<String, Object> eventProp;
    final String nextPageId;
    final String isTest;

    public static Builder createBuilder() {
        return new Builder();
    }

    public LogInfoParam(Builder builder) {
        this.logId = builder.logId;
        this.happenTime = builder.happenTime;
        this.logVersion = BIConstants.LOG_VERSION;
        this.currentPageId = builder.currentPageId;
        this.currentPageProp = builder.currentPageProp;
        this.eventId = builder.eventId;
        this.eventProp = builder.eventProp;
        this.nextPageId = builder.nextPageId;
        this.isTest = builder.isTest;
    }

    public String getLogVersion() {
        return logVersion;
    }

    public String getLogId() {
        return logId;
    }

    public String getHappenTime() {
        return happenTime;
    }

    public String getCurrentPageId() {
        return currentPageId;
    }

    public String getEventId() {
        return eventId;
    }

    public Map<String, Object> getEventProp() {
        return eventProp;
    }

    public Map<String, Object> getCurrentPageProp() {
        return currentPageProp;
    }

    public String getNextPageId() {
        return nextPageId;
    }

    public static class Builder {
        String isTest = "0";
        String logId;
        String happenTime;
        String currentPageId;
        Map<String, Object> currentPageProp;
        String eventId;
        Map<String, Object> eventProp;
        String nextPageId;


        public String getCurrentPageId() {
            return currentPageId;
        }

        public Builder setTest(String isTest) {
            this.isTest = isTest;
            return this;
        }

        public Builder setLogId(String logId) {
            this.logId = logId;
            return this;
        }

        public Builder setHappenTime(String happenTime) {
            this.happenTime = happenTime;
            return this;
        }
//        public Builder setBufferingTime(long bufferingTime) {
//            this.bufferingTime = bufferingTime;
//            return this;
//        }
//
//        public Builder setBufferingCount(int bufferingCount) {
//            this.bufferingCount = bufferingCount;
//            return this;
//        }

        public Builder putCurrentPagePropKeyValue(String key, Object value) {
            checkCurrentPageProp();
            currentPageProp.put(key, value);
            return this;
        }

        public Builder setCurrentPageProp(Object object) {
            String json = GsonUtil.getGson().toJson(object);
            Map<String, Object> map = GsonUtil.getGson().fromJson(json, new TypeToken<Map<String, Object>>() {
            }.getType());
            currentPageProp = map;
            return this;
        }

        private Builder checkCurrentPageProp() {
            if (currentPageProp == null)
                currentPageProp = new HashMap<>();
            return this;
        }

        public Builder setCurrentPageId(String currentPageId) {
            this.currentPageId = currentPageId;
            return this;
        }

        public Builder setEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }


        public Builder putEventPropKeyValue(String key, Object value) {
            checkEventProp();
            eventProp.put(key, value);
            return this;
        }

        public Builder setEventProp(Object object) {
            String json = GsonUtil.getGson().toJson(object);
            Map<String, Object> map = GsonUtil.getGson().fromJson(json, new TypeToken<Map<String, Object>>() {
            }.getType());
            eventProp = map;
            return this;
        }

        private Builder checkEventProp() {
            if (eventProp == null) {
                eventProp = new HashMap<>();
            }
            return this;
        }

        public Builder setNextPageId(String nextPageId) {
            this.nextPageId = nextPageId;
            return this;
        }

        public LogInfoParam build() {
            return new LogInfoParam(this);
        }
    }
}
