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
    private LinearLayout linearTransportBar, linearTransportItem;
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
        setBuildingListText(null,person.getName(),"주소",formTakenTime(myItem.getTotalTime()),strViewTime);

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
        linearTransportItem = convertView.findViewById(R.id.linear_transport_way_transport_item);
    }

    private void createViewBar(TransportInfoList.Data myItem){

        int totalLength = Constant.displayWidth;
        int size = myItem.getTransportInfo().size();
        double partOfLength = (double)totalLength/myItem.getTotalTime();

        Log.v("사이즈",size+"");

        addMargin();

        for(int i=0;i<size;i++) {
            Transport transport = myItem.getTransportInfo().get(i);

            int width = (int)partOfLength*transport.getSectionTime();
            width = getWidth(width,size);

            TextView txt = new TextView(context);
            txt.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT));
            if(transport.getTrafficType()==Constant.SUBWAY) txt.setBackgroundResource(R.color.colorGreen);
            else if(transport.getTrafficType()==Constant.BUS) txt.setBackgroundResource(R.color.colorOrange);
            txt.setTextColor(context.getResources().getColor(R.color.colorWhite));
            txt.setGravity(Gravity.CENTER);
            txt.setTextSize(12);
            if(transport.getSectionTime()==0) continue;
            txt.setText(formTakenTime(transport.getSectionTime()));
            linearTransportBar.addView(txt);
            Log.v("시간",formTakenTime(transport.getSectionTime()));

            if(transport.getTrafficType()==Constant.SUBWAY || transport.getTrafficType()==Constant.BUS) {
                TextView txt2 = new TextView(context);
                txt2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                txt2.setBackgroundResource(R.color.colorWhite);
                if (transport.getTrafficType() == Constant.SUBWAY)
                    txt2.setTextColor(context.getResources().getColor(R.color.colorGreen));
                else if (transport.getTrafficType() == Constant.BUS)
                    txt2.setTextColor(context.getResources().getColor(R.color.colorOrange));
                txt2.setGravity(Gravity.CENTER);
                txt2.setTextSize(12);
                txt2.setText(transport.getTransportNumber());
                linearTransportItem.addView(txt2);


                 ImageView img = new ImageView(context);
                 img.setLayoutParams(new LinearLayout.LayoutParams(50, ViewGroup.LayoutParams.MATCH_PARENT));
                 img.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                 img.setImageResource(R.drawable.ic_arrow_gray);
                 img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                 linearTransportItem.addView(img);
                 Log.v("화살표", "add");
            }

            if(i == size-1){
                TextView txt3 = new TextView(context);
                txt3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                txt3.setBackgroundResource(R.color.colorWhite);
                txt3.setTextColor(context.getResources().getColor(R.color.colorGray));
                txt3.setGravity(Gravity.CENTER);
                txt3.setTextSize(12);
                txt3.setText("목적지");
                linearTransportItem.addView(txt3);
            }
        }

    }

    private int getWidth(int width,int size) {

        if(width<100) width=100;
        else if(width>200&&size>=7) width -= 100;
        else if(width>300&&size>=6) width -= 100;
        else if(width>400&&size>=5) width -= 100;
        else if(width>500&&size>=4) width -=50;

        return width;
    }

    private void addMargin() {

        TextView margin = new TextView(context);
        margin.setLayoutParams(new LinearLayout.LayoutParams(30, LinearLayout.LayoutParams.MATCH_PARENT));
        margin.setBackgroundResource(R.color.colorWhite);
        linearTransportItem.addView(margin);
    }
}
