package com.yelp.android.data.model.response;


import java.util.ArrayList;

public class BusinessDetailResponse {
    public String price;
    public String name;
    public ArrayList<BusinessHours> hours;
    public ArrayList photos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<BusinessHours> getHours() {
        return hours;
    }

    public void setHours(ArrayList<BusinessHours> hours) {
        this.hours = hours;
    }

    public ArrayList getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList photos) {
        this.photos = photos;
    }


}
