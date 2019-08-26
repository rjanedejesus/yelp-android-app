package com.yep.android.util.font;

import android.content.Context;
import android.util.AttributeSet;

public class CircularStdBold_Button extends android.support.v7.widget.AppCompatButton {

    public CircularStdBold_Button(Context context) {
        super(context);
        setTypeface(Fonts.getCircularStdBold(context));
    }
    public CircularStdBold_Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Fonts.getCircularStdBold(context));
    }
    public CircularStdBold_Button(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Fonts.getCircularStdBold(context));
    }

}
