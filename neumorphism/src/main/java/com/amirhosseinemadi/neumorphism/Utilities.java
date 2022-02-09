package com.amirhosseinemadi.neumorphism;

import android.util.DisplayMetrics;
import android.util.TypedValue;

public class Utilities {

    private final DisplayMetrics metrics;

    public Utilities(DisplayMetrics metrics)
    {
        this.metrics = metrics;
    }

    public float dpToPx(float value)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
    }

    public static float dpToPx(DisplayMetrics metrics, float value)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
    }

}
