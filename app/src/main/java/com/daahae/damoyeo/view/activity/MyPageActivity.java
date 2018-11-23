package com.daahae.damoyeo.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.adapter.FriendsAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btnEdit;
    private ImageButton btnBack, btnAdd;
    private Button btnLogout;
    private EditText editSearchFriends, editMyName, editMyAddress;

    private int viewMode;
    private ListView listView;
    private FriendsAdapter friendsAdapter;

    public MyPageActivity(){
        friendsAdapter = new FriendsAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        initView();
        initListener();
        setMode(Constant.FRIENDS_LIST_MODE);

        connectAdapter();
    }

    private void connectAdapter(){
        listView.setAdapter(friendsAdapter);
    }

    private void setMode(int mode){
        viewMode = mode;
    }

    private void initView(){
        btnEdit = findViewById(R.id.btn_edit_my_profile);
        btnBack = findViewById(R.id.btn_back_my_page);
        btnAdd = findViewById(R.id.btn_search_friends_my_page);
        btnLogout = findViewById(R.id.btn_logout_my_page);

        editMyName = findViewById(R.id.edit_my_name_my_page);
        editMyName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        editMyAddress = findViewById(R.id.edit_my_address_my_page);
        editSearchFriends = findViewById(R.id.edit_search_friends_my_page);

        listView = findViewById(R.id.list_friend);

    }

    private void initListener(){
        btnEdit.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_edit_my_profile:
                //TODO: 프로필 수정 클릭시
                break;
            case R.id.btn_back_my_page:
                // setResult();
                // viewMainPage();
                finish();
                break;
            case R.id.btn_search_friends_my_page:
                if(viewMode==Constant.FRIENDS_LIST_MODE){
                    //TODO: 현재 친구 목록 보기 일때,
                    btnAdd.setImageDrawable(getResources().getDrawable(R.drawable.ic_sub_orange));
                    editSearchFriends.setVisibility(View.VISIBLE);
                    setMode(Constant.SEARCH_FRIENDS_MODE);
                }
                else if(viewMode==Constant.SEARCH_FRIENDS_MODE){
                    //TODO: 현재 친구 찾기 모드 일때,
                    btnAdd.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_orange));
                    editSearchFriends.setVisibility(View.GONE);
                    setMode(Constant.FRIENDS_LIST_MODE);
                }
                break;
        }
    }
}
