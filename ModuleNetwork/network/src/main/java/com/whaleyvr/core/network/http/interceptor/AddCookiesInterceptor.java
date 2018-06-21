package com.whaleyvr.core.network.http.interceptor;

import com.whaleyvr.core.network.http.CookieManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String host = chain.request().url().url().getHost();
        Map<String,HashMap<String,String>> cookie = CookieManager.getInstance().getCookieMap();
        if (cookie != null) {
            Map<String, String> cookieMap = cookie.get(host);
            if (cookieMap != null) {
                StringBuilder sb = new StringBuilder();
                for (String key : cookieMap.keySet()) {
                    sb.append(key);
                    sb.append("=");
                    sb.append(cookieMap.get(key));
                    sb.append("; ");
                }
                builder.addHeader("Cookie", sb.toString());
            }
        }
        return chain.proceed(builder.build());
    }

}