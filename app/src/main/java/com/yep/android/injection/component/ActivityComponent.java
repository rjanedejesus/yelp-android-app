package com.yep.android.injection.component;

import dagger.Subcomponent;

import com.yep.android.features.detailsPage.DetailsActivity;
import com.yep.android.features.detailsPage.MapsActivity;
import com.yep.android.features.main.MainActivity;
import com.yep.android.features.searchBy.SearchByActivity;
import com.yep.android.injection.PerActivity;
import com.yep.android.injection.module.ActivityModule;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SearchByActivity searchByActivity);

    void inject(DetailsActivity detailsActivity);

    void inject(MapsActivity mapsActivity);
}
