package com.daahae.damoyeo.presenter;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.model.Transport;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.view.activity.MainActivity;
import com.daahae.damoyeo.view.activity.ODsaySampleActivity;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class Presenter {
    TransportInfoList transportInfoList = null;// 최종 반환 ArrayList

    private ODsaySampleActivity view;// 뷰, MainActivity view로 차후 수정
    //모델은 각자 클래스 생성

    public Presenter(ODsaySampleActivity view) {//뷰, MainActivity view로 차후 수정
        this.view = view;
    }


    public void getTransportInfoList( Position start, Position end) {// 출발지와 도착지의 대중교통 시간구하기
        ODsayService odsayService;
        odsayService = ODsayService.init(view.getApplicationContext(), "H74js9B6IE4zrRu+gLY2jJNXnRydRohaTTDgxbjNWro");
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);
        odsayService.requestSearchPubTransPath(start.getLatitude()+"",start.getLongitude()+"", end.getLatitude()+"",end.getLongitude()+"", "1", "0", "0", onResultCallbackListener);
  }

    private OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            ArrayList<Transport> transportArrayList = new ArrayList<>();
            JSONObject jsonObject;
            int totalTime = 0;
            jsonObject = oDsayData.getJson();

            try {
                jsonObject = jsonObject.getJSONObject("result");
                JSONArray pathJa = jsonObject.getJSONArray("path");
                JSONObject jo = pathJa.getJSONObject(0);// 제일 빠른 경로만
                JSONArray subPathJa = jo.getJSONArray("subPath");

                for (int i = 0; i < subPathJa.length(); i++) {
                    JSONObject tmpJo = subPathJa.getJSONObject(i);
                    int trafficType = tmpJo.getInt("trafficType");
                    int sectionTime = tmpJo.getInt("sectionTime");
                    String startName = null;
                    String endName = null;
                    String transportNumber = null;

                    if (!isTypeWalk(trafficType)) {// 도보가 아닐시, lane과 출발지 목적지 존재
                        startName = tmpJo.getString("startName");
                        endName = tmpJo.getString("endName");
                        JSONArray laneJa = tmpJo.getJSONArray("lane");
                        JSONObject laneJo = laneJa.getJSONObject(0);

                        if (isTypeSubway(trafficType)) {//지하철일시
                            transportNumber = laneJo.getString("name");
                        } else {//버스일시
                            transportNumber = laneJo.getString("busNo");
                        }
                    }

                    Transport transport = new Transport(trafficType,sectionTime,startName,endName,transportNumber);
                    transportArrayList.add(transport);
                    totalTime += sectionTime;
                }

                transportInfoList = new TransportInfoList(transportArrayList, totalTime);
                Log.e("err",transportInfoList.getTransportInfo().get(0).getType()+"");
                totalTime = transportInfoList.getTotalTime();
                transportArrayList = transportInfoList.getTransportInfo();++++


                StringBuffer sb = new StringBuffer();
                for(int i=0;i<transportArrayList.size();i++){
                    Transport transport = transportArrayList.get(i);
                    sb.append(
                            "trafficType : " + transport.getType() + "\n" +
                            "sectionTime : " + transport.getTime() + "분\n" +
                            "transportNumber : "+ transport.getTransportNumber()+"\n"+
                            "startStation : " + transport.getStartStation() + "\n" +
                            "endStation : " + transport.getEndStation() + "\n" +
                            "\n"
                    );

                }
                sb.append("총 소요시간"+totalTime);

                view.tv_data.setText(sb);
            } catch (Exception E) {
                E.printStackTrace();
            }
        }

        @Override
        public void onError(int i, String s, API api) {

        }
    };



    private boolean isTypeSubway(int trafficType){
        if(trafficType == 1)
            return true;
        return false;
    }

    private boolean isTypeBus(int trafficType){
        if(trafficType == 2)
            return true;
        return false;
    }

    private boolean isTypeWalk(int trafficType){
        if(trafficType == 3)
            return true;
        return false;
    }




}




