package com.yep.android.util.font;

import android.content.Context;
import android.util.AttributeSet;

public class Montserrat_Regular_Edittext extends android.support.v7.widget.AppCompatEditText {

    public Montserrat_Regular_Edittext(Context context) {
        super(context);
        setTypeface(Fonts.getMontserratRegular(context));
    }
    public Montserrat_Regular_Edittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Fonts.getMontserratRegular(context));
    }
    public Montserrat_Regular_Edittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Fonts.getMontserratRegular(context));
    }

}
