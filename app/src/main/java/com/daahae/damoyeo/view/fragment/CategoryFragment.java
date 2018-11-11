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
import com.daahae.damoyeo.presenter.Contract.CategoryFragmentContract;
import com.daahae.damoyeo.presenter.NMapActivityPresenter;
import com.daahae.damoyeo.view.adapter.BuildingAdapter;
import com.daahae.damoyeo.view.data.Constant;
import com.nhn.android.maps.NMapContext;

@SuppressLint("ValidFragment")
public class CategoryFragment extends Fragment implements View.OnClickListener, CategoryFragmentContract.View, AdapterView.OnItemClickListener, View.OnTouchListener
,SlidingDrawer.OnDrawerOpenListener,SlidingDrawer.OnDrawerCloseListener{

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

    private Button btnSmallCategory1, btnSmallCategory2, btnSmallCategory3;

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

        setPresenter(new CategoryFragmentPresenter(this, mapContext, parentPresenter));

        buildingAdapter = new BuildingAdapter();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_category, container, false);

        initView(rootView);
        initListener();
        connectAdapter();
        setBuildingList();

        return rootView;
    }


    private void initView(View rootView){

        btnBack = rootView.findViewById(R.id.btn_back_category);

        btnPlay = rootView.findViewById(R.id.btn_category_play);
        btnFood = rootView.findViewById(R.id.btn_category_food);
        btnCafe = rootView.findViewById(R.id.btn_category_cafe);
        btnDrink = rootView.findViewById(R.id.btn_category_drink);

        btnPlayNon = rootView.findViewById(R.id.btn_category_play_dummy);
        btnFoodNon = rootView.findViewById(R.id.btn_category_food_dummy);
        btnCafeNon = rootView.findViewById(R.id.btn_category_cafe_dummy);
        btnDrinkNon = rootView.findViewById(R.id.btn_category_drink_dummy);

        linearSmallCategory = rootView.findViewById(R.id.linear_small_category);

        btnSmallCategory1 = rootView.findViewById(R.id.btn_small_category_1);
        btnSmallCategory2 = rootView.findViewById(R.id.btn_small_category_2);
        btnSmallCategory3 = rootView.findViewById(R.id.btn_small_category_3);

        listCategory = rootView.findViewById(R.id.list_category);

        linearContent = rootView.findViewById(R.id.content);

        linearHandleMenu = rootView.findViewById(R.id.linear_handle_menu);
        linearCategoryMenu = rootView.findViewById(R.id.linear_category_menu);

        slidingDrawer = rootView.findViewById(R.id.slide);

    }

    private void initListener(){

        btnBack.setOnClickListener(this);

        btnPlay.setOnClickListener(this);
        btnFood.setOnClickListener(this);
        btnCafe.setOnClickListener(this);
        btnDrink.setOnClickListener(this);

        btnSmallCategory1.setOnClickListener(this);
        btnSmallCategory2.setOnClickListener(this);
        btnSmallCategory3.setOnClickListener(this);

        //각 리스트 아이템(building) 클릭
        listCategory.setOnItemClickListener(this);

        //SlidingDrawer 내려가는 기능
        linearContent.setOnTouchListener(this);

        //SlidingDrawer 내려갔을때 view
        slidingDrawer.setOnDrawerCloseListener(this);

        //SlidingDrawer 올라갔을때 view
        slidingDrawer.setOnDrawerOpenListener(this);
    }

    private void connectAdapter(){
        listCategory.setAdapter(buildingAdapter);
    }

    public void setBuildingList(){
        presenter.setBuildingInfo(buildingAdapter); // 빌딩 정보 넣기
    }

    @Override
    public void onDrawerClosed() {
        linearHandleMenu.setVisibility(View.VISIBLE);
        linearCategoryMenu.setVisibility(View.GONE);
    }

    @Override
    public void onDrawerOpened() {
        linearHandleMenu.setVisibility(View.GONE);
        linearCategoryMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();
        switch (id){
            case R.id.content:
                int action = motionEvent.getAction();

                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        slidingDrawer.animateClose();
                        break;
                }

                break;
        }
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        parentPresenter.changeView(Constant.DETAIL_PAGE);
        parentPresenter.clickItem(buildingAdapter.getItem(position));
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

                presenter.setClickFirstButton(btnSmallCategory1);
                linearSmallCategory.setVisibility(View.VISIBLE);
                btnSmallCategory1.setText("쇼핑");
                btnSmallCategory2.setText("관람");
                btnSmallCategory3.setVisibility(View.VISIBLE);
                btnSmallCategory3.setText("힐링");
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

                linearSmallCategory.setVisibility(View.GONE);
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

                presenter.setClickFirstButton(btnSmallCategory1);
                linearSmallCategory.setVisibility(View.VISIBLE);
                btnSmallCategory1.setText("카페");
                btnSmallCategory2.setText("베이커리");
                btnSmallCategory3.setVisibility(View.GONE);
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
                break;
            case R.id.btn_small_category_2:
                btnSmallCategory1.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory2.setBackgroundResource(R.color.colorWhite);
                btnSmallCategory3.setBackgroundResource(R.color.colorLightGray);
                break;
            case R.id.btn_small_category_3:
                btnSmallCategory1.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory2.setBackgroundResource(R.color.colorLightGray);
                btnSmallCategory3.setBackgroundResource(R.color.colorWhite);
                break;
        }
    }

    @Override
    public void setPresenter(CategoryFragmentPresenter presenter) {
        this.presenter = presenter;
    }
}
