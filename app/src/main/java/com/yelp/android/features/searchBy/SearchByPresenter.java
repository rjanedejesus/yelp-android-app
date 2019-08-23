package com.yelp.android.features.searchBy;

import android.content.Context;
import android.util.Log;

import com.yelp.android.data.DataManager;
import com.yelp.android.data.model.response.SearchedItem;
import com.yelp.android.data.model.response.SearchedResponse;
import com.yelp.android.data.remote.HttpRequestHelper;
import com.yelp.android.features.base.BasePresenter;
import com.yelp.android.injection.ConfigPersistent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static timber.log.Timber.d;

@ConfigPersistent
public class SearchByPresenter extends BasePresenter<SearchByMvpView> {

    private final DataManager dataManager;
    private HttpRequestHelper httpRequest;
    private List<SearchedItem> searchedItemArrayList = new ArrayList<>();

    @Inject
    public SearchByPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

//    /**
//     * Call search business api. If successfull, fetch the business list based on the params.
//     * Proceed with displaying the list after fetching the items
//     * @param
//     * @param
//     * @param
//     */
//    public void getSearchedList(final int page, String location, String sortType) {
    public void getSearchedList(String location, double longitude, double latitude, String term) {
        httpRequest = HttpRequestHelper.getInstance();
//        httpRequest.api.getList(location, 7, sortType)
        checkViewAttached();
        getView().showProgress(true);
        httpRequest.api.getList(location, longitude, latitude, term)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            getView().showProgress(false);
                            d("SEARCH RESPONSE >> " + "|SUCCESSFUL|");
                            searchedItemArrayList.clear();
                            if (response.getBusinesses() != null){
                                for (SearchedItem searchedItem : response.getBusinesses()) {
                                    searchedItemArrayList.add(searchedItem);
                                    getView().setSearchedList(searchedItemArrayList);
                                    getView().showRecyclerView();
                                }
                            } else {
                                getView().hideRecyclerView();
                            }


                        },
                        throwable -> {
                            d("SEARCH RESPONSE >> " + "|FAILED|");
                            getView().showProgress(false);
//                            // TODO - Handle api returned errors here
//                            Response resp = ((SSRetrofitException) throwable).getResponse();
//                            if (resp == null) {
//                                getView().showToast(R.string.error_server);
//                                return;
//                            }
//                            Integer code = resp.raw().code();
//                            if (code == 422) {
//                                getView().dismissProgressDialog();
//                                getView().showToast(R.string.error_something_went_wrong);
//                            } else {
//                                getView().showToast(R.string.error_server);
//                            }
                        });

    }

}
