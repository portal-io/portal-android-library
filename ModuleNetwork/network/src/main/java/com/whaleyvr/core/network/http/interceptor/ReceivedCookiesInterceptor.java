package com.whaleyvr.core.network.http.interceptor;

import com.whaleyvr.core.network.http.CookieManager;

import java.io.IOException;

import okhttp3.Interceptor;

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        okhttp3.Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            String host = chain.request().url().url().getHost();
            for (String header : originalResponse.headers("Set-Cookie")) {
                String[] keyValues=header.split(";");
                for (String keyValue:keyValues){
                    String[] keyValueLast=keyValue.split("=");
                    if(keyValueLast.length>1) {
                        String key = keyValueLast[0];
                        String value = keyValueLast[1];
                        CookieManager.getInstance().putCookie(host,key,value);
                    }
                }
            }
            CookieManager.getInstance().saveCookiesMapToSp();
        }
        return originalResponse;
    }

}