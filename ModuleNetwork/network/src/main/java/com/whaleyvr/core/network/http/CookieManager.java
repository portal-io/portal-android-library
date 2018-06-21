package com.whaleyvr.core.network.http;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by YangZhi on 2016/9/7 16:44.
 */
public class CookieManager {

    static final String SP_COOKIE_MAP_JSON="sp_cookie_map_json";

    private Map<String,HashMap<String,String>> cookieMap;

    private SharedPreferences sp;

    private volatile static CookieManager instance;

    public static CookieManager getInstance(){
        if (instance==null){
            synchronized (CookieManager.class){
                if(instance==null){
                    instance=new CookieManager();
                }
            }
        }
        return instance;
    }

    private CookieManager(){
        sp= AppContextProvider.getInstance().getContext().getSharedPreferences("VR_WHALEY", Application.MODE_PRIVATE);
    }



    private void initCookieMap(){
        if(cookieMap==null){
            cookieMap=getCookiesMapFormSp();
            if(cookieMap==null)
                cookieMap=new HashMap<>();
        }
    }


    public void putCookie(String host, String key,String value){
        initCookieMap();
        HashMap<String,String> cookie = cookieMap.get(host);
        if(cookie == null){
            cookie = new HashMap<>();
        }
        cookie.put(key,value);
        cookieMap.put(host,cookie);
    }


    public void saveCookiesMapToSp(){
        if(cookieMap!=null) {
            String cookieJson = GsonUtil.getGson().toJson(cookieMap);
            sp.edit()
                    .putString(SP_COOKIE_MAP_JSON, cookieJson)
                    .apply();
        }
    }


    private Map<String,HashMap<String,String>> getCookiesMapFormSp(){
        String cookieJson=sp.getString(SP_COOKIE_MAP_JSON,null);
        if(!StrUtil.isEmpty(cookieJson)){
            HashMap<String, HashMap<String, String>> map = null;
            try {
                map = GsonUtil.getGson().fromJson(cookieJson, new TypeToken<HashMap<String, HashMap<String, String>>>() {}.getType());
            }catch (Exception e){
                //
            }
            if(map!=null){
                return map;
            }
        }
        return null;
    }


    public Map<String,HashMap<String,String>> getCookieMap() {
        if(cookieMap==null){
            cookieMap=getCookiesMapFormSp();
        }

        return cookieMap;
    }

    public void setCookie(HashSet<String> cookies){
        this.cookieMap=null;
        if(cookies==null) {
            sp.edit().remove(SP_COOKIE_MAP_JSON).apply();
        }
    }

    public void removeCookie(){
        this.cookieMap=null;
        sp.edit().remove(SP_COOKIE_MAP_JSON).apply();
    }




}
