package com.whaley.core.bi;

import com.whaley.core.bi.model.BIResponse;
import com.whaleyvr.core.network.http.response.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Author: qxw
 * Date: 2016/11/4
 */

public interface BIApi {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("http://vrlog.aginomoto.com/vrapplog")
    Observable<BIResponse> requestPost(@Body RequestBody metadataBean);

//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("http://test-wlsconfig.aginomoto.com/env")
//    Call<BIIsTestResponse> requestIsTestPost();


    @Headers({"Content-Type: application/json", "Accept: application/json", "forwardhost:vrlog.aginomoto.com"})
    @POST("http://test-wlslog.aginomoto.com/vrapplog")
    Observable<BIResponse> requestTestPost(@Body RequestBody metadataBean);
}
