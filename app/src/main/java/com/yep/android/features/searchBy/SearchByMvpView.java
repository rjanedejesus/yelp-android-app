package com.yep.android.features.searchBy;

import com.yep.android.data.model.response.SearchedItem;
import com.yep.android.features.base.MvpView;

import java.util.List;

public interface SearchByMvpView extends MvpView {

    void showProgress(boolean show);
    void callDetailPage();
    void showRecyclerView();
    void hideRecyclerView();
    void setSearchedList(List<SearchedItem> searchedList);

}
