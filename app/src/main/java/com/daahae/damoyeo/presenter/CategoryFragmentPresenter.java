package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;

import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.presenter.Contract.CategoryFragmentContract;
import android.widget.Button;

import com.daahae.damoyeo.view.adapter.BuildingAdapter;
import com.nhn.android.maps.NMapContext;

public class CategoryFragmentPresenter extends NMapPresenter implements CategoryFragmentContract.Presenter {
    private Fragment view;
    private NMapActivityPresenter parentPresenter;

    private BuildingAdapter buildingAdapter;

    public CategoryFragmentPresenter(Fragment view, NMapContext mapContext, NMapActivityPresenter parentPresenter) {
        super(view, mapContext);
        this.parentPresenter = parentPresenter;
    }

    @Override
    public void setBuildingInfo(BuildingAdapter buildingAdapter) {
        this.buildingAdapter = buildingAdapter;

        buildingAdapter.resetList();
        makeDummy();
    }

    public void setClickFirstButton(Button button){
        button.setClickable(true);
    }

    //TODO: 삭제예정
    private void makeDummy(){
        buildingAdapter.add(new Building(0,0,"상호", "서울광역시 광진구 군자동", "010-0000-0000","가게에 대한 상세 설명란",0.86767676767));
        buildingAdapter.add(new Building(0,0,"상호2", "서울광역시 광진구 군자동", "010-1111-1111","가게에 대한 상세 설명란",20));
        buildingAdapter.add(new Building(0,0,"상호3", "서울광역시 광진구 군자동", "010-3333-3333","가게에 대한 상세 설명란",30));

    }

}
