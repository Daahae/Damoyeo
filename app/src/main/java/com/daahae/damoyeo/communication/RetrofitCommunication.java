package com.daahae.damoyeo.communication;

import android.support.annotation.NonNull;
import android.util.Log;

import com.daahae.damoyeo.exception.ExceptionHandle;
import com.daahae.damoyeo.exception.ExceptionService;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.model.BuildingDetail;
import com.daahae.damoyeo.model.BuildingRequest;
import com.daahae.damoyeo.model.Landmark;
import com.daahae.damoyeo.model.MidInfo;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.model.UserRequest;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.view.Constant;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HEAD;

public class RetrofitCommunication {

    private RetrofitService retrofitService;
    private Retrofit retrofit;

    private ArrayList<String> totalTimes;
    private TransportInfoList transportList;
    private BuildingArr buildingList;
    private BuildingDetail buildingDetail;

    private UserCallBack userCallBack;
    private BuildingCallBack buildingCallBack;
    private BuildingDetailCallBack buildingDetailCallBack;

    private static RetrofitCommunication instance = new RetrofitCommunication();

    public static synchronized RetrofitCommunication getInstance() {
        return instance;
    }

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

    private void init() {
        retrofitService = retrofit.create(RetrofitService.class);
        totalTimes = new ArrayList<>();
    }

    private void connectServer(){
        retrofit = new Retrofit
                .Builder()
                .baseUrl(Constant.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void sendPersonLocation(ArrayList<Person> persons){
        try{
            ExceptionService.getInstance().isExistPerson(persons.size());
        }catch (ExceptionHandle e){
            e.printStackTrace();
            return;
        }
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
                    transportList = new Gson().fromJson(json, TransportInfoList.class);
                    try{
                        ExceptionService.getInstance().isExistTransportInformation(transportList);
                    } catch (ExceptionHandle e){
                        e.printStackTrace();
                        if (userCallBack != null) userCallBack.userDataPath(null);
                    }
                    if(transportList != null) {
                        Log.v("총 시간 개수", String.valueOf(transportList.getUserArr().size()));
                        TransportInfoList.getInstance().setUserArr(transportList.getUserArr());
                        if (!transportList.getUserArr().get(0).equals("Wrong Input")) {
                            // set MidInfo
                            LatLng latLng = new LatLng(transportList.getMidInfo().getMidLat(), transportList.getMidInfo().getMidLng());
                            MidInfo midInfo = new MidInfo(latLng, transportList.getMidInfo().getAddress());
                            MidInfo.setMidInfo(midInfo);
                            // set Landmark
                            LatLng lmlatlng = new LatLng(transportList.getLandmark().getLatitude(), transportList.getLandmark().getLongitude());
                            Landmark landmark = new Landmark(lmlatlng, transportList.getLandmark().getName(), transportList.getLandmark().getAddress());
                            Landmark.setLandMark(landmark);
                            //* set TransportInfo
                            for (TransportInfoList.Data data:transportList.getUserArr())
                                totalTimes.add(String.valueOf(data.getTotalTime()));
                        }
                        if (userCallBack != null) userCallBack.userDataPath(totalTimes);

                        Log.d("end1", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(System.currentTimeMillis()));
                    }

                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("retrofit","통신 실패");
            }
        });
    }

    private String makeForm(ArrayList<Person> persons){
        String strMessage="[";
        for(int i=0;i<persons.size();i++){
            strMessage += persons.get(i).getAddressPosition().toString();
            if(i!=persons.size()-1)
                strMessage += ",";
        }
        strMessage+="]";
        return strMessage;
    }

    private void sendBuildingInfo(UserRequest request){
        try{
            ExceptionService.getInstance().isCorrectUserRequest(request);
        }catch (ExceptionHandle e){
            e.printStackTrace();
            return;
        }
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

                    try {
                        ExceptionService.getInstance().isExistBuilding(buildingList);
                    } catch (ExceptionHandle exceptionHandle) {
                        exceptionHandle.printStackTrace();
                        if(buildingCallBack!=null)buildingCallBack.buildingDataPath(null);
                    }
                    if(buildingList!=null){
                        Log.v("총 빌딩 개수", String.valueOf(buildingList.getBuildingArr().size()));
                        if(buildingCallBack!=null)buildingCallBack.buildingDataPath(buildingList);
                    }

                    Log.d("end2", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(System.currentTimeMillis()));
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("retrofit","통신 실패");
            }
        });
    }


    private void sendBuildingDetail(BuildingRequest request){
        try {
            ExceptionService.getInstance().isCorrectBuildingRequest(request);
        } catch (ExceptionHandle exceptionHandle) {
            exceptionHandle.printStackTrace();
            return;
        }
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
                    try {
                        ExceptionService.getInstance().isExistBuildingDetail(buildingDetail);
                    } catch (ExceptionHandle exceptionHandle) {
                        exceptionHandle.printStackTrace();
                    }
                    buildingDetailCallBack.buildingDetailDataPath(buildingDetail);
                }
            }


            @Override
            public void onFailure(@NonNull retrofit2.Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("retrofit","통신 실패");
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
