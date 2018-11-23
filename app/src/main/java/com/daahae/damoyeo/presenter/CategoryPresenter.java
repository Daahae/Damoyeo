package com.daahae.damoyeo.presenter;

import android.util.Log;

import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.fragment.CategoryFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CategoryPresenter {

    private CategoryFragment view;

    public CategoryPresenter(CategoryFragment view) {
        this.view = view;
    }

    public void setDefaultCategory() {
        Log.d("start2", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(System.currentTimeMillis()));
        RetrofitCommunication.getInstance().setBuildingsData(Constant.DEPARTMENT_STORE);
    }

    public void setSelectCategory(int category) {
        Log.d("start2", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(System.currentTimeMillis()));
        RetrofitCommunication.getInstance().setBuildingsData(category);
    }

    public void getBuildingDetailFromServer(int index) {
        RetrofitCommunication.getInstance().clickItem(view.getBuildingAdapter().getItem(index));
    }

    public void startCallback() {

        RetrofitCommunication.UserCallBack userCallBack = new RetrofitCommunication.UserCallBack() {
            @Override
            public void userDataPath(ArrayList<String> totalTimes) {
                view.initMarkerTime(totalTimes);
                view.setMarkerTimeList(view.getMarkerTimeAdapter());
                view.getListMarkerTime().setAdapter(view.getMarkerTimeAdapter());
            }
        };
        RetrofitCommunication.getInstance().setUserData(userCallBack);

        RetrofitCommunication.BuildingCallBack buildingCallBack = new RetrofitCommunication.BuildingCallBack() {
            @Override
            public void buildingDataPath(BuildingArr buildingArr) {
                view.initBuildingInfo(buildingArr);
                view.convertList(view.setBuildingInfo(view.getBuildingAdapter()));
                view.getListCategory().setAdapter(view.getBuildingAdapter());
            }
        };
        RetrofitCommunication.getInstance().setBuildingData(buildingCallBack);
    }
}
