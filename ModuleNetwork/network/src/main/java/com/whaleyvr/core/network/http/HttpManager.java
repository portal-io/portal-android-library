package com.whaleyvr.core.network.http;

import android.content.Context;
import android.content.SharedPreferences;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.Debug;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.core.network.http.annotation.BaseUrl;
import com.whaleyvr.core.network.http.interceptor.AddCookiesInterceptor;
import com.whaleyvr.core.network.http.interceptor.ReceivedCookiesInterceptor;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by yangzhi on 16/8/5.
 */
public class HttpManager {

    private boolean isTest = false;

    private OkHttpClient client;

    private Retrofit.Builder builder;

    private OkHttpClient.Builder okHttpBuilder;

    private Map<String, Retrofit> retrofitMap = new HashMap<>();

    private String baseUrl = "http://storeapi-1.snailvr.com/";

    private TestUrlProvider testUrlProvider;

    private RequestCacheManager requestCacheManager;

    private static final int CONNECT_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;

    private final static String TEST_API = "test_api";

    private static void setTestApi(Context mContext, boolean status) {
        SharedPreferences preferences = mContext.getSharedPreferences(
                TEST_API, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(TEST_API, status);
        editor.commit();
    }

    private static boolean getTestApi(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(
                TEST_API, Context.MODE_PRIVATE);
        return preferences.getBoolean(TEST_API, false);
    }

    private static final class Singleton {
        public static final HttpManager INSTANCE = new HttpManager();
    }

    public boolean isTest() {
        return isTest;
    }

    public HttpManager setTest(boolean test) {
        isTest = test;
        setTestApi(AppContextProvider.getInstance().getContext(), isTest);
        return this;
    }

    private HttpManager() {
        initTestIfDebugMode();
    }

    public static HttpManager getInstance() {
        return Singleton.INSTANCE;
    }


    public HttpManager setTestUrlProvider(TestUrlProvider testUrlProvider) {
        this.testUrlProvider = testUrlProvider;
        return this;
    }


    public TestUrlProvider getTestUrlProvider() {
        return testUrlProvider;
    }

    public HttpManager setRequestCacheManager(RequestCacheManager requestCacheManager) {
        this.requestCacheManager = requestCacheManager;
        return this;
    }

    private void initTestIfDebugMode() {
        if (Debug.isDebug()) {
            isTest = getTestApi(AppContextProvider.getInstance().getContext());
        }
    }


    public OkHttpClient.Builder getOkHttpBuilder() {
        return okHttpBuilder;
    }

    public OkHttpClient getOkHttpClient() {
        return client;
    }


    public RequestCacheManager getRequestCacheManager() {
        return requestCacheManager;
    }

    public HttpManager setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }


    public HttpManager setBuilder(Retrofit.Builder builder) {
        this.builder = builder;
        return this;
    }

    public Retrofit getRetrofit(OkHttpClient client) {
        Retrofit retrofit = builder.baseUrl(baseUrl).client(client).build();
        return retrofit;
    }


    public Retrofit getRetrofit(Class apiClazz) {
        Annotation annotation = apiClazz.getAnnotation(BaseUrl.class);
        Retrofit retrofit = null;
        checkBuilder();
        if (annotation != null) {
            BaseUrl baseUrl = (BaseUrl) annotation;
            String baseUrlStr = baseUrl.value();
            if (!StrUtil.isEmpty(baseUrlStr)) {
                baseUrlStr = checkTest(baseUrlStr);
                retrofit = retrofitMap.get(baseUrlStr);
                if (retrofit == null) {
                    builder.baseUrl(baseUrlStr);
                    retrofit = builder.build();
                    retrofitMap.put(baseUrlStr, retrofit);
                }
            }
        }
        if (retrofit == null) {
            if (retrofitMap.containsKey(baseUrl)) {
                retrofit = retrofitMap.get(baseUrl);
            }
            if (retrofit == null) {
                retrofit = builder.baseUrl(baseUrl).build();
                retrofitMap.put(baseUrl, retrofit);
            }
        }
        return retrofit;
    }


    private void checkBuilder() {
        if (builder == null) {
            createDefaultBuilder();
        }
    }

    private void createDefaultBuilder() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpBuilder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor());
        if (Debug.isDebug())
            okHttpBuilder.addInterceptor(logging);
        client = okHttpBuilder
                .build();
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client);
    }


    /**
     * 检验是否为测试环境
     *
     * @param baseUrl
     * @return
     */
    public String checkTest(String baseUrl) {
        if (isTest && testUrlProvider != null) {
            String testUrl = testUrlProvider.getTestUrl(baseUrl);
            if (!StrUtil.isEmpty(testUrl)) {
                return testUrl;
            }
        }
        return baseUrl;
    }
}
