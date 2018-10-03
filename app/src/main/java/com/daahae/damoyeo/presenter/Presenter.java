package com.daahae.damoyeo.presenter;
import android.content.Context;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.model.Transport;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.view.activity.MainActivity;
import com.daahae.damoyeo.view.activity.ODsaySampleActivity;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class Presenter {

    private MainActivity view;// 뷰
    //모델은 각자 클래스 생성

    public Presenter(MainActivity view) {
        this.view = view;
    }


    private TransportInfoList getTransportInfoList(ODsayData oDsayData, Position start, Position end) {// 출발지와 도착지의 대중교통 시간구하기
        TransportInfoList transportInfoList;// 최종 반환 ArrayList
        ArrayList<Transport> transportArrayList = new ArrayList<>();
        ODsayService odsayService;
        JSONObject jsonObject;
        int totalTime = 0;
        Context context;

        odsayService = ODsayService.init(view.getApplicationContext(), "H74js9B6IE4zrRu+gLY2jJNXnRydRohaTTDgxbjNWro");
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);
        jsonObject = oDsayData.getJson();

        try {
            jsonObject = jsonObject.getJSONObject("result");
            JSONArray pathJa = jsonObject.getJSONArray("path");
            StringBuffer sb = new StringBuffer();// 샘플 확인을 위함

            JSONObject jo = pathJa.getJSONObject(0);// 제일 빠른 경로만
            JSONArray subPathJa = jo.getJSONArray("subPath");
            sb.append("제일 빠른 경로 --------\n+");

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
                sb.append(
                    "trafficType : " + trafficType + "\n" +
                    "sectionTime : " + sectionTime + "분\n" +
                    "transportNumber : " + transportNumber + "\n" +
                    "startStation : " + startName + "\n" +
                    "endStation : " + endName + "\n" +
                    "\n"
                );
                Transport transport = new Transport(trafficType,sectionTime,startName,endName,transportNumber);
                transportArrayList.add(transport);
                totalTime += sectionTime;
            }


            sb.append("totalTime : " + totalTime + "분" + "\n\n");
            transportInfoList = new TransportInfoList(transportArrayList, totalTime);

            return transportInfoList;

        } catch (Exception E) {
            E.printStackTrace();
        }
        return null;
    }

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




