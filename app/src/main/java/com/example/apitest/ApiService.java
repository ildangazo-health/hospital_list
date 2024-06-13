package com.example.apitest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("openapi/service/rest/HmcSearchService/getHmcList")
    Call<ApiResponse> getItems(@Query("serviceKey") String serviceKey,
                               @Query("hmcNm") String hmcNm,
                               @Query("siDoCd") String siDoCd,
                               @Query("siGunGuCd") String siGunGuCd);
}

