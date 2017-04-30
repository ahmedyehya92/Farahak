package com.kywline.far7a.helper;

/**
 * Created by Ahmed Yehya on 15/03/2017.
 */

public class Hall {
    private Integer mId;
    private String mName;
    private Integer mPrice;
    private String mPhone;
    private String mFacebook;
    private String mInstagram;
    private String mTwitter;
    private String mPreparing;
    private String mUrlOfImage1;
    private String mUrlOfImage2;
    private String mUrlOfImage3;
    private String mAddress;
    private Integer mSee;

    public Hall(int id, String name, int price, String phone,String facebook,String instagram,String twitter,String preparing, String urlOfImage1, String urlOfImage2,String urlOfImage3, String address, int see)
    {
        mId = id;
        mName = name;
        mPrice = price;
        mPhone = phone;
        mFacebook = facebook;
        mInstagram = instagram;
        mPreparing = preparing;
        mTwitter = twitter;
        mUrlOfImage1 = urlOfImage1;
        mUrlOfImage2 = urlOfImage2;
        mUrlOfImage3 = urlOfImage3;
        mAddress = address;
        mSee = see;
    }

    public String getmId() {return mId.toString();}
    public String getmName() {return mName;}
    public String getmPrice() {return mPrice.toString();}
    public String getmPhone() {return mPhone;}
    public String getmFacebook() {return mFacebook;}
    public String getmInstagram() {return mInstagram;}
    public String getmPreparing() {return mPreparing;}
    public String getmTwitter() {return mTwitter;}
    public String getmUrlOfImage1() {return mUrlOfImage1;}
    public String getmUrlOfImage2() {return mUrlOfImage2;}
    public String getmUrlOfImage3() {return mUrlOfImage3;}
    public String getmAddress(){return mAddress;}
    public String getmSee() {return mSee.toString();}


}
