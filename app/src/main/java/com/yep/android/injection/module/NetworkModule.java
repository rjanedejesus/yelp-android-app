package com.yep.android.injection.module;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by shivam on 29/5/17.
 */
@Module
public class NetworkModule {

    private final Context context;
    private final String baseUrl;
    private String token;

    public NetworkModule(final Context context, String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }

    public NetworkModule(final Context context, String baseUrl, String token) {
        this.context = context;
        this.baseUrl = baseUrl;
        this.token = token;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient.Builder okHttpClient, Gson gson, AuthenticationRequestInterceptor authInterceptor) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // add session headers interceptor
        okHttpClient.addInterceptor(authInterceptor);

        // add logging as last interceptor
        okHttpClient.addInterceptor(logging);

        // adjust read timeout
        okHttpClient.readTimeout(60, TimeUnit.SECONDS);

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides @Singleton
    public OkHttpClient.Builder provideOkHttpClient()
    {
        return new OkHttpClient.Builder();
    }

    @Provides @Singleton
    public AuthenticationRequestInterceptor provideRequestInterceptor()
    {
        AuthenticationRequestInterceptor interceptor = new AuthenticationRequestInterceptor();
        interceptor.setToken(token);
        return interceptor;
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor(message -> Timber.d(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
    ChuckInterceptor provideChuckInterceptor() {
        return new ChuckInterceptor(context);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

}
