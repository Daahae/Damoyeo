package com.daahae.damoyeo.presenter.Contract;

import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.presenter.NMapFragmentPresenter;
import com.daahae.damoyeo.view.fragment.NMapFragment;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;

import java.util.ArrayList;

public interface NMapFragmentContract {

    interface View extends BaseView<NMapFragmentPresenter> {

        void setAddress(String address);

        void setVisibleAddress(boolean isVisible);

        void setIsFixedMarker(boolean isFixedMarker);
    }

    interface Presenter extends BaseNMap {

        NMapPlacemark getInstantMarker();

        Person getTargetMarker();

        void setInstantMarkerAddress(NMapPlacemark instantMarker);

        void setInstantFloatingMarker(Position pos);

        void setSavedMarkers(int scale, ArrayList<Person> personList);

        void getGPSLocation(NMapFragment view, ArrayList<Person> personList);

        void pickLocation(ArrayList<Person> personList);

        void fixMarker(boolean isFixedMarker, NMapFragment view, NMapPlacemark instantMarker, Person targetMarker, ArrayList<Person> personList);

        void saveMarker(NMapFragment view, NMapPlacemark instantMarker, ArrayList<Person> personList);

        void removeMarker(NMapFragment view, Person targetMarker, ArrayList<Person> personList);

        void setTargetMarkerById(NMapFragment view, int id, ArrayList<Person> personList);

        void showSavedMarkersOnSaveState(ArrayList<Person> personList);

        void showSavedMarkers(NMapFragment view, ArrayList<Person> personList);
    }
}
