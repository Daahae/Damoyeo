package com.daahae.damoyeo.communication;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface RetrofitService {

    //@Multipart
    @FormUrlEncoded
    @POST("usersToMid")
    Call<JsonObject> getMidTransportData(@Field("userArr") String user);

    //@Multipart
    @FormUrlEncoded
    @POST("midCategory")
    Call<JsonObject> getBuildingData(@Field("userRequest") String type);

    //@Multipart
    @FormUrlEncoded
    @POST("midDetailCategory")
    Call<JsonObject> getBuildingDetail(@Field("buildingRequest") String name);

    //@Multipart
    @FormUrlEncoded
    @POST("midTransportInfo")
    Call<JsonObject> getLandMarkTransportData(@Field("userArr") String user);



}