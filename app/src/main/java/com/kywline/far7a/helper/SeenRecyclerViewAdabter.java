package com.kywline.far7a.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kywline.far7a.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 07/05/2017.
 */

public class SeenRecyclerViewAdabter extends
        RecyclerView.Adapter<SeenRecyclerViewHolder>{

    private ArrayList<TopSeenModel> arrayList;
    private Context context;

    public SeenRecyclerViewAdabter(Context context,
                                   ArrayList<TopSeenModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(SeenRecyclerViewHolder holder, int position) {
        final TopSeenModel model = arrayList.get(position);

        SeenRecyclerViewHolder mainHolder = (SeenRecyclerViewHolder) holder;// holder


        // bitmap

        // setting title
        Picasso.with(context)
                .load(model.getImage_url().toString())
                .placeholder(R.drawable.placeholder)
                .into(mainHolder.image);

        mainHolder.tx_name.setText(model.getName());
        mainHolder.address.setText(model.getAddress());
        mainHolder.price.setText(model.getPrice() + "/" + "جنيه");
        mainHolder.seenNumber.setText(model.getSeenNumber()+ "/" + "مشاهدة");



    }

    @Override
    public SeenRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.top_seen_item, viewGroup, false);
        SeenRecyclerViewHolder listHolder = new SeenRecyclerViewHolder(mainGroup,context,arrayList);
        return listHolder;

    }

}
