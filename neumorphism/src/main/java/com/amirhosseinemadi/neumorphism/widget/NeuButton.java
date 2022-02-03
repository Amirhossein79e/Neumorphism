package com.amirhosseinemadi.neumorphism.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
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

    public NeuButton(@NonNull Context context) {
        super(context);
        init(16,30, R.color.md_grey_100, R.color.md_white_1000, R.color.md_grey_300);
    }

    public NeuButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(16,30, R.color.md_grey_100, R.color.md_white_1000, R.color.md_grey_300);
    }


    private void init(int neuElevation, int neuRadius, int backgroundColor, int topShadowColor, int bottomShadowColor)
    {
        metrics = getContext().getResources().getDisplayMetrics();
        isInitDrawn = false;

        backgroundRect = new RectF();
        topPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setNeuElevation(neuElevation);
        setNeuRadius(neuRadius);
        setTopShadowColor(topShadowColor);
        setBottomShadowColor(bottomShadowColor);
        setBackgroundColor(backgroundColor);
    }


    private void drawBackground(Canvas canvas)
    {
        topPaint.setStyle(Paint.Style.FILL);
        topPaint.setColor(ContextCompat.getColor(getContext(),getBackgroundColor()));
        topPaint.setShadowLayer(25f,-getNeuElevation(),-getNeuElevation(),ContextCompat.getColor(getContext(),getTopShadowColor()));
        bottomPaint.setStyle(Paint.Style.FILL);
        bottomPaint.setColor(ContextCompat.getColor(getContext(),getBackgroundColor()));
        bottomPaint.setShadowLayer(25f,+getNeuElevation(),+getNeuElevation(),ContextCompat.getColor(getContext(),getBottomShadowColor()));

        canvas.drawRoundRect(backgroundRect,getNeuRadius(),getNeuRadius(),topPaint);
        canvas.drawRoundRect(backgroundRect,getNeuRadius(),getNeuRadius(),bottomPaint);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        backgroundRect.set(left + getNeuElevation() + Utilities.dpToPx(metrics,6)
                , top + getNeuElevation() + Utilities.dpToPx(metrics,6)
                , right - getNeuElevation() - Utilities.dpToPx(metrics,6)
                , bottom - getNeuElevation() - Utilities.dpToPx(metrics,6));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Matrix matrix = new Matrix(canvas.getMatrix());
        matrix.setRectToRect(backgroundRect,new RectF(backgroundRect.left+50f,backgroundRect.top+50f,backgroundRect.right-50f,backgroundRect.bottom-50f), Matrix.ScaleToFit.CENTER);
        drawBackground(canvas);
        canvas.setMatrix(matrix);
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
    public void setNeuElevation(float elevation) {
        this.neuElevation = elevation;
        if (isInitDrawn)
        {
            invalidate();
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
