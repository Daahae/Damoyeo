package com.daahae.damoyeo.presenter.Contract;

import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.MidInfo;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.presenter.SelectMidFragmentPresenter;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.daahae.damoyeo.view.fragment.SelectMidFragment;

import java.util.ArrayList;

public interface SelectMidFragmentContract {

    interface View extends BaseView<SelectMidFragmentPresenter> {

    }

    interface Presenter extends BaseNMap {

        int getSelectMidFlg();

        void setMarkerTimeList(MarkerTimeAdapter markerTimeAdapter, ArrayList<Person> personList);

        void showListView(ArrayList<Person> personList);

        void setSelectMidFlg(int selectMidMenu, MidInfo mid, Building building, ArrayList<Person> personList);

        void showMidInfoAllMarkers(int scale, MidInfo mid, ArrayList<Person> personList);

        void showMidInfoEachMarker(MidInfo mid, Position pos);

        void showLandmarkAllMarkers(int scale, Building building, ArrayList<Person> personList);

        void showLandmarkEachMarker(Building building, Position pos);
    }
}
