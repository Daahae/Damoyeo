package com.daahae.damoyeo.presenter;

import android.util.Log;
import android.widget.ListView;

import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.daahae.damoyeo.view.fragment.SelectMidFragment;

import java.util.ArrayList;

public class SelectMidFragmentPresenter {


    public static final int MID_ALGORITHM = 1;
    public static final int LANDMARK = 2;

    private SelectMidFragment view;

    private MarkerTimeAdapter markerTimeAdapter;

    private RetrofitPresenter retrofitPresenter;

    private  ArrayList<Person> people;
    private ArrayList<String> totalTimes;

    private ListView listMarkerTime;


    public SelectMidFragmentPresenter() {
        totalTimes = new ArrayList<>();
        people = new ArrayList<>();
        markerTimeAdapter = new MarkerTimeAdapter();
    }


    public SelectMidFragmentPresenter(SelectMidFragment view) {
        this.view = view;
        totalTimes = new ArrayList<>();
        people = new ArrayList<>();
        markerTimeAdapter = new MarkerTimeAdapter();
    }

    public void initList(ListView listView){
        listMarkerTime = listView;
    }

    public void initMarkerTime(ArrayList<String> totalTimes){
        this.totalTimes = totalTimes;
    }

    public void selectMid(int selectMidMenu){
        switch (selectMidMenu){
            case MID_ALGORITHM:
                selectMidAlgorithm();
                break;
            case LANDMARK:
                selectLandmark();
                break;
        }
    }

    private void selectMidAlgorithm(){

    }

    private void selectLandmark(){

    }

    public void setResetList(){
        markerTimeAdapter.resetList();
    }

    public void setMarkerTimeList(MarkerTimeAdapter markerTimeAdapter){

        this.markerTimeAdapter = markerTimeAdapter;

        for(int i=0; i<totalTimes.size();i++){
            markerTimeAdapter.add("dummy",totalTimes.get(i));
            //Log.v("데이터",people.get(i).getName()+totalTimes.get(i));
        }
        Log.v("어뎁터","set Data");

    }

}
