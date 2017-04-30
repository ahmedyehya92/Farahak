package com.kywline.far7a.helper;

/**
 * Created by Ahmed Yehya on 22/04/2017.
 */

public class Gov {

    private String mId;
    private String mGovernorate;


    public Gov ( String id, String governorate) {

        mId = id;
        mGovernorate = governorate;

    }

    public String getCityId() {return mId;}
    public String getGovernorate() {return mGovernorate;}
}
