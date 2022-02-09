package com.amirhosseinemadi.neumorphism.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import com.amirhosseinemadi.neumorphism.NeumorphismImpl;
import com.amirhosseinemadi.neumorphism.R;
import com.amirhosseinemadi.neumorphism.Utilities;

public class NeuButton extends AppCompatButton implements NeumorphismImpl {
    //TODO light source
    private final DisplayMetrics metrics;
    private final Utilities utilities;
    private boolean isInitDrawn;

    private RectF backgroundRect;
    private Paint topPaint;
    private Paint bottomPaint;
    private Rect textBounds;

    private float neuElevation;
    private float neuRadius;
    private int backgroundColor;
    private int lightShadowColor;
    private int darkShadowColor;

    public NeuButton(@NonNull Context context) {
        super(context);
        metrics = getContext().getResources().getDisplayMetrics();
        utilities = new Utilities(metrics);
        init((int) utilities.dpToPx(6f),30, R.color.md_grey_100, R.color.md_white_1000, R.color.md_grey_300);
    }

    public NeuButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        metrics = getContext().getResources().getDisplayMetrics();
        utilities = new Utilities(metrics);
        init((int) utilities.dpToPx(6f),30, R.color.md_grey_100, R.color.md_white_1000, R.color.md_grey_300);
    }


    private void init(int neuElevation, int neuRadius, int backgroundColor, int topShadowColor, int bottomShadowColor)
    {
        isInitDrawn = false;
        backgroundRect = new RectF();
        topPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textBounds = new Rect();

        setNeuElevation(neuElevation);
        setNeuRadius(neuRadius);
        setLightShadowColor(topShadowColor);
        setDarkShadowColor(bottomShadowColor);
        setBackgroundColor(backgroundColor);
        setPaddingRelative(0,0,0,0);
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
            int masterWidth = (int) (textBounds.width() + utilities.dpToPx(24) + calculateDrawableWidth() + getCompoundDrawablePadding() + neuElevation*5);

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
            int masterLineWidth = (int) (metrics.widthPixels - neuElevation*5 - utilities.dpToPx(24) - calculateDrawableWidth());

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

        backgroundRect.set(0 + neuElevation * 2.5f
                , 0 + neuElevation * 2.5f
                , getWidth() - neuElevation * 2.5f
                , getHeight() - neuElevation * 2.5f);
    }


    /**
     * This function draw neumorphism background for the view
     * @param canvas
     */
    private void drawBackground(Canvas canvas)
    {
        topPaint.setStyle(Paint.Style.FILL);
        topPaint.setColor(ContextCompat.getColor(getContext(),backgroundColor));
        topPaint.setShadowLayer(neuElevation*1.7f,-neuElevation,-neuElevation,ContextCompat.getColor(getContext(), lightShadowColor));

        bottomPaint.setStyle(Paint.Style.FILL);
        bottomPaint.setColor(ContextCompat.getColor(getContext(),backgroundColor));
        bottomPaint.setShadowLayer(neuElevation*1.7f,+neuElevation,+neuElevation,ContextCompat.getColor(getContext(), darkShadowColor));

        canvas.drawRoundRect(backgroundRect,neuRadius,neuRadius,topPaint);
        canvas.drawRoundRect(backgroundRect,neuRadius,neuRadius,bottomPaint);
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
    public void setBackground(Drawable background) {
        if (background instanceof ColorDrawable)
        {
            setBackgroundColor(((ColorDrawable) background).getColor());
        }else if (background instanceof ShapeDrawable)
        {
            setBackgroundColor(((ShapeDrawable) background).getPaint().getColor());
        }
    }

    @Override
    public Drawable getBackground() {
        return new ColorDrawable(backgroundColor);
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        if (isInitDrawn)
        {
            invalidate();
        }
    }

    /**
     * @return background color of view
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setMinWidth(int minPixels) {
        minPixels = (int) Math.max(minPixels, getSuggestedMinimumWidth() + neuElevation*5);
        super.setMinWidth(minPixels);
    }

    @Override
    public void setMinHeight(int minPixels) {
        minPixels = (int) Math.max(minPixels, getSuggestedMinimumHeight() + neuElevation*5);
        super.setMinHeight(minPixels);
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

    @Override
    public int getPaddingTop() {
        return (int) (super.getPaddingTop() - neuElevation*2.5 - utilities.dpToPx(2));
    }

    @Override
    public int getPaddingStart() {
        return (int) (super.getPaddingStart() - neuElevation*2.5 - utilities.dpToPx(2));
    }

    @Override
    public int getPaddingEnd() {
        return (int) (super.getPaddingEnd() - neuElevation*2.5 - utilities.dpToPx(2));
    }

    @Override
    public int getPaddingLeft() {
        return (int) (super.getPaddingLeft() - neuElevation*2.5 - utilities.dpToPx(2));
    }

    @Override
    public int getPaddingRight() {
        return (int) (super.getPaddingRight() - neuElevation*2.5 - utilities.dpToPx(2));
    }

    @Override
    public int getPaddingBottom() {
        return (int) (super.getPaddingBottom() - neuElevation*2.5 - utilities.dpToPx(2));
    }

    /**
     * Set elevation for the view . in fact it's for shadow value
     * @param elevation
     */
    @Override
    public void setNeuElevation(float elevation) {
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
     * Set light shadow color for the view
     * @param color light shadow color
     */
    @Override
    public void setLightShadowColor(int color) {
        this.lightShadowColor = color;
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
     * @param color dark shadow color
     */
    @Override
    public void setDarkShadowColor(int color) {
        this.darkShadowColor = color;
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
    public void setNeuRadius(float radius) {
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
