package com.yelp.android.util.font;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {
    private static Typeface montserratRegular = null;
    private static Typeface montserratSemiBold = null;
    private static Typeface circularStdBold = null;

    public static Typeface getMontserratRegular(Context context) {
        if (montserratRegular == null) {
            montserratRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
        }
        return montserratRegular;
    }

    public static Typeface getMontserratSemiBold(Context context) {
        if (montserratSemiBold == null) {
            montserratSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-SemiBold.ttf");
        }
        return montserratSemiBold;
    }

    public static Typeface getCircularStdBold(Context context) {
        if (circularStdBold == null) {
            circularStdBold = Typeface.createFromAsset(context.getAssets(), "fonts/CircularStd-Bold.otf");
        }
        return circularStdBold;
    }

}
