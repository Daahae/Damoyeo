package com.daahae.damoyeo.communication;

import android.util.Log;

import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.presenter.RetrofitPresenter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

    private boolean isGetMessage = false;

    private RetrofitPresenter retrofitPresenter;

    public RetrofitCommunication(RetrofitPresenter retrofitPresenter){
        connectServer();
        init(retrofitPresenter);

    }

    public boolean isGetMessage() {
        return isGetMessage;
    }

    public ArrayList<String> getTotalTimes() {
        return totalTimes;
    }

    private void init(RetrofitPresenter retrofitPresenter){
        retrofitService = retrofit.create(RetrofitService.class);
        persons = new ArrayList<>();
        totalTimes = new ArrayList<>();

        this.retrofitPresenter = retrofitPresenter;
    }

    private void connectServer(){
        retrofit = new Retrofit
                .Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ArrayList<String> sendPersonLocation(ArrayList<Person> persons){
        isGetMessage = false;
        this.persons = persons;
        String strMessage = makeForm();
        Log.v("메시지",strMessage);


        final retrofit2.Call<JsonObject> comment = retrofitService.getTest(strMessage);
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
                            TransportInfoList list = new Gson().fromJson(json, TransportInfoList.class);
                            Log.v("총 시간 개수", String.valueOf(list.getUserArr().size()));
                            //TODO: Json 좌표 읽어오기 실패시 "~~"String 값 받음

                            for (int i = 0; i < list.getUserArr().size(); i++) {
                                totalTimes.add(String.valueOf(list.getUserArr().get(i).getTotalTime()));
                            }
                            isGetMessage = true;
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                        //TODO: 전송실패시
                    }
                });
            }
        }).start();

        try {
            Thread.sleep(3000);
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


}
