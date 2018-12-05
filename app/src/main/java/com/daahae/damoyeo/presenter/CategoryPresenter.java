package com.daahae.damoyeo.presenter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.activity.MainActivity;
import com.daahae.damoyeo.view.fragment.CategoryFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.internal.Util;

public class CategoryPresenter {

    private CategoryFragment view;
    private CheckTypesTask lording;

    public CategoryPresenter(CategoryFragment view) {
        this.view = view;
        lording = new CheckTypesTask();
        lording.execute();
    }

    public void setDefaultCategory() {
        Log.d("start2", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(System.currentTimeMillis()));
        RetrofitCommunication.getInstance().setBuildingsData(Constant.DEPARTMENT_STORE);
    }

    public void setSelectCategory(int category) {
        Log.d("start2", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(System.currentTimeMillis()));
        RetrofitCommunication.getInstance().setBuildingsData(category);
    }

    public void getBuildingDetailFromServer(int index) {
        RetrofitCommunication.getInstance().clickItem(view.getBuildingAdapter().getItem(index));
    }

    public void startCallback() {
        lording.onPreExecute();

        RetrofitCommunication.UserCallBack userCallBack = new RetrofitCommunication.UserCallBack() {
            @Override
            public void userDataPath(ArrayList<String> totalTimes) {
                view.initMarkerTime(totalTimes);
                view.setMarkerTimeList(view.getMarkerTimeAdapter());
                view.getListMarkerTime().setAdapter(view.getMarkerTimeAdapter());
                view.showAllMarkers();
                view.setCameraState(view.getRelativeMap());
                lording.onPostExecute(null);

                Log.v("에러 메시지", TransportInfoList.getInstance().getUserArr().get(1).getError());
            }

            @Override
            public void disconnectServer() {
                view.getParentView().changeView(Constant.MAPS_PAGE); // 뒤로가기
            }
        };
        RetrofitCommunication.getInstance().setUserData(userCallBack);

        RetrofitCommunication.BuildingCallBack buildingCallBack = new RetrofitCommunication.BuildingCallBack() {
            @Override
            public void buildingDataPath(BuildingArr buildingArr) {
                view.initBuildingInfo(buildingArr);
                view.convertList(view.setBuildingInfo(view.getBuildingAdapter()));
                view.getListCategory().setAdapter(view.getBuildingAdapter());
            }
        };
        RetrofitCommunication.getInstance().setBuildingData(buildingCallBack);
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(Constant.context, R.style.progress_bar_style);

        @Override
        protected void onPreExecute() {

            asyncDialog.setCancelable(false);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("중간지점을 찾고 있습니다");

            // show dialog
            asyncDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    asyncDialog.setProgress(i*30);
                    Thread.sleep(3000);
                }

            } catch (InterruptedException e) {
                view.getParentView().changeView(Constant.MAPS_PAGE);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
}
