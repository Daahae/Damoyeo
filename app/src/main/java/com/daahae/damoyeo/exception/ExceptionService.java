package com.daahae.damoyeo.exception;

import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.model.BuildingDetail;
import com.daahae.damoyeo.model.BuildingRequest;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.model.TransportLandmarkInfoList;
import com.daahae.damoyeo.model.UserRequest;
import com.daahae.damoyeo.view.Constant;

public class ExceptionService {


    private static ExceptionService instance = new ExceptionService();

    public static synchronized ExceptionService getInstance() {return instance;}


    public void isSetMarker(int markerNumber) throws ExceptionHandle{
        if(markerNumber==0){
            throw new ExceptionHandle("지정된 좌표값이 없습니다. 좌표를 확인해주세요");
        }else if(markerNumber<0){
            throw new ExceptionHandle("좌표지정에 오류가 있습니다. 좌표를 초기화해주세요.");
        }else if(markerNumber==1){
            throw new ExceptionHandle("사용자 좌표값을 2개 이상 입력해주세요");
        }
    }
    public void isExistPerson(int personNumber)throws ExceptionHandle{

        if(personNumber==0){
            throw new ExceptionHandle("사용자의 정보가 저장되지 않았습니다");
        }else if(personNumber<0){
            throw new ExceptionHandle("사용자의 정보가 잘못되었습니다");
        }
    }

    public void isExistTransportInformation(TransportInfoList transportInfoList) throws ExceptionHandle{
        if(transportInfoList == null){
            throw new ExceptionHandle("서버에서 사용자 정보를 받아오지 못했습니다");
        }
        else if (transportInfoList.getUserArr().size() <= 0) {
            throw new ExceptionHandle("받아온 사용자의 정보가 없습니다");
        }
    }
    public void isExistTransportLandmarkInformation(TransportLandmarkInfoList transportInfoList) throws ExceptionHandle{
        if(transportInfoList == null){
            throw new ExceptionHandle("서버에서 사용자 정보를 받아오지 못했습니다");
        }
        else if (transportInfoList.getUserArr().size() <= 0) {
            throw new ExceptionHandle("받아온 사용자의 정보가 없습니다");
        }
    }


    public void isCorrectUserRequest(UserRequest request) throws ExceptionHandle{
        int requestType = request.getType();
        if(requestType==0){
            throw new ExceptionHandle("빌딩 type 정보가 없습니다");
        } else if(requestType != Constant.DEPARTMENT_STORE  && requestType != Constant.SHOPPING_MALL && requestType != Constant.STADIUM
                && requestType != Constant.ZOO && requestType != Constant.MUSEUM && requestType != Constant.MOVIE_THEATER
                && requestType != Constant.AQUARIUM && requestType != Constant.CAFE && requestType != Constant.DRINK && requestType != Constant.RESTAURANT && requestType != Constant.ETC){
            throw new ExceptionHandle("잘못된 빌딩 type 입니다");
        }

    }

    public void isExistBuilding(BuildingArr buildingArr) throws ExceptionHandle {
        if(buildingArr==null)
            throw new ExceptionHandle("빌딩 정보를 읽어오지 못했습니다");
        else if(buildingArr.getBuildingArr().size()<=0){
            throw new ExceptionHandle("빌딩 정보가 존재하지 않습니다");
        }
    }

    public void isCorrectBuildingRequest(BuildingRequest request) throws ExceptionHandle{
        String name = request.getName();
        double lat = request.getLatitude();
        double lng = request.getLongitude();

        if(request == null)
            throw new ExceptionHandle("빌딩상세 정보 요청이 존재하지 않습니다");
        else if(name == null)
            throw new ExceptionHandle("빌딩 이름이 존재하지 않습니다");
        else if(lat>90||lat<-90)
            throw new ExceptionHandle("위도 값이 잘못되었습니다");
        else if(lng>180||lng<-180)
            throw new ExceptionHandle("경도 값이 잘못되었습니다");
    }

    public void isExistBuildingDetail(BuildingDetail buildingDetail) throws ExceptionHandle {
        if(buildingDetail == null)
            throw new ExceptionHandle("빌딩 상세 정보가 없습니다");
    }
}
