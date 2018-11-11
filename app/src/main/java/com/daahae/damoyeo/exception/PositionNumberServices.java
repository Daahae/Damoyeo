package com.daahae.damoyeo.exception;

import android.util.Log;

public class PositionNumberServices {

    public void isPosition(int positionNumber) throws PositionNumberException{
        if(positionNumber<0){
            Log.v("좌표 오류","좌표 값을 읽어오지 못했습니다. \n마커를 확인해주세요.");
        }
    }
}
