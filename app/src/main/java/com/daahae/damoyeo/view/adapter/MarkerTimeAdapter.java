package com.daahae.damoyeo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.MarkerTime;
import com.daahae.damoyeo.presenter.NMapActivityPresenter;
import com.daahae.damoyeo.presenter.SelectMidFragmentPresenter;

import java.util.ArrayList;

public class MarkerTimeAdapter extends BaseAdapter{

    private TextView txtMarkerName;
    private TextView txtMarkerTime;

    private MarkerTime myItem;
    private ArrayList<MarkerTime> mItems;

    public MarkerTimeAdapter(){
        this.mItems = new ArrayList<MarkerTime>();
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
    public MarkerTime getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void add(MarkerTime markerTime){
        mItems.add(markerTime);
    }

    public void add(String name, String totalTime){mItems.add(new MarkerTime(name, totalTime));}

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_time_taken_marker, parent, false);
        }
        initGetView(convertView);

        myItem = (MarkerTime) mItems.get(position);

        //TODO: GetTotal 수정
        setMarkerListText(txtMarkerName,txtMarkerTime,myItem.getName(),myItem.getTotalTime());

        return convertView;
    }

    private void initGetView(View convertView){
        txtMarkerName = (TextView)convertView.findViewById(R.id.txt_marker_name_item);
        txtMarkerTime = (TextView)convertView.findViewById(R.id.txt_marker_time_about_mid_item);
    }

    private void setMarkerListText(TextView MarkerNameView, TextView MarkerTime, String nameText, String timeText){
        MarkerNameView.setText(nameText);
        MarkerTime.setText(timeText);
    }
}