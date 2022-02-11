package com.amirhosseinemadi.neumorphism.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import androidx.annotation.ColorRes;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import com.amirhosseinemadi.neumorphism.NeumorphismImpl;
import com.amirhosseinemadi.neumorphism.R;
import com.amirhosseinemadi.neumorphism.Utilities;

public class NeuButton extends AppCompatButton implements NeumorphismImpl {

    private final DisplayMetrics metrics;
    private final Utilities utilities;
    private boolean isInitDrawn;

    private RectF backgroundRect;
    private Paint lightPaint;
    private Paint darkPaint;

    private float neuElevation;
    private float neuRadius;
    private int backgroundColor;
    private int lightShadowColor;
    private int darkShadowColor;

    public NeuButton(@NonNull Context context) {
        super(context);
        metrics = getContext().getResources().getDisplayMetrics();
        utilities = new Utilities(metrics);

        neuElevation = utilities.dpToPx(6f);
        neuRadius = utilities.dpToPx(8f);
        backgroundColor = R.color.md_grey_100;
        lightShadowColor = R.color.white;
        darkShadowColor = R.color.md_grey_300;

        init(neuElevation,neuRadius,backgroundColor,lightShadowColor,darkShadowColor);
    }

    public NeuButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        metrics = getContext().getResources().getDisplayMetrics();
        utilities = new Utilities(metrics);

        TypedArray attrArray = context.obtainStyledAttributes(attrs,R.styleable.NeuButton);
        neuElevation = attrArray.getDimension(R.styleable.NeuButton_neu_elevation,(int) utilities.dpToPx(6f));
        neuRadius = attrArray.getDimension(R.styleable.NeuButton_neu_radius,utilities.dpToPx(8f));
        backgroundColor = attrArray.getColor(R.styleable.NeuButton_neu_background_color,ContextCompat.getColor(getContext(),R.color.md_grey_100));
        lightShadowColor = attrArray.getColor(R.styleable.NeuButton_neu_light_shadow_color,ContextCompat.getColor(getContext(),R.color.white));
        darkShadowColor = attrArray.getColor(R.styleable.NeuButton_neu_dark_shadow_color,ContextCompat.getColor(getContext(),R.color.md_grey_300));
        attrArray.recycle();

        init(neuElevation,neuRadius,backgroundColor,lightShadowColor,darkShadowColor);
    }


    private void init(float elevation, float radius, int backgroundColor, int lightShadowColor, int darkShadowColor)
    {
        backgroundRect = new RectF();
        lightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        darkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setNeuElevation(elevation);
        setNeuRadius(radius);
        setBackgroundColor(backgroundColor);
        setLightShadowColor(lightShadowColor);
        setDarkShadowColor(darkShadowColor);
        setPaddingRelative(getPaddingStart(),getPaddingTop(),getPaddingEnd(),getPaddingBottom());
    }


    /**
     * This function calculates compound drawables width plus drawable padding for each drawable
     * <br>
     * Note : currently not used in the view
     * @return compound drawables needed width with drawable padding
     */
    private int calculateDrawableWidth()
    {
        int width = 0;
        Drawable[] relativeDrawable = getCompoundDrawablesRelative();

        for (int i = 0; i < relativeDrawable.length; i++)
        {
            if (relativeDrawable[i] != null)
            {
                if (i % 2 == 0)
                {
                    width += relativeDrawable[i].getMinimumWidth() + getCompoundDrawablePadding();
                }else
                {
                    if (relativeDrawable[1] != null && relativeDrawable[3] != null)
                    {
                        width += Math.max(relativeDrawable[1].getMinimumWidth()
                                ,relativeDrawable[3].getMinimumWidth());
                    }else
                    {
                        if (relativeDrawable[i] != null)
                        {
                            width += relativeDrawable[i].getMinimumWidth();
                        }
                    }
                }
            }
        }

        return width;
    }


    /**
     * This function calculates compound drawables height plus drawable padding for each drawable
     * <br>
     * Note : currently not used in the view
     * @return compound drawables needed height with drawable padding
     */
    private int calculateDrawableHeight()
    {
        int height = 0;
        Drawable[] relativeDrawable = getCompoundDrawablesRelative();

        for (int i = 0; i < relativeDrawable.length; i++)
        {
            if (relativeDrawable[i] != null)
            {
                if (i % 2 != 0)
                {
                    height += relativeDrawable[i].getMinimumHeight() + getCompoundDrawablePadding();
                }else
                {
                    if (relativeDrawable[0] != null && relativeDrawable[2] != null)
                    {
                        height += Math.max(relativeDrawable[1].getMinimumHeight()
                                ,relativeDrawable[3].getMinimumHeight());
                    }else
                    {
                        if (relativeDrawable[i] != null)
                        {
                            height += relativeDrawable[i].getMinimumHeight();
                        }
                    }
                }
            }
        }

        return height;
    }


    /**
     * This function calculates suggested width for the view by contents
     * <br>
     * Note : currently not used in the view
     * @param metrics DisplayMetrics
     * @param textBounds A Rect object which filled by Paint class getTextBound()
     * @param neuElevation Elevation of the view
     * @return suggested width for the view
     */
    private int calculateMinWidth(DisplayMetrics metrics, Rect textBounds, int neuElevation)
    {
        int width = 0;

        if (textBounds.width() > 0)
        {
            int masterWidth = (int) (textBounds.width() + utilities.dpToPx(4) + calculateDrawableWidth() + getCompoundDrawablePadding() + neuElevation*5);

            if (masterWidth < getSuggestedMinimumWidth() + neuElevation*5)
            {
                width = getSuggestedMinimumWidth() + neuElevation*5;
            }else
            {
                width = Math.min(masterWidth, metrics.widthPixels);
            }
        }else
        {
            int drawableWidth = calculateDrawableWidth();

            if (drawableWidth > 0)
            {
                width = drawableWidth + neuElevation*5 + getPaddingStart() + getPaddingEnd();
            }else
            {
                if (getPaddingStart() + getPaddingEnd() < getSuggestedMinimumWidth())
                {
                    width = getSuggestedMinimumWidth() + neuElevation*5;
                }else
                {
                    width = getSuggestedMinimumWidth() + neuElevation*5 + getPaddingStart() + getPaddingEnd();
                }
            }
        }

        return width;
    };


    /**
     * This function calculates suggested height for the view by contents
     * <br>
     * Note : currently not used in the view
     * @param metrics DisplayMetrics
     * @param textBounds A Rect object which filled by Paint class getTextBound()
     * @param neuElevation Elevation of the view
     * @return suggested height for the view
     */
    private int calculateMinHeight(DisplayMetrics metrics, Rect textBounds, int neuElevation)
    {
        int height = 0;

        if (getText().length() > 0)
        {
            int masterLineWidth = (int) (metrics.widthPixels - neuElevation*5 - utilities.dpToPx(4) - calculateDrawableWidth());

            if (textBounds.width() > masterLineWidth)
            {
                int masterLineHeight = 0;
                int masterLines = textBounds.width() / masterLineWidth;

                for (int i = 0; i < masterLines; i++)
                {
                    masterLineHeight += getLineHeight();
                }

                height = getSuggestedMinimumHeight() + neuElevation*5 + masterLineHeight;
                height = Math.min(height,metrics.heightPixels);

            }else
            {
                height = getSuggestedMinimumHeight() + neuElevation*5;
            }
        }else
        {
            int drawableHeight = calculateDrawableHeight();

            if (drawableHeight > 0)
            {
                height = drawableHeight + neuElevation*5 + getPaddingTop() + getPaddingBottom();
            }else
            {
                if (getPaddingTop() + getPaddingBottom() < getSuggestedMinimumHeight())
                {
                    height = getSuggestedMinimumHeight() + neuElevation*5;
                }else
                {
                    height = getSuggestedMinimumHeight() + neuElevation*5 + getPaddingStart() + getPaddingEnd();
                }
            }
        }

        return height;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (calculateDrawableWidth() <= 0)
        {
            setMinWidth((int) (getSuggestedMinimumWidth() + neuElevation*5));
            setMinHeight((int) (getSuggestedMinimumHeight() + neuElevation*5));

        }else if ((getText() != null && getText().length() > 0) || (getHint() != null && getHint().length() > 0))
        {
            setMinWidth((int) (getSuggestedMinimumWidth() + neuElevation*5));
            setMinHeight((int) (getSuggestedMinimumHeight() + neuElevation*5));
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        backgroundRect.set(0 + neuElevation * 2.7f
                , 0 + neuElevation * 2.7f
                , getWidth() - neuElevation * 2.7f
                , getHeight() - neuElevation * 2.7f);
    }


    /**
     * This function draw neumorphism background for the view
     * @param canvas
     */
    private void drawBackground(Canvas canvas)
    {
        lightPaint.setStyle(Paint.Style.FILL);
        lightPaint.setColor(backgroundColor);
        lightPaint.setShadowLayer(neuElevation*2.1f,-neuElevation,-neuElevation,lightShadowColor);

        darkPaint.setStyle(Paint.Style.FILL);
        darkPaint.setColor(backgroundColor);
        darkPaint.setShadowLayer(neuElevation*2.1f,+neuElevation,+neuElevation,darkShadowColor);

        canvas.drawRoundRect(backgroundRect,neuRadius,neuRadius, lightPaint);
        canvas.drawRoundRect(backgroundRect,neuRadius,neuRadius, darkPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        isInitDrawn = true;
        canvas.clipRect(backgroundRect);
        super.onDraw(canvas);
    }


    /**
     *This method use for setting background color for view and does not apply shape drawable.<br>
     * <br>
     *note : this view does not support gradient background color.
     * @param background background drawable for view
     */
    @Override
    @SuppressLint("ResourceType")
    public void setBackground(Drawable background) {
        if (background instanceof ColorDrawable)
        {
            setNeuBackgroundColor(((ColorDrawable) background).getColor());
        }else if (background instanceof ShapeDrawable)
        {
            setNeuBackgroundColor(((ShapeDrawable) background).getPaint().getColor());
        }
    }

    @Override
    public Drawable getBackground() {
        return new ColorDrawable(backgroundColor);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        left = (int) (left + neuElevation*2.5 + utilities.dpToPx(2));
        top = (int) (top + neuElevation*2.5 + utilities.dpToPx(2));
        right = (int) (right + neuElevation*2.5 + utilities.dpToPx(2));
        bottom = (int) (bottom + neuElevation*2.5 + utilities.dpToPx(2));
        super.setPadding(left, top, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        start = (int) (start + neuElevation*2.5 + utilities.dpToPx(2));
        top = (int) (top + neuElevation*2.5 + utilities.dpToPx(2));
        end = (int) (end + neuElevation*2.5 + utilities.dpToPx(2));
        bottom = (int) (bottom + neuElevation*2.5 + utilities.dpToPx(2));
        super.setPadding(start, top, end, bottom);
    }

    /**
     * @return Padding top of view minus default padding
     */
    public int getNuePaddingTop()
    {
        return (int) (getPaddingTop() - neuElevation*2.5 - utilities.dpToPx(2));
    }

    /**
     * @return Padding start of view minus default padding
     */
    public int getNuePaddingStart()
    {
        return (int) (getPaddingStart() - neuElevation*2.5 - utilities.dpToPx(2));
    }

    /**
     * @return Padding end of view minus default padding
     */
    public int getNuePaddingEnd()
    {
        return (int) (getPaddingEnd() - neuElevation*2.5 - utilities.dpToPx(2));
    }

    /**
     * @return Padding bottom of view minus default padding
     */
    public int getNuePaddingBottom()
    {
        return (int) (getPaddingBottom() - neuElevation*2.5 - utilities.dpToPx(2));
    }

    /**
     * Set elevation for the view . in fact it's for shadow value
     * @param elevation
     */
    @Override
    public void setNeuElevation(@Dimension float elevation) {
        this.neuElevation = elevation;
        if (isInitDrawn)
        {
            requestLayout();
        }
    }

    /**
     * @return elevation of view
     */
    @Override
    public float getNeuElevation() {
        return neuElevation;
    }

    /**
     * This function set background color for the view
     * @param color ResourceColor background color
     */
    @Override
    public void setNeuBackgroundColor(@ColorRes int color) {
        try
        {
            this.backgroundColor = ContextCompat.getColor(getContext(),color);
        }catch (Resources.NotFoundException exception)
        {
            this.backgroundColor = color;
        }
        if (isInitDrawn)
        {
            invalidate();
        }
    }

    /**
     * @return background color
     */
    @Override
    public int getNeuBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Set light shadow color for the view
     * @param color light shadow color res
     */
    @Override
    public void setLightShadowColor(@ColorRes  int color) {
        try
        {
            this.lightShadowColor = ContextCompat.getColor(getContext(),color);
        }catch (Resources.NotFoundException exception)
        {
            this.lightShadowColor = color;
        }

        if (isInitDrawn)
        {
            invalidate();
        }
    }

    /**
     * @return light shadow color of view
     */
    @Override
    public int getLightShadowColor() {
        return lightShadowColor;
    }

    /**
     * Set dark shadow color for the view
     * @param color dark shadow color res
     */
    @Override
    public void setDarkShadowColor(@ColorRes int color) {
        try
        {
            this.darkShadowColor = ContextCompat.getColor(getContext(),color);
        }catch (Resources.NotFoundException exception)
        {
            this.darkShadowColor = color;
        }

        if (isInitDrawn)
        {
            invalidate();
        }
    }

    /**
     * @return dark shadow color of the view
     */
    @Override
    public int getDarkShadowColor() {
        return darkShadowColor;
    }

    /**
     * Set radius corners for the view
     * @param radius radius value in pixel
     */
    @Override
    public void setNeuRadius(@Dimension float radius) {
        this.neuRadius = radius;
        if (isInitDrawn)
        {
            invalidate();
        }
    }

    /**
     * @return radius of the corners
     */
    @Override
    public float getNeuRadius() {
        return neuRadius;
    }
}
