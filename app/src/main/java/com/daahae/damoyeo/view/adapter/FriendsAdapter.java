package com.daahae.damoyeo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;

import java.util.ArrayList;

public class FriendsAdapter extends BaseAdapter {

    //TODO: Person 클리스가 아닌 로그인 보내주는 정보에 맞게 클래스 생성
    private ArrayList<Person> people;

    private ImageView imgPhoto;
    private TextView txtName, txtAddress;

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int i) {
        return people.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void add(Person person){
        people.add(person);
    }

    public void addItem(String name, String address, Position position){
        Person person = new Person(name, address, position);
        people.add(person);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_friend, parent, false);
        }

        initGetView(convertView);

        Person myItem = (Person) people.get(position);

        //TODO: View에 set

        return convertView;
    }

    private void initGetView(View view){
        imgPhoto = view.findViewById(R.id.img_friend_photo_my_page);
        txtName = view.findViewById(R.id.txt_friend_name_my_page);
        txtAddress = view.findViewById(R.id.txt_friend_address_my_page);
    }
}
