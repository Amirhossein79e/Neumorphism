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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.amirhosseinemadi.neumorphism.NeumorphismImpl;
import com.amirhosseinemadi.neumorphism.R;
import com.amirhosseinemadi.neumorphism.Utilities;

public class NeuTextView extends AppCompatTextView implements NeumorphismImpl {
    //TODO light source
    private DisplayMetrics metrics;
    private boolean isInitDrawn;

    private RectF backgroundRect;
    private Paint topPaint;
    private Paint bottomPaint;

    private float neuElevation;
    private float neuRadius;
    private int backgroundColor;
    private int topShadowColor;
    private int bottomShadowColor;

    public NeuTextView(@NonNull Context context) {
        super(context);
        metrics = getContext().getResources().getDisplayMetrics();
        init((int) Utilities.dpToPx(metrics,8f),30, R.color.md_grey_100, R.color.md_white_1000, R.color.md_grey_300);
    }

    public NeuTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        metrics = getContext().getResources().getDisplayMetrics();
        init((int) Utilities.dpToPx(metrics,8f),30, R.color.md_grey_100, R.color.md_white_1000, R.color.md_grey_300);
    }


    private void init(int neuElevation, int neuRadius, int backgroundColor, int topShadowColor, int bottomShadowColor)
    {
        isInitDrawn = false;
        backgroundRect = new RectF();
        topPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setNeuElevation(neuElevation);
        setNeuRadius(neuRadius);
        setTopShadowColor(topShadowColor);
        setBottomShadowColor(bottomShadowColor);
        setBackgroundColor(backgroundColor);
        setPaddingRelative(0,0,0,0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getText().length() == 0 && getHint().length() == 0)
        {
            setPadding((int) (-neuElevation*2.5),(int) (-neuElevation*2.5),(int) (-neuElevation*2.5),(int) (-neuElevation*2.5));
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        backgroundRect.set(0 + getNeuElevation() * 2.5f
                , 0 + getNeuElevation() * 2.5f
                , getWidth() - getNeuElevation() * 2.5f
                , getHeight() - getNeuElevation() * 2.5f);
    }


    private void drawBackground(Canvas canvas)
    {
        topPaint.setStyle(Paint.Style.FILL);
        topPaint.setColor(ContextCompat.getColor(getContext(),getBackgroundColor()));
        topPaint.setShadowLayer(getNeuElevation()*1.7f,-getNeuElevation(),-getNeuElevation(),ContextCompat.getColor(getContext(),getTopShadowColor()));
        bottomPaint.setStyle(Paint.Style.FILL);
        bottomPaint.setColor(ContextCompat.getColor(getContext(),getBackgroundColor()));
        bottomPaint.setShadowLayer(getNeuElevation()*1.7f,+getNeuElevation(),+getNeuElevation(),ContextCompat.getColor(getContext(),getBottomShadowColor()));

        canvas.drawRoundRect(backgroundRect,getNeuRadius(),getNeuRadius(),topPaint);
        canvas.drawRoundRect(backgroundRect,getNeuRadius(),getNeuRadius(),bottomPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        canvas.clipRect(backgroundRect);
        super.onDraw(canvas);
        isInitDrawn = true;
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
        return new ColorDrawable(getBackgroundColor());
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        if (isInitDrawn)
        {
            invalidate();
        }
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        left = (int) (left + neuElevation*2.5 + Utilities.dpToPx(metrics,4));
        top = (int) (top + neuElevation*2.5 + Utilities.dpToPx(metrics,4));
        right = (int) (right + neuElevation*2.5 + Utilities.dpToPx(metrics,4));
        bottom = (int) (bottom + neuElevation*2.5 + Utilities.dpToPx(metrics,4));
        super.setPadding(left, top, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        start = (int) (start + neuElevation*2.5 + Utilities.dpToPx(metrics,4));
        top = (int) (top + neuElevation*2.5 + Utilities.dpToPx(metrics,4));
        end = (int) (end + neuElevation*2.5 + Utilities.dpToPx(metrics,4));
        bottom = (int) (bottom + neuElevation*2.5 + Utilities.dpToPx(metrics,4));
        super.setPadding(start, top, end, bottom);
    }

    @Override
    public int getPaddingStart() {
        return (int) (super.getPaddingStart() - neuElevation*2.5);
    }

    @Override
    public int getPaddingEnd() {
        return (int) (super.getPaddingEnd() - neuElevation*2.5);
    }

    @Override
    public void setNeuElevation(float elevation) {
        this.neuElevation = elevation;
        if (isInitDrawn)
        {
            requestLayout();
        }
    }

    @Override
    public float getNeuElevation() {
        return neuElevation;
    }

    @Override
    public void setTopShadowColor(int color) {
        this.topShadowColor = color;
        if (isInitDrawn)
        {
            invalidate();
        }
    }

    @Override
    public int getTopShadowColor() {
        return topShadowColor;
    }

    @Override
    public void setBottomShadowColor(int color) {
        this.bottomShadowColor = color;
        if (isInitDrawn)
        {
            invalidate();
        }
    }

    @Override
    public int getBottomShadowColor() {
        return bottomShadowColor;
    }

    @Override
    public void setNeuRadius(float radius) {
        this.neuRadius = radius;
        if (isInitDrawn)
        {
            invalidate();
        }
    }

    @Override
    public float getNeuRadius() {
        return neuRadius;
    }
}

