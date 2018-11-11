package com.daahae.damoyeo.communication;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {

    //@Multipart
    @FormUrlEncoded
    @POST("usersToMid")
    Call<JsonObject> getTransportData(@Field("userArr") String users);

    //@Multipart
    @FormUrlEncoded
    @POST("midCategory")
    Call<JsonObject> getBuildingData(@Field("userRequest") String type);
}