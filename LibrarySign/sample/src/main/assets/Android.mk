LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := sign_whaleyvr-jni
LOCAL_SRC_FILES := sign_whaleyvr.cpp

include $(BUILD_SHARED_LIBRARY)