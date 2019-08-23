package com.yelp.android.features.detailsPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


import com.yelp.android.R;
import com.yelp.android.features.base.BaseActivity;
import com.yelp.android.injection.component.ActivityComponent;



import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static android.util.Log.d;


public class DetailsActivity extends BaseActivity implements DetailsMvpView {

    @Inject
    DetailsPresenter presenter;

    // UI references.
    @BindView(R.id.toolbar_back)
    ImageView mBack;

    @BindView(R.id.business_name) public TextView businessName;
    @BindView(R.id.category) public TextView categories;
    @BindView(R.id.location) public TextView location;
    @BindView(R.id.rating_bar) public RatingBar ratingBar;
    @BindView(R.id.review_count) public TextView reviewCount;
    @BindView(R.id.display_phone) public TextView displayPhone;

    @OnClick(R.id.toolbar_back) void back() {
        finish();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        presenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        presenter.detachView();
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this code removed the default back arrow of toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }

        //getIntent
        String name = getIntent().getStringExtra("name");
        String category = getIntent().getStringExtra("categories");
        String completeLocation = getIntent().getStringExtra("location");
        String rating = getIntent().getStringExtra("rating");
        String review_count = getIntent().getStringExtra("review_count");
        String display_phone = getIntent().getStringExtra("display_phone");
        String id = getIntent().getStringExtra("id");
        String businessId = id;

        presenter.getBusinessDetails(businessId);

        //set values
        businessName.setText(name);
        categories.setText(category);
        location.setText(completeLocation);
        ratingBar.setRating(Float.parseFloat(rating));
        reviewCount.setText(review_count);
        if (!display_phone.equals("")){
            displayPhone.setText("Call " + display_phone);
        } else {
            displayPhone.setVisibility(View.GONE);
        }



    }




    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showToast(int message) {
        showMessage(message);
    }


    @Override
    public void showError(Throwable error) {


    }


}

