package com.line360.loginprojectah.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.line360.loginprojectah.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 15/03/2017.
 */

public class HallAdapter extends ArrayAdapter<Hall> {


    public HallAdapter(Context context, ArrayList<Hall> hallArrayList) {
        super(context,0, hallArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View lisItemView = convertView;

        if (lisItemView == null) {
            lisItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Hall currentHall = getItem(position);
        ViewHolder viewHolder = (ViewHolder)lisItemView.getTag();

        if (viewHolder==null){
            viewHolder = new ViewHolder();
            viewHolder.txName = (TextView) lisItemView.findViewById(R.id.tx_name);
            viewHolder.txPrice = (TextView) lisItemView.findViewById(R.id.tx_price);
            viewHolder.txAddress = (TextView) lisItemView.findViewById(R.id.tx_address);
            viewHolder.hallImage = (ImageView) lisItemView.findViewById(R.id.hall_image);
            lisItemView.setTag(viewHolder);
        }

        viewHolder.txName.setText(currentHall.getmName());
        viewHolder.txPrice.setText(currentHall.getmPrice());
        viewHolder.txAddress.setText(currentHall.getmAddress());



        if (!(currentHall.getmUrlOfImage1().toString() == "")){
            Picasso.with(getContext())
                    .load(currentHall.getmUrlOfImage1().toString())
                    .into(viewHolder.hallImage);
    }


        return lisItemView;
    }
    class ViewHolder {
        TextView txName;
        TextView txPrice;
        TextView txAddress;
        ImageView hallImage;


    }
}


