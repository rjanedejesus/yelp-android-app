package com.yelp.android.data.remote;

import android.util.Log;

import com.yelp.android.config.Config;
import com.yelp.android.config.Constant;
import com.yelp.android.data.model.response.BusinessDetailResponse;
import com.yelp.android.data.model.response.SearchedResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

        //Get searched list
//        @GET("/search")
//        Observable<SearchedResponse> getList(@Query("page[number]") int pageNumber, @Query("page[size]") int pageSize, @Query("sort_by") String sortType);
//        String location, double longitude, double latitude, String term

        //Get searched list
        @GET("search")
        Observable<SearchedResponse> getList(@Query("location") String location, @Query("longitude") double longitude, @Query("latitude") double latitude, @Query("term") String term);

        //business details
        @GET("{id}")
        Observable<BusinessDetailResponse> getBusinessDetails(@Path("id") String id);


    }
}