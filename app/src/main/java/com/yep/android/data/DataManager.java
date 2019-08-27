package com.yep.android.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gson.Gson;
import com.yep.android.data.model.response.User;
import com.yep.android.data.remote.YelpService;
import com.yep.android.data.local.PreferencesHelper;

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

    /************ Shared Preferences Actions ************/

    public String getToken() {
        d("Token on DataManager : " + preferencesHelper.getString(PreferencesHelper.TOKEN));
        return preferencesHelper.getString(PreferencesHelper.TOKEN);
    }

    public void setToken(String token) {
        preferencesHelper.putString(PreferencesHelper.TOKEN, token);
    }

    public String getCurrentLocationLatitude() {
        d("CURRENT_LOCATION_LATITUDE on DataManager : " + preferencesHelper.getString(PreferencesHelper.CURRENT_LOCATION_LATITUDE));
        return preferencesHelper.getString(PreferencesHelper.CURRENT_LOCATION_LATITUDE);
    }

    public void setCurrentLocationLatitude(String latitude) {
        preferencesHelper.putString(PreferencesHelper.CURRENT_LOCATION_LATITUDE, latitude);
    }

    public String getCurrentLocationLongitude() {
        d("CURRENT_LOCATION_LONGITUDE on DataManager : " + preferencesHelper.getString(PreferencesHelper.CURRENT_LOCATION_LONGITUDE));
        return preferencesHelper.getString(PreferencesHelper.CURRENT_LOCATION_LONGITUDE);
    }

    public void setCurrentLocationLongitude(String longitude) {
        preferencesHelper.putString(PreferencesHelper.CURRENT_LOCATION_LONGITUDE, longitude);
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
