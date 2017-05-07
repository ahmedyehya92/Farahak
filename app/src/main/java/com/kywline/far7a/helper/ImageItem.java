package com.kywline.far7a.helper;

/**
 * Created by Ahmed Yehya on 25/03/2017.
 */

public class ImageItem {
    public static  boolean ISTHEREIMAGE = false;

    private String urlOfimage;

    public ImageItem(String image) {

        this.urlOfimage = image;
        ISTHEREIMAGE = true;
    }

    public String getImage() {

        return urlOfimage;


    }


}
