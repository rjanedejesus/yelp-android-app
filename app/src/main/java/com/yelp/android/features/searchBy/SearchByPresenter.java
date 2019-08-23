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
    public void getSearchedList(String location) {
        httpRequest = HttpRequestHelper.getInstance();
//        getView().refreshView(true);
//        httpRequest.api.getList(location, 7, sortType)
        httpRequest.api.getList(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
////                            getView().dismissProgressDialog();
////                            getView().refreshView(false);
                            d("SEARCH RESPONSE >> " + "|SUCCESSFUL|");
                            d("response.businesses.toString() >> " + response.businesses.toString());

                            searchedItemArrayList.clear();
                            if (response.getBusinesses() != null){
                                for (SearchedItem searchedItem : response.getBusinesses()) {
                                    searchedItemArrayList.add(searchedItem);
//                                jobArrayList.add(data);
                                    d("searchedItem.name >> " + searchedItem.name);
                                    d("searchedItem.categories.title >> " + searchedItem.categories.get(0).title);
                                    d("searchedItem.rating >> " + searchedItem.rating);
                                    d("searchedItem.review_count >> " + searchedItem.review_count);
                                    //  address1 + ", " + city + ", " + state + " " + zipcode
                                    String address1, city, state, zipcode;
                                    address1 = searchedItem.location.address1;
                                    city = searchedItem.location.city;
                                    state = searchedItem.location.state;
                                    zipcode = searchedItem.location.zip_code;
                                    d("searchedItem.location >> " + address1 + ", " + city + ", " + state + " " + zipcode);
                                    getView().setSearchedList(searchedItemArrayList);
                                    getView().showRecyclerView();
                                }
                            } else {
                                getView().hideRecyclerView();
                            }

//                            getView().addDataFromPresenter(jobArrayList);
//                            jobArrayList.clear();
//                            pageCount = page;
//                            pageCount++;
//                            getView().refreshView(false);
//                            getView().incrementPage(pageCount);

                        },
                        throwable -> {
                            d("SEARCH RESPONSE >> " + "|FAILED|");
//                            getView().refreshView(false);
//                            getView().dismissProgressDialog();
//
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
