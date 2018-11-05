package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;

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
        for(int i=0;i<3;i++){
            buildingAdapter.addDummy();
        }
    }
}
