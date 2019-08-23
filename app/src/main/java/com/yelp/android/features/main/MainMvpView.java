package com.yelp.android.features.main;

import com.yelp.android.features.base.MvpView;

public interface MainMvpView extends MvpView {

    void showProgress(boolean show);

    void showError(Throwable error);
}
