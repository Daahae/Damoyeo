package com.daahae.damoyeo.presenter;
import com.daahae.damoyeo.view.activity.MainActivity;

public class Presenter {

    private MainActivity view;// 뷰
    //모델은 각자 클래스 생성

    public Presenter(MainActivity view){
        this.view = view;
    }



}