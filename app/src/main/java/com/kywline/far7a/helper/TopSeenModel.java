package com.kywline.far7a.helper;

/**
 * Created by Ahmed Yehya on 07/05/2017.
 */

public class TopSeenModel {
    private String id;
    private String name;
    private String price;
    private String image_url;
    private String address;
    private String seenNumber;

    public TopSeenModel(String id,  String name, String price, String image_url, String address, String seenNumber) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.image_url = image_url;
        this.address = address;
        this.seenNumber = seenNumber;


    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getAddress() {
        return address;
    }

    public String getSeenNumber() {
        return seenNumber;
    }

}
