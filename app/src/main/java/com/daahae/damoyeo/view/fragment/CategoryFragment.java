package com.daahae.damoyeo.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.presenter.CategoryFragmentPresenter;
import com.daahae.damoyeo.presenter.NMapActivityPresenter;
import com.daahae.damoyeo.view.adapter.BuildingAdapter;
import com.nhn.android.maps.NMapContext;

@SuppressLint("ValidFragment")
public class CategoryFragment extends Fragment implements View.OnClickListener{

    private NMapContext mapContext;
    private CategoryFragmentPresenter presenter;
    private NMapActivityPresenter parentPresenter;

    private ImageButton btnBack;
    private ImageButton btnPlayNon, btnFoodNon, btnCafeNon, btnDrinkNon;
    private ImageButton btnPlay, btnFood, btnCafe, btnDrink;

    private SlidingDrawer slidingDrawer;

    private LinearLayout linearContent;
    private LinearLayout linearHandleMenu;
    private LinearLayout linearCategoryMenu;
    private LinearLayout linearSmallCategory;

    private Button btnSmallCategory1, btnSmallCategory2, btnSmallCategory3, btnSmallCategory4;

    private BuildingAdapter buildingAdapter;
    private ListView listCategory;

    public CategoryFragment(NMapActivityPresenter parentPresenter) {
        this.parentPresenter = parentPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapContext = new NMapContext(super.getActivity());;
        mapContext.onCreate();
        presenter = new CategoryFragmentPresenter(this, mapContext, parentPresenter);
        buildingAdapter = new BuildingAdapter();
    }

    @SuppressLint("ClickableViewAccessibility")
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

        btnPlayNon = rootView.findViewById(R.id.btn_category_play_dummy);
        btnFoodNon = rootView.findViewById(R.id.btn_category_food_dummy);
        btnCafeNon = rootView.findViewById(R.id.btn_category_cafe_dummy);
        btnDrinkNon = rootView.findViewById(R.id.btn_category_drink_dummy);

        linearSmallCategory = rootView.findViewById(R.id.linear_small_category);

        btnSmallCategory1 = rootView.findViewById(R.id.btn_small_category_1);
        btnSmallCategory1.setOnClickListener(this);

        btnSmallCategory2 = rootView.findViewById(R.id.btn_small_category_2);
        btnSmallCategory2.setOnClickListener(this);

        btnSmallCategory3 = rootView.findViewById(R.id.btn_small_category_3);
        btnSmallCategory3.setOnClickListener(this);

        btnSmallCategory4 = rootView.findViewById(R.id.btn_small_category_4);
        btnSmallCategory4.setOnClickListener(this);

        listCategory = rootView.findViewById(R.id.list_category);
        presenter.setBuildingInfo(buildingAdapter); // 빌딩 정보 넣기

        listCategory.setAdapter(buildingAdapter);
        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: 리스트뷰 아이템 클릭시
            }
        });

        linearContent = rootView.findViewById(R.id.content);
        linearContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        slidingDrawer.animateClose();
                        break;
                }
                return false;
            }
        });
        linearHandleMenu = rootView.findViewById(R.id.linear_handle_menu);
        linearCategoryMenu = rootView.findViewById(R.id.linear_category_menu);

        slidingDrawer = rootView.findViewById(R.id.slide);
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                linearHandleMenu.setVisibility(View.GONE);
                linearCategoryMenu.setVisibility(View.VISIBLE);
            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                linearHandleMenu.setVisibility(View.VISIBLE);
                linearCategoryMenu.setVisibility(View.GONE);
            }
        });

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

                btnPlayNon.setImageResource(R.drawable.btn_category_play_orange);
                btnFoodNon.setImageResource(R.drawable.btn_category_food_gray);
                btnCafeNon.setImageResource(R.drawable.btn_category_cafe_gray);
                btnDrinkNon.setImageResource(R.drawable.btn_category_drink_gray);

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

                btnPlayNon.setImageResource(R.drawable.btn_category_play_gray);
                btnFoodNon.setImageResource(R.drawable.btn_category_food_orange);
                btnCafeNon.setImageResource(R.drawable.btn_category_cafe_gray);
                btnDrinkNon.setImageResource(R.drawable.btn_category_drink_gray);

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

                btnPlayNon.setImageResource(R.drawable.btn_category_play_gray);
                btnFoodNon.setImageResource(R.drawable.btn_category_food_gray);
                btnCafeNon.setImageResource(R.drawable.btn_category_cafe_orange);
                btnDrinkNon.setImageResource(R.drawable.btn_category_drink_gray);

                linearSmallCategory.setVisibility(View.GONE);
                break;
            case R.id.btn_category_drink:
                btnPlay.setImageResource(R.drawable.btn_category_play_gray);
                btnFood.setImageResource(R.drawable.btn_category_food_gray);
                btnCafe.setImageResource(R.drawable.btn_category_cafe_gray);
                btnDrink.setImageResource(R.drawable.btn_category_drink_orange);

                btnPlayNon.setImageResource(R.drawable.btn_category_play_gray);
                btnFoodNon.setImageResource(R.drawable.btn_category_food_gray);
                btnCafeNon.setImageResource(R.drawable.btn_category_cafe_gray);
                btnDrinkNon.setImageResource(R.drawable.btn_category_drink_orange);

                linearSmallCategory.setVisibility(View.GONE);
                break;

            case R.id.btn_small_category_1:
                btnSmallCategory1.setBackgroundResource(R.color.colorWhite);
                btnSmallCategory2.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory3.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory4.setBackgroundResource(R.color.colorLightGray);
                break;
            case R.id.btn_small_category_2:
                btnSmallCategory1.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory2.setBackgroundResource(R.color.colorWhite);
                btnSmallCategory3.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory4.setBackgroundResource(R.color.colorLightGray);
                break;
            case R.id.btn_small_category_3:
                btnSmallCategory1.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory2.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory3.setBackgroundResource(R.color.colorWhite);
                btnSmallCategory4.setBackgroundResource(R.color.colorLightGray);
                break;
            case R.id.btn_small_category_4:
                btnSmallCategory1.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory2.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory3.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory4.setBackgroundResource(R.color.colorWhite);
                break;
        }
    }
}
