package com.kywline.far7a.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kywline.far7a.R;

/**
 * Created by Ahmed Yehya on 07/05/2017.
 */

public class SeenRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView tx_name;
    public ImageView image;
    public TextView price;
    public TextView address;
    public TextView seenNumber;





    public SeenRecyclerViewHolder(View view) {
        super(view);
        // Find all views ids

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

}
