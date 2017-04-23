package com.line360.loginprojectah.helper;

/**
 * Created by Ahmed Yehya on 22/04/2017.
 */

public class CityList {
    private String mId;
    private String mCity;

    public CityList(String id, String city) {

        mId = id;
        mCity = city;
    }

    public String getCityId() {return mId;}
    public String getCity() {return mCity;}
}
