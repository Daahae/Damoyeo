package com.daahae.damoyeo.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Transport;
import com.daahae.damoyeo.model.TransportInfoList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TransportAdapter  extends BaseAdapter {

    private ImageView imgProfile;
    private TextView txtName,txtAddress,txtTotalTime,txtViewTime;
    private List<TransportInfoList.Data> mItems;
    private ArrayList<Person> people;
    private LinearLayout linearTransportBar;
    public TransportAdapter(List<TransportInfoList.Data> list, ArrayList<Person> people){
        mItems = list;
        this.people = people;
        Log.v("어뎁터","데이터 삽입");
    }

    /* 아이템을 세트로 담기 위한 어레이 */

    public void resetList(){
        mItems.clear();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public TransportInfoList.Data getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public void add(TransportInfoList.Data data){
        mItems.add(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_transport, parent, false);
        }

        initGetView(convertView);

        TransportInfoList.Data myItem = (TransportInfoList.Data) mItems.get(position);
        Person person = people.get(position);

        String strViewTime = getViewTime(getTime(), myItem.getTotalTime());

        setBuildingListText(null,person.getName(),person.getAddress(),formTime(myItem.getTotalTime()),strViewTime);

        return convertView;
    }

    private String getViewTime(Calendar calendar, int totalTime){


        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        int nowTime = hour*60 + min;
        int expectedTime = totalTime + nowTime;


        String viewTime = formTime(nowTime)+"-"+formTime(expectedTime);

        return viewTime;
    }

    private String formTime(int time){
        int timeHour = time/60;
        int timeMin = time%60;
        String strTime;
        if(timeHour>12){
            timeHour -=12;
            strTime = "오후"+timeHour+"시"+timeMin + "분";
        }
        else{
            strTime = "오전"+timeHour+"시"+timeMin + "분";
        }
        return strTime;
    }

    private Calendar getTime(){
        GregorianCalendar today = new GregorianCalendar( );
        return today;
    }


    private void setBuildingListText(ImageView profile, String name, String address, String totalTime, String viewTime){
        imgProfile.setImageResource(R.drawable.ic_guest_profile);
        txtName.setText(name);
        if(address.length()>18) {
            String shortAddr = address.substring(0,17);
            shortAddr += "...";
            txtAddress.setText(shortAddr);
        } else txtAddress.setText(address);
        txtTotalTime.setText(totalTime);
        txtViewTime.setText(viewTime);
    }

    private void initGetView(View convertView){
        imgProfile = convertView.findViewById(R.id.img_profile_photo_transport_item);
        txtName = convertView.findViewById(R.id.txt_name_transport_item);
        txtAddress = convertView.findViewById(R.id.txt_address_transport_item);
        txtTotalTime = convertView.findViewById(R.id.txt_total_time_transport_item);
        txtViewTime = convertView.findViewById(R.id.txt_view_time_transport_item);
        linearTransportBar = convertView.findViewById(R.id.linear_transport_bar_transport_item);
    }

    private void createViewBar(){
        //TODO: view동적으로 추가하는 기능
    }
}
