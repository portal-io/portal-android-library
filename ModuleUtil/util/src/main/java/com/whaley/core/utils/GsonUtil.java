package com.whaley.core.utils;


import com.google.gson.Gson;

/**
 * Created by YangZhi on 2016/9/3 15:15.
 */
public class GsonUtil {
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null)
            gson = new Gson();
        return gson;
    }
}
