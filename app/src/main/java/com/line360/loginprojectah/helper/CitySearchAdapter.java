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
 * Created by Ahmed Yehya on 19/04/2017.
 */

public class CitySearchAdapter extends ArrayAdapter<CitySearch> {

    Context context;
    public CitySearchAdapter(Context context, ArrayList<CitySearch> citiesArrayList) {
        super(context,0, citiesArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View lisItemView = convertView;

        if (lisItemView == null) {
            lisItemView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_item, parent, false);
        }
        CitySearch currentCitySearch = getItem(position);
        CitySearchAdapter.ViewHolder viewHolder = (CitySearchAdapter.ViewHolder)lisItemView.getTag();

        if (viewHolder==null){
            viewHolder = new CitySearchAdapter.ViewHolder();
            viewHolder.txCity = (TextView) lisItemView.findViewById(R.id.tx_city_search);
            viewHolder.txGovernorate = (TextView) lisItemView.findViewById(R.id.tx_governorate);
            lisItemView.setTag(viewHolder);
        }

        viewHolder.txCity.setText(currentCitySearch.getCity());
        viewHolder.txGovernorate.setText(currentCitySearch.getGovernorate());






        return lisItemView;
    }
    class ViewHolder {
        TextView txGovernorate;
        TextView txCity;



    }
}
