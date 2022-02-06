package com.amirhosseinemadi.neumorphism;

import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class Utilities {

    public static float dpToPx(DisplayMetrics metrics, float value)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
    }

}
