package com.daahae.damoyeo.presenter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.model.Transport;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.model.TransportLandmarkInfoList;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.fragment.CategoryFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CategoryPresenter {

    private CategoryFragment view;
    private CheckTypesTask loading;

    public CategoryPresenter(CategoryFragment view) {
        this.view = view;
        loading = new CheckTypesTask();
        loading.execute();
    }

    public void setSelectCategory(int category) {
        Log.d("start2", new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(System.currentTimeMillis()));
        if(!view.isMid())
            RetrofitCommunication.getInstance().setBuildingsData(category);
        else
            RetrofitCommunication.getInstance().setBuildingsDataInLandmark(category);
    }

    public void getBuildingDetailFromServer(int index) {
        RetrofitCommunication.getInstance().clickItem(view.getBuildingAdapter().getItem(index));
    }

    public void setLandmarkTransport(){
        ArrayList<String> totalTimes = new ArrayList<>();
        for(int i = 0; i< TransportLandmarkInfoList.getInstance().getUserArr().size(); i++){
            totalTimes.add(String.valueOf(TransportLandmarkInfoList.getInstance().getUserArr().get(i).getTotalTime()));
        }
        view.initMarkerTime(totalTimes);
        view.setMarkerTimeList(view.getMarkerTimeAdapter());
        view.getListMarkerTime().setAdapter(view.getMarkerTimeAdapter());
    }

    public void setMidInfoTransport(){
        ArrayList<String> totalTimes = new ArrayList<>();
        for(int i=0;i<TransportInfoList.getInstance().getUserArr().size();i++){
            totalTimes.add(String.valueOf(TransportInfoList.getInstance().getUserArr().get(i).getTotalTime()));
        }
        view.initMarkerTime(totalTimes);
        view.setMarkerTimeList(view.getMarkerTimeAdapter());
        view.getListMarkerTime().setAdapter(view.getMarkerTimeAdapter());
    }

    public void clickLandmark(){

        RetrofitCommunication.getInstance().setBuildingsDataInLandmark();

        RetrofitCommunication.UserLandmarkBack userLandmarkBack = new RetrofitCommunication.UserLandmarkBack() {
            @Override
            public void userLandmarkDataPath(ArrayList<String> totalTimes) {
                Log.v("랜드마크", TransportInfoList.getInstance().toString());
                view.initMarkerTime(totalTimes);
                Log.v("전체 시간", totalTimes.toString());
                view.setMarkerTimeList(view.getMarkerTimeAdapter());
                view.getListMarkerTime().setAdapter(view.getMarkerTimeAdapter());
            }
            @Override
            public void disconnectServer() {
                Toast.makeText(Constant.context,"랜드마크 탐색에 실패했습니다",Toast.LENGTH_SHORT).show();

            }
        };
        RetrofitCommunication.getInstance().setUserLandmarkData(userLandmarkBack);
    }

    public void startCallback() {
        if(Constant.existPerson) return;
        loading.onPreExecute();

        RetrofitCommunication.UserCallBack userCallBack = new RetrofitCommunication.UserCallBack() {
            @Override
            public void userDataPath(ArrayList<String> totalTimes) {
                loading.onPostExecute(null);
                view.initMarkerTime(totalTimes);
                view.setMarkerTimeList(view.getMarkerTimeAdapter());
                view.getListMarkerTime().setAdapter(view.getMarkerTimeAdapter());
                view.showAllMarkers();
                view.setCameraState(view.getRelativeMap());
                Constant.existPerson = true;
            }

            @Override
            public void disconnectServer() {
                loading.onPostExecute(null);
                view.getParentView().changeView(Constant.MAPS_PAGE); // 뒤로가기
                Toast.makeText(Constant.context,"중간지점 탐색에 실패했습니다",Toast.LENGTH_SHORT).show();
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
                    Thread.sleep(30000);
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
