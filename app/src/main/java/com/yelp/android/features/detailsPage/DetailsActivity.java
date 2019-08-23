package com.yelp.android.features.detailsPage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


import com.yelp.android.R;
import com.yelp.android.data.model.response.ImageUrl;
import com.yelp.android.features.base.BaseActivity;
import com.yelp.android.injection.component.ActivityComponent;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static timber.log.Timber.d;


public class DetailsActivity extends BaseActivity implements DetailsMvpView {

    private int current_day;
    private ProgressDialog progressDialog;
    private String[] urlArray;

    @Inject
    DetailsPresenter presenter;
    
    @Inject
    ImagesAdapter imagesAdapter;

    // UI references.
    @BindView(R.id.toolbar_back)
    ImageView mBack;

    @BindView(R.id.recyclerView_images)
    RecyclerView recyclerViewImages;
    
    @BindView(R.id.business_name) public TextView businessName;
    @BindView(R.id.category) public TextView categories;
    @BindView(R.id.location) public TextView location;
    @BindView(R.id.rating_bar) public RatingBar ratingBar;
    @BindView(R.id.review_count) public TextView reviewCount;
    @BindView(R.id.display_phone) public TextView displayPhone;
    @BindView(R.id.opening_hour) public TextView openingHour;
    @BindView(R.id.closing_hour) public TextView closingHour;
    @BindView(R.id.price) public TextView price;

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

        //set values
        businessName.setText(name);
        categories.setText(category);
        location.setText(completeLocation);
        ratingBar.setRating(Float.parseFloat(rating));
        reviewCount.setText(review_count + " Reviews");
        if (!display_phone.equals("")){
            displayPhone.setText("Call " + display_phone);
        } else {
            displayPhone.setVisibility(View.GONE);
        }

        presenter.getBusinessDetails(currentDay(), id);

        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setAdapter(imagesAdapter);

    }

    @Override
    public void showProgress(boolean show) {
        if (show){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }

    }

    @Override
    public void showToast(int message) {
        showMessage(message);
    }


    @Override
    public void showError(Throwable error) {


    }

    private int currentDay(){
        Date currentDate = Calendar.getInstance().getTime();
        String dayOfTheWeek = (String) DateFormat.format("EEE", currentDate);

        if (dayOfTheWeek.equals("Mon")){
            current_day = 0;
        } else if (dayOfTheWeek.contains("Tue")){
            current_day = 1;
        } else if (dayOfTheWeek.contains("Wed")){
            current_day = 2;
        } else if (dayOfTheWeek.contains("Thu")){
            current_day = 3;
        } else if (dayOfTheWeek.contains("Fri")){
            current_day = 4;
        } else if (dayOfTheWeek.contains("Sat")){
            current_day = 5;
        } else if (dayOfTheWeek.contains("Sun")){
            current_day = 6;
        }

        return current_day;

    }


    @Override
    public void displayOperatingHours(String open, String close) {
        String open24hr_part1 = "";
        String open24hr_part2 = "";

        //split the string into 2 parts
        //convert to 24 hr format
        open24hr_part1 = open.substring(0,2);
        open24hr_part2 = open.substring(2,4);

        int open24hr_hour = Integer.parseInt(open24hr_part1);
        int open24hr_minute = Integer.parseInt(open24hr_part2);
        String openingTime =  ((open24hr_hour > 12) ? open24hr_hour % 12 : open24hr_hour) + ":" + (open24hr_minute < 10 ? ("0" + open24hr_minute) : open24hr_minute) + " " + ((open24hr_hour >= 12) ? "PM" : "AM");
        openingHour.setText("Opens " + openingTime);

        String close24hr_part1 = "";
        String close24hr_part2 = "";

        //split the string into 2 parts
        //convert to 24 hr format
        close24hr_part1 = close.substring(0,2);
        close24hr_part2 = close.substring(2,4);

        int close24hr_hour = Integer.parseInt(close24hr_part1);
        int close24hr_minute = Integer.parseInt(close24hr_part2);
        String closingTime =  ((close24hr_hour > 12) ? close24hr_hour % 12 : close24hr_hour) + ":" + (close24hr_minute < 10 ? ("0" + close24hr_minute) : close24hr_minute) + " " + ((close24hr_hour >= 12) ? "PM" : "AM");
        closingHour.setText("Closes " + closingTime);


    }

    @Override
    public void displayPrice(String priceTxt) {
        price.setText(priceTxt);
    }

    @Override
    public void setupPage(ArrayList imageUrlArrayList) {

        String [] strArrayUrl = (String[]) imageUrlArrayList.toArray(new String[0]);;
        ArrayList imageUrlList = new ArrayList<>();
        for (int i = 0; i < strArrayUrl.length; i++) {
            ImageUrl imageUrl = new ImageUrl();
            imageUrl.setImageUrl(strArrayUrl[i]);
            imageUrlList.add(imageUrl);
        }

        imagesAdapter.setImageUrls(getApplicationContext(), imageUrlList);
        if (imageUrlList.size() > 0){
            recyclerViewImages.setAdapter(imagesAdapter);
        } else {
            recyclerViewImages.setVisibility(View.GONE);
        }
    }


}

