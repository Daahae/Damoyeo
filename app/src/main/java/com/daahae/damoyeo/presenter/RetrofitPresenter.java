package com.daahae.damoyeo.presenter;

import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.BuildingRequest;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.TransportInfoList;

import java.util.ArrayList;

public class RetrofitPresenter {
    private static RetrofitPresenter instance = new RetrofitPresenter();

    public static synchronized RetrofitPresenter getInstance() {return instance;}

    private RetrofitCommunication retrofit;

    private ArrayList<Person> persons;

    public RetrofitPresenter(){
        retrofit = new RetrofitCommunication(this);
    }

    public void setPersonList(ArrayList<Person> person){
        persons = person;
    }

    public ArrayList<String> sendPersonMessage(){
        return retrofit.sendPersonLocation(persons);
    }

    public TransportInfoList getList(){return retrofit.getList();}

    public void sendBuildingInfo(BuildingRequest buildingRequest){
        retrofit.sendBuildingInfo(buildingRequest);
    }
}
