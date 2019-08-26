package com.yep.android.features.detailsPage;

import com.yep.android.features.base.MvpView;

import java.util.ArrayList;


public interface DetailsMvpView extends MvpView {

    void displayOperatingHours(String open, String close);
    void displayPrice(String price);
    void setupPage(ArrayList imageUrlList);

}
