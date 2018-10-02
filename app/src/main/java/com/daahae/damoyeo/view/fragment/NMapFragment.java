package com.daahae.damoyeo.view.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.presenter.NMapFragmentPresenter;
import com.daahae.damoyeo.view.data.FloatingActionBtn;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nhn.android.maps.NMapContext;

import java.util.ArrayList;

/**
 * NMapFragment 클래스는 NMapActivity를 상속하지 않고 NMapView만 사용하고자 하는 경우에 NMapContext를 이용한 예제임.
 * NMapView 사용시 필요한 초기화 및 리스너 등록은 NMapActivity 사용시와 동일함.
 */
public class NMapFragment extends Fragment {
    private NMapContext mapContext;
    private NMapFragmentPresenter presenter;

    private FloatingActionBtn fabtn;

    /* Fragment 라이프사이클에 따라서 NMapContext의 해당 API를 호출함 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapContext = new NMapContext(super.getActivity());;
        mapContext.onCreate();
        presenter = new NMapFragmentPresenter(this, mapContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //throw new IllegalArgumentException("onCreateView should be implemented in the subclass of NMapFragment.");
        View rootView = (View) inflater.inflate(R.layout.fragment_nmap, container, false);
        //mMapView = (NMapView) rootView.findViewById(R.id.mapView);

        fabtn = new FloatingActionBtn();
        fabtn.setFabOpen(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open));
        fabtn.setFabClose(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close));

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.fab:
                        fabtn.anim();
                        break;
                    case R.id.fab1:
                        presenter.initLocation();
                        fabtn.anim();
                        break;
                    case R.id.fab2:
                        // GPS
                        presenter.startMyLocation();
                        fabtn.anim();
                        break;
                }
            }
        };

        fabtn.setFab((FloatingActionButton)rootView.findViewById(R.id.fab));
        fabtn.setFab1((FloatingActionButton) rootView.findViewById(R.id.fab1));
        fabtn.setFab2((FloatingActionButton) rootView.findViewById(R.id.fab2));
        fabtn.getFab().setOnClickListener(onClickListener);
        fabtn.getFab1().setOnClickListener(onClickListener);
        fabtn.getFab2().setOnClickListener(onClickListener);

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