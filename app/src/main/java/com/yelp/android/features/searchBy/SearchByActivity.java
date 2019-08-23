package com.yelp.android.features.searchBy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yelp.android.R;
import com.yelp.android.data.model.response.SearchedItem;
import com.yelp.android.features.base.BaseActivity;
import com.yelp.android.injection.component.ActivityComponent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static timber.log.Timber.d;

/**
 * screen where you chose what category to search
 */
public class SearchByActivity extends BaseActivity implements SearchByMvpView{

    private String apiLocation;
    private double apiLocationLongitude;
    private double apiLocationLatitude;
    private String apiTerm;
    boolean userTouchedSearchBusinessView;

    @Inject
    SearchByPresenter presenter;

    @Inject SearchByAdapter searchByAdapter;
    private CustomTextWatcher customTextWatcher;
    private ProgressDialog progressDialog;

    // UI references.
    @BindView(R.id.search_business_name_toggle)
    ToggleButton mSearchBusinessName;
    @BindView(R.id.search_address_toggle) ToggleButton mSearchBusinessLocation;
    @BindView(R.id.search_cuisine_toggle) ToggleButton mSearchBusinessCuisine;
    @BindView(R.id.content_layout) DrawerLayout drawer_layout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.no_item_text) TextView noItemText;
    @BindView(R.id.search_business_edittext)
    EditText mSearchBusinessEdittext;
    @BindView(R.id.search_location_edittext)
    EditText mSearchLocationEdittext;
    @BindView(R.id.current_location_text)
    TextView mCurrentLocationText;
    @BindView(R.id.search_button)
    ImageView mSearchButton;
    @BindView(R.id.search_location_layout)
    LinearLayout mSearchLocationLayout;
    @BindView(R.id.current_location_layout)
    LinearLayout mCurrentLocationLayout;
    @BindView(R.id.container_layout)
    LinearLayout mContainerLayout;

    @OnClick(R.id.search_button) void searchButtonClicked() {
        searchByAdapter.clearData();
        hideKeyboard(this);
        mSearchLocationLayout.setVisibility(View.GONE);
        mCurrentLocationLayout.setVisibility(View.GONE);
        mSearchBusinessEdittext.clearFocus();

        apiLocation = mSearchLocationEdittext.getText().toString();
        apiTerm = mSearchBusinessEdittext.getText().toString();
        presenter.getSearchedList(apiLocation, apiLocationLongitude, apiLocationLatitude, apiTerm);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_search_by;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searchByAdapter);
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

        customTextWatcher=new CustomTextWatcher();
        mSearchBusinessEdittext.addTextChangedListener(customTextWatcher);

        mSearchBusinessEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSearchLocationLayout.setVisibility(View.VISIBLE);
                    mCurrentLocationLayout.setVisibility(View.VISIBLE);
                } else {
                    mSearchLocationLayout.setVisibility(View.GONE);
                    mCurrentLocationLayout.setVisibility(View.GONE);
                }
            }
        });

        mSearchLocationEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSearchLocationLayout.setVisibility(View.VISIBLE);
                    mCurrentLocationLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the input method manager
                InputMethodManager inputMethodManager = (InputMethodManager)
                        view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                // Hide the soft keyboard
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                mSearchLocationLayout.setVisibility(View.GONE);
                mCurrentLocationLayout.setVisibility(View.GONE);

            }
        });

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
    public void callDetailPage() {
//        callActivity(BottomNavigationActivity.class, null);
//        finish();
    }

    @Override
    public void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        noItemText.setVisibility(View.GONE);
    }

    @Override
    public void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
        noItemText.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSearchedList(List<SearchedItem> searchedListItems) {
        searchByAdapter.setExpenses(searchedListItems);
    }

    @Override
    public void showToast(int message) {
        showMessage(message);
    }


    @Override
    public void showError(Throwable error) {

    }

    public void hideSearchLocationEditText(){
        mSearchLocationLayout.setVisibility(View.GONE);
        mCurrentLocationLayout.setVisibility(View.GONE);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }



    class CustomTextWatcher implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable search) {


        }
    }

}

