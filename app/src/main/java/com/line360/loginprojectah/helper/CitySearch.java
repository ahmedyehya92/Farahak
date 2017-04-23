package com.line360.loginprojectah.helper;

/**
 * Created by Ahmed Yehya on 19/04/2017.
 */

public class CitySearch {

    private String mId;
    private String mGovernorate;
    private String mCity;

    public CitySearch(String id, String governorate, String city) {

        mId = id;
        mGovernorate = governorate;
        mCity = city;
    }

    public String getCityId() {return mId;}
    public String getCity() {return mCity;}
    public String getGovernorate() {return mGovernorate;}
}
