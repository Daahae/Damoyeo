package com.daahae.damoyeo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.TransportInfoList;

import java.util.ArrayList;

public class MarkerTimeAdapter extends BaseAdapter{

    private ArrayList<TransportInfoList> mItems;

    public MarkerTimeAdapter(){
        mItems = new ArrayList<TransportInfoList>();
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
    public TransportInfoList getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //더미
    public void addDummy(){
        TransportInfoList dummy = new TransportInfoList(null, 10, 15011067);
        mItems.add(dummy);
    }

    public void add(TransportInfoList transportInfoList){
        mItems.add(transportInfoList);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_time_taken_marker, parent, false);
        }

        TextView txtMarkerName = (TextView)convertView.findViewById(R.id.txt_marker_name_item);
        TextView txtMarkerTime = (TextView)convertView.findViewById(R.id.txt_marker_time_about_mid_item);

        TransportInfoList myItem = (TransportInfoList) mItems.get(position);

        txtMarkerName.setText(String.valueOf(myItem.getPersonID()));
        txtMarkerTime.setText(String.valueOf(myItem.getTotalTime()));

        return convertView;
    }

}
