package com.whaley.core.sample;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by dell on 2017/7/10.
 */

public interface TestApi {

    @GET("http://www.baidu.com")
    Observable<ResponseBody> requestTest();

}
