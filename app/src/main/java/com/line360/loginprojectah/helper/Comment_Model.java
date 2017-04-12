package com.line360.loginprojectah.helper;

import android.renderscript.Short4;

/**
 * Created by Ahmed Yehya on 12/04/2017.
 */

public class Comment_Model {
    private String username;
    private String date;
    private String comment;


    public Comment_Model (String username, String date, String comment)
    {
        this.username = username;
        this.date = date;
        this.comment = comment;
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
}
