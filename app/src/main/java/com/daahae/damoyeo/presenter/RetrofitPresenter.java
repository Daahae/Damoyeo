package com.daahae.damoyeo.presenter;

import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;

import java.util.ArrayList;

public class RetrofitPresenter {


    private RetrofitCommunication retrofit;

    private ArrayList<Person> persons;
    private ArrayList<String> totalTimes;

    public RetrofitPresenter(){
        retrofit = new RetrofitCommunication();
        persons = new ArrayList<>();
        totalTimes = new ArrayList<>();
    }

    public ArrayList<String> getTotalTimes() {
        return totalTimes;
    }
    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void dummy(){
        addPerson("guest1","null",37.5502596,127.073139);
        addPerson("guest2","null",37.543802,127.069145);

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

}
