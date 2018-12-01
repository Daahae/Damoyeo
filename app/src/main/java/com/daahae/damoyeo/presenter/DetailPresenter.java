package com.daahae.damoyeo.presenter;

import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.BuildingDetail;
import com.daahae.damoyeo.view.fragment.DetailFragment;

public class DetailPresenter {

    private DetailFragment view;

    public DetailPresenter(DetailFragment view) {
        this.view = view;
    }

    public void startCallback() {
        RetrofitCommunication.BuildingDetailCallBack buildingDetailCallBack = new RetrofitCommunication.BuildingDetailCallBack() {
            @Override
            public void buildingDetailDataPath(BuildingDetail buildingDetail) {

                view.setBuildingInfo();
                view.setBuildingDetail(buildingDetail);

                view.setBuildingLocation();
                view.showMid();
                view.showBuilding();
            }
        };
        RetrofitCommunication.getInstance().setBuildingDetailData(buildingDetailCallBack);
    }
}