package com.daahae.damoyeo.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.presenter.CategoryFragmentPresenter;
import com.daahae.damoyeo.presenter.ClickableSlidingDrawerPresenter;
import com.daahae.damoyeo.presenter.NMapActivityPresenter;
import com.daahae.damoyeo.presenter.SelectMidFragmentPresenter;

@SuppressLint("ValidFragment")
public class CategoryFragment extends Fragment implements View.OnClickListener{

    private CategoryFragmentPresenter presenter;
    private NMapActivityPresenter parentPresenter;

    private ImageButton btnBack;
    private ImageButton btnPlay, btnFood, btnCafe, btnDrink;

    private ClickableSlidingDrawerPresenter slidingDrawer;
    private LinearLayout linearHandle;

    private LinearLayout linearSmallCategory;

    private Button btnSmallCategory1, btnSmallCategory2, btnSmallCategory3, btnSmallCategory4;

    public CategoryFragment(NMapActivityPresenter parentPresenter) {
        this.parentPresenter = parentPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CategoryFragmentPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_category, container, false);

        btnBack = rootView.findViewById(R.id.btn_back_category);
        btnBack.setOnClickListener(this);

        btnPlay = rootView.findViewById(R.id.btn_category_play);
        btnPlay.setOnClickListener(this);

        btnFood = rootView.findViewById(R.id.btn_category_food);
        btnFood.setOnClickListener(this);

        btnCafe = rootView.findViewById(R.id.btn_category_cafe);
        btnCafe.setOnClickListener(this);

        btnDrink = rootView.findViewById(R.id.btn_category_drink);
        btnDrink.setOnClickListener(this);

        linearHandle = rootView.findViewById(R.id.handle);

        slidingDrawer = rootView.findViewById(R.id.slide);
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                // 로딩 끝나고 화면이 켜질때 값을 띄워줌
                linearHandle.setDrawingCacheEnabled(false);
            }
        });

        linearSmallCategory = rootView.findViewById(R.id.linear_small_category);

        btnSmallCategory1 = rootView.findViewById(R.id.btn_small_category_1);
        btnSmallCategory2 = rootView.findViewById(R.id.btn_small_category_2);
        btnSmallCategory3 = rootView.findViewById(R.id.btn_small_category_3);
        btnSmallCategory4 = rootView.findViewById(R.id.btn_small_category_4);

        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_back_category:
                parentPresenter.backView(this);
                break;
            case R.id.btn_category_play:
                btnPlay.setImageResource(R.drawable.btn_category_play_orange);
                btnFood.setImageResource(R.drawable.btn_category_food_gray);
                btnCafe.setImageResource(R.drawable.btn_category_cafe_gray);
                btnDrink.setImageResource(R.drawable.btn_category_drink_gray);
                linearSmallCategory.setVisibility(View.VISIBLE);
                btnSmallCategory1.setText("노래방");
                btnSmallCategory2.setText("당구장");
                btnSmallCategory3.setText("PC방");
                btnSmallCategory4.setText("볼링장");
                break;
            case R.id.btn_category_food:
                btnPlay.setImageResource(R.drawable.btn_category_play_gray);
                btnFood.setImageResource(R.drawable.btn_category_food_orange);
                btnCafe.setImageResource(R.drawable.btn_category_cafe_gray);
                btnDrink.setImageResource(R.drawable.btn_category_drink_gray);
                linearSmallCategory.setVisibility(View.VISIBLE);
                btnSmallCategory1.setText("일식");
                btnSmallCategory2.setText("한식");
                btnSmallCategory3.setText("중식");
                btnSmallCategory4.setText("양식");
                break;
            case R.id.btn_category_cafe:
                btnPlay.setImageResource(R.drawable.btn_category_play_gray);
                btnFood.setImageResource(R.drawable.btn_category_food_gray);
                btnCafe.setImageResource(R.drawable.btn_category_cafe_orange);
                btnDrink.setImageResource(R.drawable.btn_category_drink_gray);
                linearSmallCategory.setVisibility(View.GONE);
                break;
            case R.id.btn_category_drink:
                btnPlay.setImageResource(R.drawable.btn_category_play_gray);
                btnFood.setImageResource(R.drawable.btn_category_food_gray);
                btnCafe.setImageResource(R.drawable.btn_category_cafe_gray);
                btnDrink.setImageResource(R.drawable.btn_category_drink_orange);
                linearSmallCategory.setVisibility(View.GONE);
                break;
        }
    }
}
