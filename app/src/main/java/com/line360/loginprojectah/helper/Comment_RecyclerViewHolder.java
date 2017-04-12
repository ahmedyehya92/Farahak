package com.line360.loginprojectah.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.line360.loginprojectah.R;

/**
 * Created by Ahmed Yehya on 12/04/2017.
 */

public class Comment_RecyclerViewHolder extends RecyclerView.ViewHolder {
public TextView txUsername, txDate, txComment;

    public Comment_RecyclerViewHolder(View itemView) {
        super(itemView);

        this.txUsername = (TextView) itemView.findViewById(R.id.tx_username);
        this.txDate = (TextView) itemView.findViewById(R.id.tx_date_comment);
        this.txComment = (TextView) itemView.findViewById(R.id.tx_comment);
    }
}
