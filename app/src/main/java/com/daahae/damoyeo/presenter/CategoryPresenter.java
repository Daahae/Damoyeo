package com.daahae.damoyeo.presenter;

import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.fragment.CategoryFragment;

import java.util.ArrayList;

public class CategoryPresenter {

    private CategoryFragment view;

    public CategoryPresenter(CategoryFragment view) {
        this.view = view;
    }

    public void setDefaultBuilding() {
        RetrofitCommunication.getInstance().setBuildingsData(Constant.DEPARTMENT_STORE);
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
