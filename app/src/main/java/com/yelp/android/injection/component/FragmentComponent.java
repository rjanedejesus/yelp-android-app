package com.yelp.android.injection.component;

import dagger.Subcomponent;
import com.yelp.android.injection.PerFragment;
import com.yelp.android.injection.module.FragmentModule;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
}
