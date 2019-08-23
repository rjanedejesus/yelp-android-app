package com.yelp.android.features.searchBy;

import com.yelp.android.data.model.response.SearchedItem;
import com.yelp.android.features.base.MvpView;

import java.util.List;

public interface SearchByMvpView extends MvpView {

    void showProgress(boolean show);
    void callDetailPage();
    void showRecyclerView();
    void hideRecyclerView();
    void setSearchedList(List<SearchedItem> searchedList);

}
