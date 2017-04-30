package com.kywline.far7a.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kywline.far7a.R;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 22/04/2017.
 */

public class GovAdapter extends ArrayAdapter<Gov> {

    Context context;
    public GovAdapter(Context context, ArrayList<Gov> govesArrayList) {
        super(context,0, govesArrayList);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View lisItemView = convertView;

        if (lisItemView == null) {
            lisItemView = LayoutInflater.from(getContext()).inflate(R.layout.gov_list_item, parent, false);
        }
        Gov currentGov = getItem(position);
        GovAdapter.ViewHolder viewHolder = (GovAdapter.ViewHolder)lisItemView.getTag();

        if (viewHolder==null){
            viewHolder = new GovAdapter.ViewHolder();
            viewHolder.txGovernorate = (TextView) lisItemView.findViewById(R.id.tx_gov);
            lisItemView.setTag(viewHolder);
        }


        viewHolder.txGovernorate.setText(currentGov.getGovernorate());






        return lisItemView;
    }
    class ViewHolder {
        TextView txGovernorate;



    }

}
