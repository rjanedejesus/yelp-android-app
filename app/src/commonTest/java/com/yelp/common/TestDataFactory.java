package com.yelp.common;

import com.yep.android.data.model.response.NamedResource;
import com.yep.android.data.model.response.Sprites;
import com.yep.android.data.model.response.Statistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Factory class that makes instances of data models with random field values. The aim of this class
 * is to help setting up test fixtures.
 */
public class TestDataFactory {

    private static final Random random = new Random();

    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }


}
