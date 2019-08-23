package com.yelp.android.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gson.Gson;
import com.yelp.android.data.model.response.SearchedResponse;
import com.yelp.android.data.model.response.User;
import com.yelp.android.data.remote.YelpService;
import com.yelp.android.data.local.PreferencesHelper;

import java.util.HashMap;

import io.reactivex.Single;

import static timber.log.Timber.d;

@Singleton
public class DataManager {
    
    private YelpService yelpService;
    private PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(YelpService yelpService, PreferencesHelper preferencesHelper) {
        this.yelpService = yelpService;
        this.preferencesHelper = preferencesHelper;
    }

    /************ API Actions ************/




    /************ Shared Preferences Actions ************/

    public String getToken() {
        d("Token on DataManager : " + preferencesHelper.getString(PreferencesHelper.TOKEN));
        return preferencesHelper.getString(PreferencesHelper.TOKEN);
    }

    public void setToken(String token) {
        preferencesHelper.putString(PreferencesHelper.TOKEN, token);
    }

    public User getCurrentUser() {
        String user = preferencesHelper.getString(PreferencesHelper.USER);
        if(user == null || user.isEmpty()) return null;
        return new Gson().fromJson(user, User.class);
    }

    public void setCurrentUser(User user) {
        String userJson;
        if(user == null) userJson = null;
        else userJson = new Gson().toJson(user);
        preferencesHelper.putString(PreferencesHelper.USER, userJson);
    }

}
