package com.daahae.damoyeo.presenter.Contract;

import com.daahae.damoyeo.presenter.CategoryFragmentPresenter;
import com.daahae.damoyeo.view.adapter.BuildingAdapter;

public interface CategoryFragmentContract {

    interface View extends BaseView<CategoryFragmentPresenter> {

    }

    interface Presenter extends BaseNMap {

        void setBuildingInfo(BuildingAdapter buildingAdapter);
    }
}
