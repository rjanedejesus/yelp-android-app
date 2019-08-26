package com.yep.android.features.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.BindView;
import com.yep.android.R;
import com.yep.android.features.base.BaseActivity;
import com.yep.android.features.common.ErrorView;
import com.yep.android.injection.component.ActivityComponent;

public class MainActivity extends BaseActivity implements MainMvpView {

    @Inject
    MainPresenter mainPresenter;

    @BindView(R.id.view_error)
    ErrorView errorView;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

    }


    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        mainPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        mainPresenter.detachView();
    }

    @Override
    protected void initComponents() {

    }


    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showToast(int message) {

    }

    @Override
    public void showError(Throwable error) {

    }

}
