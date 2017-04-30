package com.kywline.far7a.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public String getDate () throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        String dateInString = date;
        Date dateD = sdf.parse(dateInString);
        Date dateObject = new Date(dateD.getTime());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);
        return dateToDisplay;
    }

    public String getComment()
    {
        return comment;
    }

    public String getRate() {return rate;}
}
