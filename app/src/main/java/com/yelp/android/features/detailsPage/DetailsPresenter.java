package com.yelp.android.features.detailsPage;


import com.yelp.android.data.DataManager;
import com.yelp.android.data.model.response.BusinessDetailResponse;
import com.yelp.android.data.remote.HttpRequestHelper;
import com.yelp.android.features.base.BasePresenter;
import com.yelp.android.injection.ConfigPersistent;


import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static timber.log.Timber.d;


@ConfigPersistent
public class DetailsPresenter extends BasePresenter<DetailsMvpView> {

    private final DataManager dataManager;
    private HttpRequestHelper httpRequest;

    @Inject
    public DetailsPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(DetailsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void getBusinessDetails(String id) {
        httpRequest = HttpRequestHelper.getInstance();
        httpRequest.api.getBusinessDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            d("DETAILS RESPONSE >> " + "|SUCCESSFUL|");
                            d("response.getName() >> " + response.getName());
                            d("response.getPrice() >> " + response.getPrice());


                        },
                        throwable -> {
                            d("DETAILS RESPONSE >> " + "|FAILED|");
                        });

    }





}
