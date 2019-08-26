package com.yelp.common.injection.component;

import com.yep.android.injection.component.AppComponent;
import com.yelp.common.injection.module.ApplicationTestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends AppComponent {
}
