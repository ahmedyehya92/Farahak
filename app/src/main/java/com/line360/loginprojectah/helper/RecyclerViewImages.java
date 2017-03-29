package com.line360.loginprojectah.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.line360.loginprojectah.R;

/**
 * Created by Ahmed Yehya on 25/03/2017.
 */

public class RecyclerViewImages extends RecyclerView.ViewHolder {

    public ImageView imageview;

    public RecyclerViewImages(View view) {
        super(view);
        // Find all views ids


        this.imageview = (ImageView) view
                .findViewById(R.id.image_Items);


    }
}
