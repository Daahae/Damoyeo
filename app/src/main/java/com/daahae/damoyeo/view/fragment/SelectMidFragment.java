package com.daahae.damoyeo.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.MidInfo;
import com.daahae.damoyeo.presenter.Contract.SelectMidFragmentContract;
import com.daahae.damoyeo.presenter.NMapActivityPresenter;
import com.daahae.damoyeo.presenter.NMapFragmentPresenter;
import com.daahae.damoyeo.presenter.SelectMidFragmentPresenter;
import com.daahae.damoyeo.view.adapter.BuildingAdapter;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.daahae.damoyeo.view.data.Constant;
import com.nhn.android.maps.NMapContext;

@SuppressLint("ValidFragment")
public class SelectMidFragment extends Fragment implements View.OnClickListener, SelectMidFragmentContract.View, AdapterView.OnItemClickListener {
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

        setPresenter(new SelectMidFragmentPresenter(this, mapContext));

        markerTimeAdapter = new MarkerTimeAdapter(presenter,parentPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_select_mid, container, false);

        initView(rootView);
        initListener();

        presenter.initMarkerTime(parentPresenter.getTotalTimes());
        markerTimeAdapter.resetList();
        presenter.setMarkerTimeList(markerTimeAdapter);
        listMarkerTime.setAdapter(markerTimeAdapter);
        Log.v("mid","뷰 생성");

        listMarkerTime.setOnItemClickListener(this);

        return rootView;
    }

    private void initView(View rootView){

        btnBack = rootView.findViewById(R.id.btn_back_select_mid);

        btnSelectMidAlgorithm = rootView.findViewById(R.id.btn_select_mid_algorithm);
        btnSelectLandmark = rootView.findViewById(R.id.btn_select_landmark);

        btnSelectMid = rootView.findViewById(R.id.btn_select_mid);

        listMarkerTime = rootView.findViewById(R.id.list_marker_time);

        btnAllMarkerList = rootView.findViewById(R.id.btn_all_marker_list);
    }

    private void initListener(){
        btnSelectMid.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSelectMidAlgorithm.setOnClickListener(this);
        btnSelectLandmark.setOnClickListener(this);
        btnAllMarkerList.setOnClickListener(this);
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
        presenter.showMidInfoAllMarkers(8, parentPresenter.getMid(), parentPresenter.getPersonList());
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
                parentPresenter.changeView(Constant.CATEGORY_PAGE);
                break;
            case R.id.btn_back_select_mid:
                parentPresenter.backView(this);
                break;
            case R.id.btn_select_mid_algorithm:
                presenter.setSelectMidFlg(Constant.MID_ALGORITHM, parentPresenter.getMid(), parentPresenter.getBuilding(), parentPresenter.getPersonList());
                btnSelectMidAlgorithm.setImageResource(R.drawable.btn_selected_mid_white);
                btnSelectMidAlgorithm.setBackgroundResource(R.color.appMainColor);
                btnSelectLandmark.setImageResource(R.drawable.btn_selected_landmark_orange);
                btnSelectLandmark.setBackgroundResource(R.color.colorWhite);
                break;
            case R.id.btn_select_landmark:
                presenter.setSelectMidFlg(Constant.LANDMARK, parentPresenter.getMid(), parentPresenter.getBuilding(), parentPresenter.getPersonList());
                btnSelectMidAlgorithm.setImageResource(R.drawable.btn_selected_mid_orange);
                btnSelectMidAlgorithm.setBackgroundResource(R.color.colorWhite);
                btnSelectLandmark.setImageResource(R.drawable.btn_selected_landmark_white);
                btnSelectLandmark.setBackgroundResource(R.color.appMainColor);
                break;

            case R.id.btn_all_marker_list:
                if(presenter.getSelectMidFlg() == Constant.MID_ALGORITHM)
                    presenter.showMidInfoAllMarkers(0, parentPresenter.getMid(), parentPresenter.getPersonList());
                else if(presenter.getSelectMidFlg() == Constant.LANDMARK)
                    presenter.showLandmarkAllMarkers(0, parentPresenter.getBuilding(), parentPresenter.getPersonList());
                break;
        }
    }

    @Override
    public void setPresenter(SelectMidFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(presenter.getSelectMidFlg() == Constant.MID_ALGORITHM)
            presenter.showMidInfoEachMarker(parentPresenter.getMid(), parentPresenter.getPersonList().get(position).getAddressPosition());
        else if(presenter.getSelectMidFlg() == Constant.LANDMARK)
            presenter.showLandmarkEachMarker(parentPresenter.getBuilding(), parentPresenter.getPersonList().get(position).getAddressPosition());
    }
}