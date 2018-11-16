package com.daahae.damoyeo.communication;

import android.support.annotation.NonNull;
import android.util.Log;

import com.daahae.damoyeo.exception.PositionNumberServices;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.model.BuildingDetail;
import com.daahae.damoyeo.model.BuildingRequest;
import com.daahae.damoyeo.model.UserRequest;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.view.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCommunication{

    private static final String URL = "http://13.125.192.103/";

    private RetrofitService retrofitService;
    private Retrofit retrofit;

    private ArrayList<String> totalTimes;
    private TransportInfoList TransportList;
    private BuildingArr buildingList;
    private BuildingDetail buildingDetail;

    private UserCallBack userCallBack;
    private BuildingCallBack buildingCallBack;
    private BuildingDetailCallBack buildingDetailCallBack;

    private static RetrofitCommunication instance = new RetrofitCommunication();

    public static synchronized RetrofitCommunication getInstance() {return instance;}

    public interface UserCallBack {
        void userDataPath(ArrayList<String> totalTimes);
    }

    public interface BuildingCallBack {
        void buildingDataPath(BuildingArr buildingArr);
    }

    public interface BuildingDetailCallBack{
        void buildingDetailDataPath(BuildingDetail buildingDetail);
    }

    private RetrofitCommunication(){
        connectServer();
        init();
    }

    public void setUserData(UserCallBack userCallBack){
        this.userCallBack = userCallBack;
    }
    public void setBuildingData(BuildingCallBack buildingCallBack){
        this.buildingCallBack = buildingCallBack;
    }
    public void setBuildingDetailData(BuildingDetailCallBack buildingDetailCallBack){
        this.buildingDetailCallBack = buildingDetailCallBack;
    }

    private void init(){
        retrofitService = retrofit.create(RetrofitService.class);
        totalTimes = new ArrayList<>();
    }

    private void connectServer(){
        retrofit = new Retrofit
                .Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void sendPersonLocation(ArrayList<Person> persons){
        String strMessage = makeForm(persons);
        Log.v("메시지",strMessage);

        final retrofit2.Call<JsonObject> comment = retrofitService.getTransportData(strMessage);
        comment.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.v("알림", response.toString());
                    Log.v("전체", response.body().toString());
                    JsonObject json = response.body();
                    TransportList = new Gson().fromJson(json, TransportInfoList.class);
                    Log.v("총 시간 개수", String.valueOf(TransportList.getUserArr().size()));

                    //* set TransportInfo
                    TransportInfoList.getInstance().setUserArr(TransportList.getUserArr());
                    PositionNumberServices positionNumberServices = new PositionNumberServices();

                    try {
                        positionNumberServices.isPosition(TransportList.getUserArr().size());
                        if (!TransportList.getUserArr().get(0).equals("Wrong Input")) {
                            for (int i = 0; i < TransportList.getUserArr().size(); i++) {
                                totalTimes.add(String.valueOf(TransportList.getUserArr().get(i).getTotalTime()));
                            }
                        }
                        if(userCallBack!=null) userCallBack.userDataPath(totalTimes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<JsonObject> call, @NonNull Throwable t) {
            }
        });
    }

    private String makeForm(ArrayList<Person> persons){
        String strMessage="[";
        for(int i=0;i<persons.size();i++){
            strMessage += persons.get(i).getAddressPosition().toString();
            if(i!=persons.size()-1)
                strMessage += ",";;
        }
        strMessage+="]";
        return strMessage;
    }

    private void sendBuildingInfo(UserRequest request){
        String message = request.toString();
        Log.v("메시지",message+"");

        final retrofit2.Call<JsonObject> comment = retrofitService.getBuildingData(message);
        comment.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.v("알림", response.toString());
                    Log.v("전체", response.body().toString());
                    JsonObject json = response.body();
                    buildingList = new Gson().fromJson(json, BuildingArr.class);
                    Log.v("총 빌딩 개수", String.valueOf(buildingList.getBuildingArr().size()));

                    if(buildingCallBack!=null)buildingCallBack.buildingDataPath(buildingList);
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<JsonObject> call, @NonNull Throwable t) {
            }
        });
    }


    private void sendBuildingDetail(BuildingRequest request){
        String message = request.toString();
        Log.v("메시지",message+"");

        final retrofit2.Call<JsonObject> comment = retrofitService.getBuildingDetail(message);
        comment.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.v("알림", response.toString());
                    Log.v("전체", response.body().toString());
                    JsonObject json = response.body();
                    buildingDetail = new Gson().fromJson(json, BuildingDetail.class);
                    buildingDetailCallBack.buildingDetailDataPath(buildingDetail);

                }
            }


            @Override
            public void onFailure(@NonNull retrofit2.Call<JsonObject> call, @NonNull Throwable t) {
            }
        });
    }

    public void sendMarkerTimeMessage(){
        sendPersonLocation(Person.getInstance());
        Log.v(Constant.TAG, "전송");
    }

    public void clickItem(Building building){
        Building.setInstance(building);
        BuildingRequest buildingRequest = new BuildingRequest(building.getName(),building.getLatitude(),building.getLongitude());
        sendBuildingDetail(buildingRequest);
    }

    public void setBuildingsData(int buildingType){
        UserRequest request = new UserRequest();
        request.setType(buildingType);
        sendBuildingInfo(request);
    }
}
