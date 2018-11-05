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
        selectMidFragmentPresenter = new SelectMidFragmentPresenter();

    }

    public ArrayList<String> getTotalTimes() {
        return totalTimes;
    }
    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void dummy(){
        addPerson("guest1","null",37.5502596,127.073139);
        addPerson("guest2","null",37.5252373,126.7029589);
        addPerson("guest3","null",37.5252373,126.7029589);
        addPerson("guest4","null",37.5252373,126.7029589);
        addPerson("guest5","null",37.5252373,126.7029589);
        addPerson("guest6","null",37.5252373,126.7029589);
        addPerson("guest7","null",37.5252373,126.7029589);
        addPerson("guest8","null",37.5252373,126.7029589);
        addPerson("guest9","null",37.5252373,126.7029589);
        addPerson("guest10","null",37.5252373,126.7029589);

    }

    public void addPerson(String name, String address, double latitude, double longitude){
        Person person = new Person(name,address,new Position(latitude, longitude));
        persons.add(person);
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
