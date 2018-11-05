package com.daahae.damoyeo.presenter.Contract;

import android.support.v4.app.Fragment;

import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.MidInfo;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.presenter.NMapActivityPresenter;

import java.util.ArrayList;

public interface NMapActivityContract {

    interface View extends BaseView<NMapActivityPresenter> {

    }

    interface Presenter extends BasePresenter {

        void backView(Fragment fragment);

        void changeView(int nextPageNumber);

        ArrayList<Person> getPersonList();

        MidInfo getMid();

        Building getBuilding();

        void initPersonList();

        // TODO setMid & setBuilding
    }
}
