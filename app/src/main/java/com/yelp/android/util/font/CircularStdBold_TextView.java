package com.yelp.android.util.font;

import android.content.Context;
import android.util.AttributeSet;

public class CircularStdBold_TextView extends android.support.v7.widget.AppCompatTextView {

    public CircularStdBold_TextView(Context context) {
        super(context);
        setTypeface(Fonts.getCircularStdBold(context));
    }
    public CircularStdBold_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Fonts.getCircularStdBold(context));
    }
    public CircularStdBold_TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Fonts.getCircularStdBold(context));
    }

}
