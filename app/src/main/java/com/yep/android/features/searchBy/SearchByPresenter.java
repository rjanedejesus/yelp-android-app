package com.yep.android.features.searchBy;

import android.annotation.SuppressLint;

import com.yep.android.data.DataManager;
import com.yep.android.data.model.response.SearchedItem;
import com.yep.android.data.remote.HttpRequestHelper;
import com.yep.android.features.base.BasePresenter;
import com.yep.android.injection.ConfigPersistent;

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

    @SuppressLint("CheckResult")
    public void getSearchedListCurrentLocation(double longitude, double latitude, String term) {
        httpRequest = HttpRequestHelper.getInstance();
        checkViewAttached();
        getView().showProgress(true);
        dataManager.setCurrentLocationLongitude(String.valueOf(longitude));
        dataManager.setCurrentLocationLatitude(String.valueOf(latitude));
        httpRequest.api.getListFromCurrentLocation(longitude, latitude, term)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            getView().showProgress(false);
                            d("getSearchedListCurrentLocation RESPONSE >> " + "|SUCCESSFUL|");
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

                            d("response.getTotal() >> " + response.getTotal());
                            if (response.getTotal() == 0){
                                getView().hideRecyclerView();
                            }

                        },
                        throwable -> {
                            d("getSearchedListCurrentLocation RESPONSE >> " + "|FAILED|");
                            getView().showProgress(false);
                        });

    }

    @SuppressLint("CheckResult")
    public void getSearchedListInputLocation(String location, String term) {
        httpRequest = HttpRequestHelper.getInstance();
        checkViewAttached();
        getView().showProgress(true);
        httpRequest.api.getListFromInputLocation(location, term)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            getView().showProgress(false);
                            d("getSearchedListInputLocation RESPONSE >> " + "|SUCCESSFUL|");
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

                            d("response.getTotal() >> " + response.getTotal());
                            if (response.getTotal() == 0){
                                getView().hideRecyclerView();
                            }


                        },
                        throwable -> {
                            d("getSearchedListInputLocation RESPONSE >> " + "|FAILED|");
                            getView().showProgress(false);
                            getView().hideRecyclerView();
                        });

    }

    @SuppressLint("CheckResult")
    public void sortListCurrentLocation(double longitude, double latitude, String term, String sort_by) {
        httpRequest = HttpRequestHelper.getInstance();
        checkViewAttached();
        getView().showProgress(true);
        httpRequest.api.sortListFromCurrentLocationList(longitude, latitude, term, sort_by)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            getView().showProgress(false);
                            d("sortListCurrentLocation RESPONSE >> " + "|SUCCESSFUL|");
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

                            d("response.getTotal() >> " + response.getTotal());
                            if (response.getTotal() == 0){
                                getView().hideRecyclerView();
                            }

                        },
                        throwable -> {
                            d("getSearchedListCurrentLocation RESPONSE >> " + "|FAILED|");
                            getView().showProgress(false);
                        });

    }

    @SuppressLint("CheckResult")
    public void sortListTypedLocation(String location, String term, String sort_by) {
        httpRequest = HttpRequestHelper.getInstance();
        checkViewAttached();
        getView().showProgress(true);
        httpRequest.api.sortListFromInputLocationList(location, term, sort_by)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            getView().showProgress(false);
                            d("sortListCurrentLocation RESPONSE >> " + "|SUCCESSFUL|");
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

                            d("response.getTotal() >> " + response.getTotal());
                            if (response.getTotal() == 0){
                                getView().hideRecyclerView();
                            }

                        },
                        throwable -> {
                            d("getSearchedListCurrentLocation RESPONSE >> " + "|FAILED|");
                            getView().showProgress(false);
                        });

    }

    public double getCurrentLocationLatitude() {
        return Double.parseDouble(dataManager.getCurrentLocationLatitude());
    }

    public double getCurrentLocationLongitude() {
        return Double.parseDouble(dataManager.getCurrentLocationLongitude());
    }




}
