package com.daahae.damoyeo.presenter;

import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.Person;

import java.util.ArrayList;

public class RetrofitPresenter {

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
}
