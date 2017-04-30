package com.kywline.far7a.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kywline.far7a.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 25/03/2017.
 */

public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerViewImages> {

    private ArrayList<ImageItem> arrayList;
    private Context context;

    public RecyclerView_Adapter(Context context,
                                ArrayList<ImageItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewImages holder, int position) {
        final ImageItem model = arrayList.get(position);

        RecyclerViewImages mainHolder = (RecyclerViewImages) holder;// holder

        Picasso.with(context)
                .load(model.getImage().toString())
                .placeholder(R.drawable.placeholder_large)
                .into(mainHolder.imageview);
    }


    @Override
    public RecyclerViewImages onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.images_list_item, viewGroup, false);
        RecyclerViewImages listHolder = new RecyclerViewImages(mainGroup);
        return listHolder;

    }


}
