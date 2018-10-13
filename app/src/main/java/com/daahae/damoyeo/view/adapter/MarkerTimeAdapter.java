package com.daahae.damoyeo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.presenter.SelectMidFragmentPresenter;

import java.util.ArrayList;

public class MarkerTimeAdapter extends BaseAdapter{
    private SelectMidFragmentPresenter presenter;
    private ArrayList<TransportInfoList> mItems;

    public MarkerTimeAdapter(SelectMidFragmentPresenter presenter){
        this.presenter = presenter;
        this.mItems = new ArrayList<TransportInfoList>();
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
    public void addDummy(Person person){
        TransportInfoList dummy = new TransportInfoList(null, 10, person);
        mItems.add(dummy);
    }

    public void add(TransportInfoList transportInfoList){
        mItems.add(transportInfoList);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_time_taken_marker, parent, false);
        }

        TextView txtMarkerName = (TextView)convertView.findViewById(R.id.txt_marker_name_item);
        TextView txtMarkerTime = (TextView)convertView.findViewById(R.id.txt_marker_time_about_mid_item);

        final TransportInfoList myItem = (TransportInfoList) mItems.get(position);

        txtMarkerName.setText(String.valueOf(myItem.getPerson().getName()));
        txtMarkerTime.setText(String.valueOf(myItem.getTotalTime()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showSelectMaker(myItem.getPerson().getAddressPosition());
            }
        });

        return convertView;
    }
}
