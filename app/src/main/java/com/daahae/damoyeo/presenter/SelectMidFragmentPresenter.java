package com.daahae.damoyeo.presenter;

import com.daahae.damoyeo.view.fragment.SelectMidFragment;

import java.util.ArrayList;

public class SelectMidFragmentPresenter {


    public static final int MID_ALGORITHM = 1;
    public static final int LANDMARK = 2;

    private SelectMidFragment view;

    public SelectMidFragmentPresenter(SelectMidFragment view) {
        this.view = view;
    }

    public void selectMid(int selectMidMenu){
        switch (selectMidMenu){
            case MID_ALGORITHM:
                selectMidAlgorithm();
                break;
            case LANDMARK:
                selectLandmark();
                break;
        }
    }

    private void selectMidAlgorithm(){

    }

    private void selectLandmark(){

    }

}
