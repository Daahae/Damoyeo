package com.daahae.damoyeo.presenter;

import com.daahae.damoyeo.SQL.ResentSearchDBHelper;
import com.daahae.damoyeo.view.Constant;

public class MapsFragmentPresenter {

    ResentSearchDBHelper dbHelper;
    public MapsFragmentPresenter(){
        dbHelper = new ResentSearchDBHelper(Constant.context);
    }

    public void saveSearchName(String search){
        dbHelper.insertResentSearch(search);
    }

}
