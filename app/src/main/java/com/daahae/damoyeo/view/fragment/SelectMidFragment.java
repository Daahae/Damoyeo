package com.daahae.damoyeo.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.presenter.NMapActivityPresenter;
import com.daahae.damoyeo.presenter.NMapFragmentPresenter;
import com.daahae.damoyeo.presenter.SelectMidFragmentPresenter;
import com.daahae.damoyeo.view.adapter.BuildingAdapter;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.nhn.android.maps.NMapContext;

@SuppressLint("ValidFragment")
public class SelectMidFragment extends Fragment implements View.OnClickListener{
    private NMapContext mapContext;

    private SelectMidFragmentPresenter presenter;
    private NMapActivityPresenter parentPresenter;

    private ImageButton btnSelectMid;
    private ImageButton btnBack;

    private ImageButton btnSelectMidAlgorithm, btnSelectLandmark;

    private MarkerTimeAdapter markerTimeAdapter;
    private ListView listMarkerTime;

    private Button btnAllMarkerList;

    public SelectMidFragment(NMapActivityPresenter parentPresenter) {
        this.parentPresenter = parentPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapContext = new NMapContext(super.getActivity());;
        mapContext.onCreate();
        presenter = new SelectMidFragmentPresenter(this, mapContext);
        markerTimeAdapter = new MarkerTimeAdapter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_select_mid, container, false);

        btnSelectMid = rootView.findViewById(R.id.btn_select_mid);
        btnSelectMid.setOnClickListener(this);

        btnBack = rootView.findViewById(R.id.btn_back_select_mid);
        btnBack.setOnClickListener(this);

        btnSelectMidAlgorithm = rootView.findViewById(R.id.btn_select_mid_algorithm);
        btnSelectMidAlgorithm.setOnClickListener(this);

        btnSelectLandmark = rootView.findViewById(R.id.btn_select_landmark);
        btnSelectLandmark.setOnClickListener(this);

        listMarkerTime = rootView.findViewById(R.id.list_marker_time);
        presenter.setMarkerTimeList(markerTimeAdapter, parentPresenter.getPersonList());

        listMarkerTime.setAdapter(markerTimeAdapter);

        btnAllMarkerList = rootView.findViewById(R.id.btn_all_marker_list);
        btnAllMarkerList.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        presenter.init(this);
        mapContext.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapContext.onResume();
        presenter.initLocation(parentPresenter.getPersonList());
    }

    @Override
    public void onPause() {
        super.onPause();
        mapContext.onPause();
    }

    @Override
    public void onStop() {
        mapContext.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mapContext.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_select_mid:
                parentPresenter.changeView(parentPresenter.CATEGORY_PAGE);
                break;
            case R.id.btn_back_select_mid:
                parentPresenter.backView(this);
                break;
            case R.id.btn_select_mid_algorithm:
                presenter.selectMid(presenter.MID_ALGORITHM);
                btnSelectMidAlgorithm.setImageResource(R.drawable.btn_selected_mid_white);
                btnSelectMidAlgorithm.setBackgroundResource(R.color.appMainColor);
                btnSelectLandmark.setImageResource(R.drawable.btn_selected_landmark_orange);
                btnSelectLandmark.setBackgroundResource(R.color.colorWhite);
                break;
            case R.id.btn_select_landmark:
                presenter.selectMid(presenter.LANDMARK);
                btnSelectMidAlgorithm.setImageResource(R.drawable.btn_selected_mid_orange);
                btnSelectMidAlgorithm.setBackgroundResource(R.color.colorWhite);
                btnSelectLandmark.setImageResource(R.drawable.btn_selected_landmark_white);
                btnSelectLandmark.setBackgroundResource(R.color.appMainColor);
                break;

            case R.id.btn_all_marker_list:
                presenter.showSavedMarkers(0, parentPresenter.getPersonList());
                break;
        }
    }
}
