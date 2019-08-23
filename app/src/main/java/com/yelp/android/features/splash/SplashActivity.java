package com.yelp.android.features.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yelp.android.R;
import com.yelp.android.features.searchBy.SearchByActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Start home activity
        startActivity(new Intent(SplashActivity.this, SearchByActivity.class));
        // close splash activity
        finish();

    }

}
