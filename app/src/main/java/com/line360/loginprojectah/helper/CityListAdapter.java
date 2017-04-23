package com.line360.loginprojectah.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.line360.loginprojectah.R;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 22/04/2017.
 */

public class CityListAdapter extends ArrayAdapter<CityList> {

    Context context;
    public CityListAdapter(Context context, ArrayList<CityList> citiesArrayList) {
        super(context,0, citiesArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View lisItemView = convertView;

        if (lisItemView == null) {
            lisItemView = LayoutInflater.from(getContext()).inflate(R.layout.cities_list_item, parent, false);
        }
        CityList currentCity = getItem(position);
        CityListAdapter.ViewHolder viewHolder = (CityListAdapter.ViewHolder)lisItemView.getTag();

        if (viewHolder==null){
            viewHolder = new CityListAdapter.ViewHolder();
            viewHolder.txCity = (TextView) lisItemView.findViewById(R.id.tx_cit);
            lisItemView.setTag(viewHolder);
        }

        viewHolder.txCity.setText(currentCity.getCity());







        return lisItemView;
    }
    class ViewHolder {
        TextView txCity;



    }
}
