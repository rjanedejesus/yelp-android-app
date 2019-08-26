package com.yep.android.injection.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.yep.android.data.remote.YelpService;

import retrofit2.Retrofit;


@Module(includes = {NetworkModule.class})
public class ApiModule {

    @Provides
    @Singleton
    YelpService provideYelpApi(Retrofit retrofit) {
        return retrofit.create(YelpService.class);
    }
}
