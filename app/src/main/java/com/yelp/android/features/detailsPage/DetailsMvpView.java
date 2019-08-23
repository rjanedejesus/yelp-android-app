package com.yelp.android.features.detailsPage;

import com.yelp.android.data.model.response.ImageUrl;
import com.yelp.android.features.base.MvpView;

import java.util.ArrayList;


public interface DetailsMvpView extends MvpView {

    void displayOperatingHours(String open, String close);
    void displayPrice(String price);
    void setupPage(ArrayList imageUrlList);

}
