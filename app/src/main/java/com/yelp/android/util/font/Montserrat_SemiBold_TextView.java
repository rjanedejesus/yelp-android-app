package com.yelp.android.util.font;

import android.content.Context;
import android.util.AttributeSet;

public class Montserrat_SemiBold_TextView extends android.support.v7.widget.AppCompatTextView {

    public Montserrat_SemiBold_TextView(Context context) {
        super(context);
        setTypeface(Fonts.getMontserratSemiBold(context));
    }
    public Montserrat_SemiBold_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Fonts.getMontserratSemiBold(context));
    }
    public Montserrat_SemiBold_TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Fonts.getMontserratSemiBold(context));
    }

}
