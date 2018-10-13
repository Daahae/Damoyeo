package com.daahae.damoyeo.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.presenter.NMapActivityPresenter;
import com.daahae.damoyeo.presenter.NMapFragmentPresenter;
import com.daahae.damoyeo.view.data.FloatingActionBtn;
import com.nhn.android.maps.NMapContext;

/**
 * NMapFragment 클래스는 NMapActivity를 상속하지 않고 NMapView만 사용하고자 하는 경우에 NMapContext를 이용.
 * NMapView 사용시 필요한 초기화 및 리스너 등록은 NMapActivity 사용시와 동일함.
 */
@SuppressLint("ValidFragment")
public class NMapFragment extends Fragment implements View.OnClickListener{
    private NMapContext mapContext;
    private NMapFragmentPresenter presenter;

    private FloatingActionBtn fabtn;
    private LinearLayout linearBtnSearchMid;

    private NMapActivityPresenter parentPresenter;

    public NMapFragment(NMapActivityPresenter parentPresenter) {
        this.parentPresenter = parentPresenter;
    }

    /* Fragment 라이프사이클에 따라서 NMapContext의 해당 API를 호출함 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapContext = new NMapContext(super.getActivity());;
        mapContext.onCreate();
        presenter = new NMapFragmentPresenter(this, parentPresenter,  mapContext);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = (View) inflater.inflate(R.layout.fragment_nmap, container, false);

        TextView tvAddress = rootView.findViewById(R.id.tv_address);
        presenter.setTvAddress(tvAddress);

        LinearLayout layoutAddress = rootView.findViewById(R.id.layout_address);
        presenter.setLayoutAddress(layoutAddress);

        LinearLayout.OnClickListener layoutOnClickListener = new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.layout_fixmarker:
                        presenter.fixMarker();
                        break;
                }
            }
        };

        LinearLayout layoutAddMarker = rootView.findViewById(R.id.layout_fixmarker);
        presenter.setLayoutAddMarker(layoutAddMarker);
        layoutAddMarker.setOnClickListener(layoutOnClickListener);

        fabtn = new FloatingActionBtn();
        fabtn.setFabOpen(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open));
        fabtn.setFabClose(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close));

        fabtn.setFabMenu((FloatingActionButton) rootView.findViewById(R.id.fab_menu));
        fabtn.setFabGPS((FloatingActionButton) rootView.findViewById(R.id.fab_gps));
        fabtn.setFabPick((FloatingActionButton) rootView.findViewById(R.id.fab_pick));
        fabtn.setFabClear((FloatingActionButton) rootView.findViewById(R.id.fab_clear));
        fabtn.setFabFull((FloatingActionButton) rootView.findViewById(R.id.fab_full));

        fabtn.getFabMenu().setOnClickListener(this);
        fabtn.getFabGPS().setOnClickListener(this);
        fabtn.getFabPick().setOnClickListener(this);
        fabtn.getFabClear().setOnClickListener(this);
        fabtn.getFabFull().setOnClickListener(this);

        linearBtnSearchMid = rootView.findViewById(R.id.linear_search_mid);
        linearBtnSearchMid.setOnClickListener(this);

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
        switch (view.getId()) {
            case R.id.fab_menu:
                fabtn.anim();
                break;
            case R.id.fab_gps:
                // GPS
                presenter.getPermission();
                presenter.getGPSLocation();
                fabtn.anim();
                break;
            case R.id.fab_pick:
                // 직접 마커 지정
                presenter.pickLocation();
                fabtn.anim();
                break;
            case R.id.fab_clear:
                // 초기화
                presenter.initLocation();
                fabtn.anim();
                break;
            case R.id.fab_full:
                // 전체보기
                presenter.showSavedMarkers(parentPresenter.getPersonList());
                fabtn.anim();
                break;
            case R.id.linear_search_mid:
                parentPresenter.changeView(parentPresenter.SELECT_MID_PAGE);
                break;
        }
    }
}