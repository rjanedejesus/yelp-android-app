package com.yelp.android;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.yelp.android.BuildConfig;
import com.yelp.android.injection.component.DaggerAppComponent;
import com.singhajit.sherlock.core.Sherlock;
import com.squareup.leakcanary.LeakCanary;
import com.tspoon.traceur.Traceur;

import com.yelp.android.injection.component.AppComponent;
import com.yelp.android.injection.module.AppModule;
import com.yelp.android.injection.module.NetworkModule;

import timber.log.Timber;

public class YelpApplication extends Application {

    private AppComponent appComponent;
    private String token;

    public static YelpApplication get(Context context) {
        return (YelpApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
            LeakCanary.install(this);
            Sherlock.init(this);
            Traceur.enableLogging();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    public AppComponent getComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .networkModule(new NetworkModule(this, BuildConfig.YELP_API_URL))
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }

    public AppComponent getComponent(String token) {

        if(this.token == null || appComponent == null || token != this.token) {
            appComponent = DaggerAppComponent.builder()
                    .networkModule(new NetworkModule(this, BuildConfig.YELP_API_URL, token))
                    .appModule(new AppModule(this))
                    .build();
            this.token = token;
        }
        return appComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

}
