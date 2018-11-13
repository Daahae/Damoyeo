package com.daahae.damoyeo.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Transport;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.activity.MapsActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TransportAdapter extends BaseAdapter {

    private ImageView imgProfile;
    private TextView txtName,txtAddress,txtTotalTime,txtViewTime;
    private List<TransportInfoList.Data> mItems;
    private ArrayList<Person> people;
    private LinearLayout linearTransportBar;
    private Context context;
    public TransportAdapter(List<TransportInfoList.Data> list, ArrayList<Person> people,Context context){
        mItems = list;
        this.people = people;
        this.context = context;
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

        setBuildingListText(null,person.getName(),formAddress(person.getAddress()),formTakenTime(myItem.getTotalTime()),strViewTime);

        createViewBar(myItem);
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

    private String formAddress(String address){
        String[] words = address.split("\\s");
        String formAddress="";
        boolean isEnter=false;
        for(String str:words){
            formAddress += str+" ";
            if(formAddress.length()>10&&isEnter==false){
                formAddress +="\n";
                isEnter=true;
            }
        }
        return formAddress;
    }

    private String formTakenTime(int time){
        String strTime;

        int timeHour = time/60;
        int timeMin = time%60;

        if(timeMin==0){
            strTime = timeHour+"시간";
        }else if(timeHour==0){
            strTime = timeMin+"분";
        }else{
            strTime = timeHour+"시간"+timeMin + "분";
        }

        return strTime;
    }

    private String formTime(int time){
        int timeHour = time/60;
        int timeMin = time%60;
        String strTime;
        if(timeHour==12){
            strTime = "오후"+timeHour+"시"+timeMin + "분";
        }
        else if(timeHour>12){
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
        txtAddress.setText(address);
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

    private void createViewBar(TransportInfoList.Data myItem){

        int totalLength = Constant.displayWidth;
        int size = myItem.getTransportInfo().size();
        double partOfLength = (double)totalLength/myItem.getTotalTime();

        Log.v("사이즈",size+"");
        for(int i=0;i<size;i++) {
            Transport transport = myItem.getTransportInfo().get(i);

            int width = (int)partOfLength*transport.getSectionTime();
            if(width<100) width=100;
            else if(width>400) width -= 50;
            Log.v("width",width+"");
            TextView txt = new TextView(context);
            txt.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT));
            if(transport.getTrafficType()==Constant.SUBWAY) txt.setBackgroundResource(R.color.colorGreen);
            else if(transport.getTrafficType()==Constant.BUS) txt.setBackgroundResource(R.color.colorOrange);
            txt.setTextColor(context.getResources().getColor(R.color.colorWhite));
            txt.setGravity(Gravity.CENTER);
            txt.setTextSize(13);
            txt.setText(formTakenTime(transport.getSectionTime()));
            linearTransportBar.addView(txt);
        }

    }


}
