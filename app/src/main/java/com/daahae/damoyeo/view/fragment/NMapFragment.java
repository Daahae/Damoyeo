package com.daahae.damoyeo.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.presenter.NMapActivityPresenter;
import com.daahae.damoyeo.presenter.NMapFragmentPresenter;
import com.daahae.damoyeo.presenter.RetrofitPresenter;
import com.daahae.damoyeo.view.data.FloatingActionBtn;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nhn.android.maps.NMapContext;

import java.util.ArrayList;

/**
 * NMapFragment 클래스는 NMapActivity를 상속하지 않고 NMapView만 사용하고자 하는 경우에 NMapContext를 이용한 예제임.
 * NMapView 사용시 필요한 초기화 및 리스너 등록은 NMapActivity 사용시와 동일함.
 */
@SuppressLint("ValidFragment")
public class NMapFragment extends Fragment{
    private NMapContext mapContext;
    private NMapFragmentPresenter presenter;

    private RetrofitPresenter retrofitPresenter;

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
        presenter = new NMapFragmentPresenter(this, mapContext);
        retrofitPresenter = new RetrofitPresenter();
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
                    case R.id.layout_addmarker:
                        presenter.clickMarker();
                        break;
                }
            }
        };

        LinearLayout layoutAddMarker = rootView.findViewById(R.id.layout_addmarker);
        presenter.setLayoutAddMarker(layoutAddMarker);
        layoutAddMarker.setOnClickListener(layoutOnClickListener);

        fabtn = new FloatingActionBtn();
        fabtn.setFabOpen(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open));
        fabtn.setFabClose(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close));

        Button.OnClickListener btnOnClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fab_menu:
                        fabtn.anim();
                        break;
                    case R.id.fab_gps:
                        // GPS
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
                        presenter.viewAllMarkerFull();
                        fabtn.anim();
                        break;
                    case R.id.linear_search_mid:
                        parentPresenter.sendMarkerTimeMessage(retrofitPresenter.getPersons(),presenter.sendRetrofit());
                        parentPresenter.changeView(parentPresenter.SELECT_MID_PAGE);

                        break;

                }
            }
        };

        fabtn.setFabMenu((FloatingActionButton) rootView.findViewById(R.id.fab_menu));
        fabtn.setFabGPS((FloatingActionButton) rootView.findViewById(R.id.fab_gps));
        fabtn.setFabPick((FloatingActionButton) rootView.findViewById(R.id.fab_pick));
        fabtn.setFabClear((FloatingActionButton) rootView.findViewById(R.id.fab_clear));
        fabtn.setFabFull((FloatingActionButton) rootView.findViewById(R.id.fab_full));

        fabtn.getFabMenu().setOnClickListener(btnOnClickListener);
        fabtn.getFabGPS().setOnClickListener(btnOnClickListener);
        fabtn.getFabPick().setOnClickListener(btnOnClickListener);
        fabtn.getFabClear().setOnClickListener(btnOnClickListener);
        fabtn.getFabFull().setOnClickListener(btnOnClickListener);

        linearBtnSearchMid = rootView.findViewById(R.id.linear_search_mid);
        linearBtnSearchMid.setOnClickListener(btnOnClickListener);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();

        presenter.init();
        mapContext.onStart();

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                getActivity().finish();
            }
        };

        // GPS 위치정보를 받기위해 권한을 설정
        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("지도 서비스를 사용하기 위해서는 위치 접근 권한이 필요해요")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
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
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        mapContext.onDestroy();
        super.onDestroy();
    }
}