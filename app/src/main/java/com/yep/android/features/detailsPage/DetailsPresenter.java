package com.yep.android.features.detailsPage;


import com.yep.android.data.DataManager;
import com.yep.android.data.model.response.BusinessHours;
import com.yep.android.data.model.response.OperatingHours;
import com.yep.android.data.remote.HttpRequestHelper;
import com.yep.android.features.base.BasePresenter;
import com.yep.android.injection.ConfigPersistent;


import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static timber.log.Timber.d;


@ConfigPersistent
public class DetailsPresenter extends BasePresenter<DetailsMvpView> {

    private final DataManager dataManager;
    private HttpRequestHelper httpRequest;
    private ArrayList photoList;

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

    public void getBusinessDetails(int currentday, String id) {
        httpRequest = HttpRequestHelper.getInstance();
        getView().showProgress(true);
        httpRequest.api.getBusinessDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            d("DETAILS RESPONSE >> " + "|SUCCESSFUL|");
                            getView().showProgress(false);
                            getView().displayPrice(response.getPrice());
                            for (BusinessHours businessHours : response.getHours()) {
                                for (OperatingHours operatingHours : businessHours.getOperatingHours()) {
                                    if (operatingHours.day == currentday){
                                        getView().displayOperatingHours(operatingHours.start, operatingHours.end);
                                    }
                                }
                                d("response.getPhotos() >> " + response.getPhotos());
                                getView().setupPage(response.getPhotos());
                            }


                            //-- display data from API ---
                            //photo
                            //longitude, latitude
                        },
                        throwable -> {
                            d("DETAILS RESPONSE >> " + "|FAILED|");
                            getView().showProgress(false);
                        });

    }



}
