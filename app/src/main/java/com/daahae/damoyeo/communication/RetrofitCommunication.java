package com.daahae.damoyeo.communication;

import android.util.Log;

import com.daahae.damoyeo.exception.PositionNumberServices;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.model.BuildingRequest;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.model.TransportInfoList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.HttpUrl;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCommunication{

    public static final String URL = "http://13.125.192.103/";

    private RetrofitService retrofitService;
    private Retrofit retrofit;

    private ArrayList<Person> persons;
    private ArrayList<String> totalTimes;
    private TransportInfoList TransportList;
    private BuildingArr buildingList;

    public RetrofitCommunication(){
        connectServer();

    }

    public ArrayList<String> getTotalTimes() {
        return totalTimes;
    }

    public TransportInfoList getList(){return TransportList;}

    private void init(){
        retrofitService = retrofit.create(RetrofitService.class);
        persons = new ArrayList<>();
        totalTimes = new ArrayList<>();

    }

    private void connectServer(){
        retrofit = new Retrofit
                .Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ArrayList<String> sendPersonLocation(ArrayList<Person> persons){
        this.persons = persons;
        String strMessage = makeForm();
        Log.v("메시지",strMessage);

        final retrofit2.Call<JsonObject> comment = retrofitService.getTransportData(strMessage);
        new Thread(new Runnable() {
            @Override
            public void run() {
                comment.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Log.v("알림", response.toString());
                            Log.v("전체", response.body().toString());
                            JsonObject json = response.body();
                            TransportList = new Gson().fromJson(json, TransportInfoList.class);
                            Log.v("총 시간 개수", String.valueOf(TransportList.getUserArr().size()));

                            PositionNumberServices positionNumberServices = new PositionNumberServices();

                            try{
                                positionNumberServices.isPosition(TransportList.getUserArr().size());
                                if(!TransportList.getUserArr().get(0).equals("Wrong Input")){
                                    for (int i = 0; i < TransportList.getUserArr().size(); i++) {
                                        totalTimes.add(String.valueOf(TransportList.getUserArr().get(i).getTotalTime()));
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                    }
                });
            }
        }).start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return totalTimes;
    }

    private String makeForm(){
        String strMessage="[";
        for(int i=0;i<persons.size();i++){
            if(i==persons.size()-1) strMessage += persons.get(i).getAddressPosition().toString();
            else strMessage += persons.get(i).getAddressPosition().toString()+",";
        }
        strMessage+="]";
        return strMessage;
    }


    public BuildingArr sendBuildingInfo(BuildingRequest request)  {
        String message = request.toString();
        Log.v("빌딩 메시지", message);

        final retrofit2.Call<JsonObject> comment = retrofitService.getBuildingData(message);
        new Thread(new Runnable() {
            @Override
            public void run() {
                comment.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Log.v("알림", response.toString());
                            Log.v("전체", response.body().toString());
                            JsonObject json = response.body();
                            buildingList = new Gson().fromJson(json, BuildingArr.class);
                            for(int i=0;i<buildingList.getBuildingArr().size();i++)
                                Log.v("총 빌딩 개수", String.valueOf(buildingList.getBuildingArr().get(i).toString()));


                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                    }
                });
            }
        }).start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return buildingList;
    }

}

