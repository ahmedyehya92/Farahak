package com.kywline.far7a.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kywline.far7a.R;
import com.kywline.far7a.activities.HallActivity;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 07/05/2017.
 */

public class SeenRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tx_name;
    public ImageView image;
    public TextView price;
    public TextView address;
    public TextView seenNumber;



    ArrayList<TopSeenModel>seenNumberList = new ArrayList<TopSeenModel>();
    Context contextA;

    public SeenRecyclerViewHolder(View view, Context contextA, ArrayList<TopSeenModel> seenNumberList) {
        super(view);

        this.seenNumberList = seenNumberList;
        this.contextA = contextA;
        // Find all views ids
        view.setOnClickListener(this);

        this.tx_name = (TextView) view
                .findViewById(R.id.seen_name);
        this.image = (ImageView) view
                .findViewById(R.id.seen_image);
        this.price = (TextView) view
                .findViewById(R.id.seen_price);
        this.address = (TextView) view
                .findViewById(R.id.seen_address);
        this.seenNumber = (TextView) view
                .findViewById(R.id.seen_number);



    }

    @Override
    public void onClick(View v) {

        final String id_key = "idKey";
        int position = getAdapterPosition();
        TopSeenModel seenItem = this.seenNumberList.get(position);

        Intent intent = new Intent(this.contextA, HallActivity.class);
        intent.putExtra(id_key,seenItem.getId());
        this.contextA.startActivity(intent);


    }
}
