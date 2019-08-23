package com.yelp.android.util.font;

import android.content.Context;
import android.util.AttributeSet;

public class CircularStdBold_Edittext extends android.support.v7.widget.AppCompatEditText {

    public CircularStdBold_Edittext(Context context) {
        super(context);
        setTypeface(Fonts.getCircularStdBold(context));
    }
    public CircularStdBold_Edittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Fonts.getCircularStdBold(context));
    }
    public CircularStdBold_Edittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Fonts.getCircularStdBold(context));
    }

}
