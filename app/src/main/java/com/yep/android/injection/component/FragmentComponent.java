package com.yep.android.injection.component;

import dagger.Subcomponent;
import com.yep.android.injection.PerFragment;
import com.yep.android.injection.module.FragmentModule;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
}
