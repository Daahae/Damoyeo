package com.daahae.damoyeo.presenter;

import android.app.Fragment;
import android.util.Log;
import android.view.View;

import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.daahae.damoyeo.view.fragment.SelectMidFragment;

import java.util.ArrayList;

public class RetrofitPresenter {

    private RetrofitCommunication retrofit;

    private ArrayList<Person> persons;
    private ArrayList<String> totalTimes;

    private SelectMidFragmentPresenter selectMidFragmentPresenter;


    public RetrofitPresenter(){
        retrofit = new RetrofitCommunication(this);
        persons = new ArrayList<>();
        totalTimes = new ArrayList<>();
    }

    public ArrayList<String> getTotalTimes() {
        return totalTimes;
    }
    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void addPerson(ArrayList<Person> person){
        persons = person;
    }

    public ArrayList<String> sendPersonMessage(){
        return retrofit.sendPersonLocation(persons);
    }

    public ArrayList<String> receiveTotalTime(){
        totalTimes = retrofit.getTotalTimes();
        return totalTimes;
    }

    public void callData(ArrayList<String> totalTimes){
        this.totalTimes = totalTimes;
        if(totalTimes.size()!=0)
        Log.v("데이터","call back");
    }

}
