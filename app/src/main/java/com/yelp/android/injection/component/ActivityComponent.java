package com.yelp.android.injection.component;

import dagger.Subcomponent;

import com.yelp.android.features.detailsPage.DetailsActivity;
import com.yelp.android.features.main.MainActivity;
import com.yelp.android.features.searchBy.SearchByActivity;
import com.yelp.android.injection.PerActivity;
import com.yelp.android.injection.module.ActivityModule;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SearchByActivity searchByActivity);

    void inject(DetailsActivity detailsActivity);
}
