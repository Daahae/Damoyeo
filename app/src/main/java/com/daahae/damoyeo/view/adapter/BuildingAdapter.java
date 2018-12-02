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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BuildingAdapter extends BaseAdapter{

    private TextView txtCompanyName,txtAboutAddress,txtDistance;
    private ImageView imgStar1, imgStar2, imgStar3, imgStar4, imgStar5;
    private TextView txtBuildingScore;
    private ArrayList<Building> mItems;

    public BuildingAdapter(){
        mItems = new ArrayList<Building>();
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
    public Building getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void add(Building building){
        mItems.add(building);
    }

    private final static Comparator<Building> sortScoreComparator= new Comparator<Building>() {
        @Override
        public int compare(Building object1, Building object2) {
            return Double.compare(object1.getRating(), object2.getRating());
        }
    };

    private final static Comparator<Building> sortDistanceComparator= new Comparator<Building>() {
        @Override
        public int compare(Building object1, Building object2) {
            return Double.compare(object1.getDistance(), object2.getDistance());
        }
    };

    public void sortScore(){
        Collections.sort(mItems, sortScoreComparator);
        Collections.reverse(mItems);
        notifyDataSetChanged();
    }
    public void sortDistance(){
        Collections.sort(mItems, sortDistanceComparator);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_category, parent, false);
        }

        initGetView(convertView);

        Building myItem = (Building) mItems.get(position);

        setAllScore();
        setBuildingListText(txtCompanyName, txtAboutAddress, txtDistance,txtBuildingScore, myItem.getName(),myItem.getBuildingAddress(),myItem.getDistance(), myItem.getRating());
        setBuildingScore(myItem.getRating());
        return convertView;
    }

    private void setBuildingScore(double score){
        if(score==5){
            imgStar1.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar2.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar3.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar4.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar5.setImageResource(R.drawable.ic_full_yellow_star);
        } else if(score>4 && score<5){
            imgStar1.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar2.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar3.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar4.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar5.setImageResource(R.drawable.ic_half_yellow_star);
        }else if(score==4){
            imgStar1.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar2.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar3.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar4.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar5.setImageResource(R.drawable.block_default);
        }else if(score>3 && score<4){
            imgStar1.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar2.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar3.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar4.setImageResource(R.drawable.ic_half_yellow_star);
            imgStar5.setImageResource(R.drawable.block_default);
        }else if(score==3){
            imgStar1.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar2.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar3.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar4.setImageResource(R.drawable.block_default);
            imgStar5.setImageResource(R.drawable.block_default);
        }else if(score>2 && score<3){
            imgStar1.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar2.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar3.setImageResource(R.drawable.ic_half_yellow_star);
            imgStar4.setImageResource(R.drawable.block_default);
            imgStar5.setImageResource(R.drawable.block_default);
        }else if(score==2){
            imgStar1.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar2.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar3.setImageResource(R.drawable.block_default);
            imgStar4.setImageResource(R.drawable.block_default);
            imgStar5.setImageResource(R.drawable.block_default);
        }else if(score>1 && score<2){
            imgStar1.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar2.setImageResource(R.drawable.ic_half_yellow_star);
            imgStar3.setImageResource(R.drawable.block_default);
            imgStar4.setImageResource(R.drawable.block_default);
            imgStar5.setImageResource(R.drawable.block_default);
        }else if(score==1){
            imgStar1.setImageResource(R.drawable.ic_full_yellow_star);
            imgStar2.setImageResource(R.drawable.block_default);
            imgStar3.setImageResource(R.drawable.block_default);
            imgStar4.setImageResource(R.drawable.block_default);
            imgStar5.setImageResource(R.drawable.block_default);
        }else if(score>0 && score<1){
            imgStar1.setImageResource(R.drawable.ic_half_yellow_star);
            imgStar2.setImageResource(R.drawable.block_default);
            imgStar3.setImageResource(R.drawable.block_default);
            imgStar4.setImageResource(R.drawable.block_default);
            imgStar5.setImageResource(R.drawable.block_default);
        }else if(score==0){
            imgStar1.setImageResource(R.drawable.block_default);
            imgStar2.setImageResource(R.drawable.block_default);
            imgStar3.setImageResource(R.drawable.block_default);
            imgStar4.setImageResource(R.drawable.block_default);
            imgStar5.setImageResource(R.drawable.block_default);
        }

    }

    private void setAllScore(){
        imgStar1.setVisibility(View.VISIBLE);
        imgStar2.setVisibility(View.VISIBLE);
        imgStar3.setVisibility(View.VISIBLE);
        imgStar4.setVisibility(View.VISIBLE);
        imgStar5.setVisibility(View.VISIBLE);
    }

    private void setBuildingListText(TextView companyName, TextView address, TextView distance, TextView score, String strName, String strAddr,double strDist, double lfScore){
        companyName.setText(strName);
        address.setText(strAddr);
        distance.setText(String.valueOf(strDist));
        score.setText(String.valueOf(lfScore)+" / 5.0");
    }

    private void initGetView(View convertView){
        txtCompanyName = (TextView)convertView.findViewById(R.id.txt_company_name_item);
        txtAboutAddress = (TextView)convertView.findViewById(R.id.txt_address_item);
        txtDistance = (TextView)convertView.findViewById(R.id.txt_distance_away_item);

        txtBuildingScore = (TextView)convertView.findViewById(R.id.txt_score_building_item);
        imgStar1 = (ImageView)convertView.findViewById(R.id.img_star1);
        imgStar2 = (ImageView)convertView.findViewById(R.id.img_star2);
        imgStar3 = (ImageView)convertView.findViewById(R.id.img_star3);
        imgStar4 = (ImageView)convertView.findViewById(R.id.img_star4);
        imgStar5 = (ImageView)convertView.findViewById(R.id.img_star5);
    }

}
