package com.yep.android.data.model.response;


import java.util.ArrayList;

public class SearchedResponse {
    public ArrayList<SearchedItem> businesses;
    public int total;


    public ArrayList<SearchedItem> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(ArrayList<SearchedItem> businesses) {
        this.businesses = businesses;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }




}
