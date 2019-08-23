package com.yelp.android.config;


public class Config {
    private static final Boolean mDebugMode = true;
    private static final Boolean mDevMode = true;

    public static Boolean isDebugMode() {
        return mDebugMode;
    }
    public static Boolean isDevMode() {
        return mDevMode;
    }

    public static String getServerUrl() {
        return mDevMode ? Constant.NON_PROD : Constant.PROD;
    }

}