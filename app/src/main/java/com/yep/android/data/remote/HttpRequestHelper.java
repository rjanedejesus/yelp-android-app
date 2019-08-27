package com.yep.android.data.remote;

import android.util.Log;

import com.yep.android.config.Config;
import com.yep.android.config.Constant;
import com.yep.android.data.model.response.BusinessDetailResponse;
import com.yep.android.data.model.response.SearchedResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public class HttpRequestHelper {

    private static HttpRequestHelper mInstance = null;
    public static String mType = "";
    public ApiList api;

    public HttpRequestHelper() {
        HttpLoggingInterceptor logHeader = new HttpLoggingInterceptor();
        HttpLoggingInterceptor logBody = new HttpLoggingInterceptor();

        logHeader.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        logBody.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        if (Config.isDebugMode()) {
            httpClient.addInterceptor(logHeader);
            httpClient.addInterceptor(logBody);
//        }

        httpClient.connectTimeout(100, TimeUnit.SECONDS);
        httpClient.readTimeout(100, TimeUnit.SECONDS);
        httpClient.writeTimeout(100, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String authorization = Config.isDevMode() ? Constant.NON_PROD_BASIC_AUTH : Constant.PROD_BASIC_AUTH;
                Log.d("Authorization", authorization);

                Request mainRequest = original.newBuilder()
                        .header("Authorization", authorization)
                        .method(original.method(), original.body())
                        .build();

                Response response;

                Log.i("HttpRequestHelper", "" + mainRequest.headers());
                Log.i("HttpRequestHelper", "" + authorization);

                response = chain.proceed(mainRequest);
                mType = "";

                return response;
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getServerUrl())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        api = retrofit.create(ApiList.class);
    }

    public HttpRequestHelper(final String version) {
        HttpLoggingInterceptor logHeader = new HttpLoggingInterceptor();
        HttpLoggingInterceptor logBody = new HttpLoggingInterceptor();

        logHeader.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        logBody.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (Config.isDebugMode()) {
            httpClient.addInterceptor(logHeader);
            httpClient.addInterceptor(logBody);
        }

        httpClient.connectTimeout(100, TimeUnit.SECONDS);
        httpClient.readTimeout(100, TimeUnit.SECONDS);
        httpClient.writeTimeout(100, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String authorization = Config.isDevMode() ? Constant.NON_PROD_BASIC_AUTH : Constant.PROD_BASIC_AUTH;
                Log.d("Authorization", authorization);

                Request mainRequest = original.newBuilder()
                        .header("Authorization", authorization)
//                        .header("Accept", "application/vnd.api+json; version="+version)
//                        .header("Content-Type", "application/vnd.api+json")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(mainRequest);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getServerUrl())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        api = retrofit.create(ApiList.class);
    }

    public static HttpRequestHelper getInstance() {
        if (mInstance == null || mType.equals("upload") || mType.equals("notUpload") || mType.equals("completable") || mType.equals("v1")) {
            mInstance = new HttpRequestHelper();
        }
        return mInstance;
    }

    public void setType(String type) {
        this.mType = type;
    }


    public interface ApiList {

        //using current location
        @GET("search")
        Observable<SearchedResponse> getListFromCurrentLocation(@Query("limit") int limit, @Query("longitude") double longitude, @Query("latitude") double latitude, @Query("term") String term);

        //using typed location
        @GET("search")
        Observable<SearchedResponse> getListFromInputLocation(@Query("limit") int limit,@Query("location") String location, @Query("term") String term);

        //using current location, sort_by
        @GET("search")
        Observable<SearchedResponse> sortListFromCurrentLocationList(@Query("limit") int limit,@Query("longitude") double longitude, @Query("latitude") double latitude, @Query("term") String term, @Query("sort_by") String sort_by);

        //using typed location, sort_by
        @GET("search")
        Observable<SearchedResponse> sortListFromInputLocationList(@Query("limit") int limit,@Query("location") String location, @Query("term") String term, @Query("sort_by") String sort_by);

//        //autocomplete businesses
//        @GET("autocomplete")
//        Observable<AutoCompleteResponse> autoCompleteBusiness(@Query("businesses") String businesses);


        //business details
        @GET("{id}")
        Observable<BusinessDetailResponse> getBusinessDetails(@Path("id") String id);


    }
}