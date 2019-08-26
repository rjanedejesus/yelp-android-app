package com.yep.android.features.main;

import com.yep.android.features.base.MvpView;

public interface MainMvpView extends MvpView {

    void showProgress(boolean show);

    void showError(Throwable error);
}
