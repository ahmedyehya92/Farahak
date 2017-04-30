package com.kywline.far7a.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kywline.far7a.R;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 12/04/2017.
 */

public class Comment_RecyclerView_Adapter extends RecyclerView.Adapter<Comment_RecyclerViewHolder> {

    private ArrayList<Comment_Model> arrayList;
    private Context context;

    public Comment_RecyclerView_Adapter(Context context,
                                        ArrayList<Comment_Model> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(Comment_RecyclerViewHolder holder, int position) {
        final Comment_Model model = arrayList.get(position);

        Comment_RecyclerViewHolder mainHolder = (Comment_RecyclerViewHolder) holder;// holder


        // setting title
        mainHolder.txUsername.setText(model.getUsername());
        try {
            mainHolder.txDate.setText(model.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mainHolder.txComment.setText(model.getComment());
        mainHolder.txRate.setText(model.getRate());

    }

    @Override
    public Comment_RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.comment_item_row, viewGroup, false);
        Comment_RecyclerViewHolder listHolder = new Comment_RecyclerViewHolder(mainGroup);
        return listHolder;

    }
}
