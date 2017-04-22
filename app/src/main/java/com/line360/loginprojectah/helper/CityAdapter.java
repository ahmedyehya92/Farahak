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
import com.line360.loginprojectah.ui.CustomTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 19/04/2017.
 */

public class CityAdapter extends ArrayAdapter<City> {

    Context context;
    public CityAdapter(Context context, ArrayList<City> citiesArrayList) {
        super(context,0, citiesArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View lisItemView = convertView;

        if (lisItemView == null) {
            lisItemView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_item, parent, false);
        }
        City currentCity = getItem(position);
        CityAdapter.ViewHolder viewHolder = (CityAdapter.ViewHolder)lisItemView.getTag();

        if (viewHolder==null){
            viewHolder = new CityAdapter.ViewHolder();
            viewHolder.txCity = (TextView) lisItemView.findViewById(R.id.tx_city_search);
            viewHolder.txGovernorate = (TextView) lisItemView.findViewById(R.id.tx_governorate);
            lisItemView.setTag(viewHolder);
        }

        viewHolder.txCity.setText(currentCity.getCity());
        viewHolder.txGovernorate.setText(currentCity.getGovernorate());






        return lisItemView;
    }
    class ViewHolder {
        TextView txGovernorate;
        TextView txCity;



    }
}
