package com.yep.android.features.searchBy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yep.android.R;
import com.yep.android.data.model.response.SearchedItem;
import com.yep.android.features.base.BaseActivity;
import com.yep.android.injection.component.ActivityComponent;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static timber.log.Timber.d;

/**
 * main screen for searching business based on location, term
 */
public class SearchByActivity extends BaseActivity implements SearchByMvpView, LocationListener {

    private String apiLocation;
    private String term;
    LocationManager locationManager;
    private double currentLocationLatitude;
    private double currentLocationLongitude;
    private BottomSheetDialog sortBottomSheet;
    private RadioButton mSortRatingRb;
    private RadioButton mSortDistanceRb;
    private boolean isSortedBy = false;
    private boolean isSortByRating =  false;
    private boolean isSortByDistance =  false;
    private String businessLocationTxt;
    private String currentLocationTxt = "Current Location";

    @Inject
    SearchByPresenter presenter;

    @Inject
    SearchByAdapter searchByAdapter;
    private CustomTextWatcher customTextWatcher;
    private ProgressDialog progressDialog;

    // UI references.
    @BindView(R.id.search_business_name_toggle)
    ToggleButton mSearchBusinessName;
    @BindView(R.id.search_address_toggle)
    ToggleButton mSearchbusinessLocationTxt;
    @BindView(R.id.search_cuisine_toggle)
    ToggleButton mSearchBusinessCuisine;
    @BindView(R.id.content_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.no_item_text)
    TextView noItemText;
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
    @BindView(R.id.sort_layout)
    LinearLayout sortLayout;

    @OnClick(R.id.search_button)
    void searchButtonClicked() {
        searchBusiness();
    }

    @OnClick(R.id.current_location_layout)
    void currentLocationSelected() {
        apiLocation = currentLocationTxt;
        if (!isLocationEnabled(this)){
            showSettingsAlert();
        } else {
            mSearchLocationEdittext.setText(currentLocationTxt);
            mSearchLocationEdittext.setTextColor(getResources().getColor(R.color.blue));
            mCurrentLocationLayout.setVisibility(GONE);
        }

    }


    @OnClick(R.id.sort_layout)
    void sortLayout() {
        sortBottomSheet.show();
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

        setupSortBottomSheet();

        //permission check
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

//        currentLocationLatitude = presenter.getCurrentLocationLatitude();
//        currentLocationLongitude = presenter.getCurrentLocationLongitude();


        //show alert if current location is not yet known
        if (!isLocationEnabled(this)){
            showSettingsAlert();
        }

        getCurrentLocation();
        customTextWatcher = new CustomTextWatcher();
        mSearchBusinessEdittext.addTextChangedListener(customTextWatcher);
        mSearchbusinessLocationTxt.addTextChangedListener(customTextWatcher);

        mSearchBusinessEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSearchLocationLayout.setVisibility(View.VISIBLE);
                    mCurrentLocationLayout.setVisibility(View.VISIBLE);
                } else {
                    mSearchLocationLayout.setVisibility(GONE);
                    mCurrentLocationLayout.setVisibility(GONE);
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
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                mSearchLocationLayout.setVisibility(GONE);
                mCurrentLocationLayout.setVisibility(GONE);

            }
        });

        mSearchBusinessEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchBusiness();
                    return true;
                }
                return false;
            }
        });

        mSearchLocationEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchBusiness();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
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
        noItemText.setVisibility(GONE);
        sortLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRecyclerView() {
        recyclerView.setVisibility(GONE);
        noItemText.setVisibility(View.VISIBLE);
        sortLayout.setVisibility(GONE);
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

    public void searchBusiness(){
        getCurrentLocation();
        searchByAdapter.clearData();
        hideKeyboard(this);
        mSearchLocationLayout.setVisibility(GONE);
        mCurrentLocationLayout.setVisibility(GONE);
        mSearchBusinessEdittext.clearFocus();

        if (mSearchLocationEdittext.getText().toString().equals("")){
            Toast.makeText(this, "Please select location.", Toast.LENGTH_SHORT).show();
        } else {
            apiLocation = mSearchLocationEdittext.getText().toString();
        }

        term = mSearchBusinessEdittext.getText().toString();
        String sortBy = "";

        if (isSortedBy){
            if (isSortByRating){
                sortBy = "rating";
            } else if (isSortByDistance){
                sortBy = "distance";
            }
        }

        d("apiLocation >> " + apiLocation);
        d("isSortedBy >> " + isSortedBy);
        d("isSortByRating >> " + isSortByRating);
        d("isSortByDistance >> " + isSortByDistance);

        if (apiLocation!= null && apiLocation.equalsIgnoreCase(currentLocationTxt)){
            if (isSortedBy){
                presenter.sortListCurrentLocation(currentLocationLongitude, currentLocationLatitude, term, sortBy);
            } else {
                presenter.getSearchedListCurrentLocation(currentLocationLongitude, currentLocationLatitude, term);
            }

        } else {
            if (isSortedBy){
                presenter.sortListTypedLocation(apiLocation, term, sortBy);
            } else {
                presenter.getSearchedListInputLocation(apiLocation, term);
            }
        }
    }

    public void hideSearchLocationEditText() {
        mSearchLocationLayout.setVisibility(GONE);
        mCurrentLocationLayout.setVisibility(GONE);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu? ");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
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
            businessLocationTxt = mSearchLocationEdittext.getText().toString();

        }
    }

    private void setupSortBottomSheet() {
        sortBottomSheet = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        sortBottomSheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sortBottomSheet.setContentView(sheetView);
        mSortRatingRb = (RadioButton) sheetView.findViewById(R.id.rating_rb);
        mSortDistanceRb = (RadioButton) sheetView.findViewById(R.id.distance_rb);



        mSortRatingRb.setOnClickListener(v -> {
           sortBottomSheet.dismiss();
            businessLocationTxt = mSearchLocationEdittext.getText().toString();
            d("businessLocationTxt >> " +businessLocationTxt);
            isSortedBy = true;
            isSortByRating = true;
            if (businessLocationTxt.equalsIgnoreCase(currentLocationTxt)){
                presenter.sortListCurrentLocation(currentLocationLongitude, currentLocationLatitude, term, "rating");
            } else {
                presenter.sortListTypedLocation(businessLocationTxt, term, "rating");
            }
        });

        mSortDistanceRb.setOnClickListener(v -> {
            sortBottomSheet.dismiss();
            businessLocationTxt = mSearchLocationEdittext.getText().toString();
            d("businessLocationTxt >> " +businessLocationTxt);
            isSortedBy = true;
            isSortByDistance = true;
            if (businessLocationTxt.equalsIgnoreCase(currentLocationTxt)){
                presenter.sortListCurrentLocation(currentLocationLongitude, currentLocationLatitude, term, "distance");
            } else {
                presenter.sortListTypedLocation(businessLocationTxt, term, "distance");
            }

        });




    }

    public static Boolean isLocationEnabled(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }

    void getCurrentLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
            }
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        d("current location" + "Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        currentLocationLatitude = location.getLatitude();
        currentLocationLongitude = location.getLongitude();

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            d("current location : " +addresses.get(0).getAddressLine(0)+ ", " +
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        } catch(Exception e) { }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(SearchByActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

}

