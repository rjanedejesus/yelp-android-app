package com.yelp.android.data.remote;

import com.yelp.android.data.model.response.Pokemon;
import com.yelp.android.data.model.response.PokemonListResponse;
import com.yelp.android.data.model.response.SearchedResponse;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface YelpService {

    @GET("/human_resource/liquidation/approval_list")
    Single<SearchedResponse> getSearchedList(@QueryMap HashMap<String, String> param);
}
