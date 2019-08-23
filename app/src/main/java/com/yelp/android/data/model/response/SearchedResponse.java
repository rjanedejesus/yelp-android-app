package com.yelp.android.data.model.response;


import java.util.ArrayList;

public class SearchedResponse {
    public ArrayList<SearchedItem> businesses;


    public ArrayList<SearchedItem> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(ArrayList<SearchedItem> businesses) {
        this.businesses = businesses;
    }




}
