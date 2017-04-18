package com.line360.loginprojectah.helper;

import android.renderscript.Short4;

/**
 * Created by Ahmed Yehya on 12/04/2017.
 */

public class Comment_Model {
    private String username;
    private String date;
    private String comment;
    private String rate;


    public Comment_Model (String username, String date, String comment, String rate)
    {
        this.username = username;
        this.date = date;
        this.comment = comment;
        this.rate = rate;
    }

    public String getUsername() {
        return username;
    }

    public String getDate ()
    {
        return date;
    }

    public String getComment()
    {
        return comment;
    }

    public String getRate() {return rate;}
}
