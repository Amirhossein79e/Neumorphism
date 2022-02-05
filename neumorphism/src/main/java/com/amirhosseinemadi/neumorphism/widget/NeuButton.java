package com.amirhosseinemadi.neumorphism.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import com.amirhosseinemadi.neumorphism.NeumorphismImpl;
import com.amirhosseinemadi.neumorphism.R;
import com.amirhosseinemadi.neumorphism.Utilities;

public class NeuButton extends AppCompatButton implements NeumorphismImpl {
    //TODO should check gravity
    private DisplayMetrics metrics;
    private boolean isInitDrawn;
    private boolean isMarginSet;

    private Rect mainRect;
    private RectF backgroundRect;
    private Rect innerRect;
    private Paint topPaint;
    private Paint bottomPaint;
    private Rect textBounds;
    private Rect wordBounds;

    private float neuElevation;
    private float neuRadius;
    private int backgroundColor;
    private int topShadowColor;
    private int bottomShadowColor;

    public NeuButton(@NonNull Context context) {
        super(context);
        init(20,24, R.color.md_grey_100, R.color.md_white_1000, R.color.md_grey_300);
    }

    public NeuButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(20,24, R.color.md_grey_100, R.color.md_white_1000, R.color.md_grey_300);
    }


    private void init(int neuElevation, int neuRadius, int backgroundColor, int topShadowColor, int bottomShadowColor)
    {
        metrics = getContext().getResources().getDisplayMetrics();
        isInitDrawn = false;

        mainRect = new Rect();
        backgroundRect = new RectF();
        innerRect = new Rect();
        topPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textBounds = new Rect();
        wordBounds = new Rect();

        setNeuElevation(neuElevation);
        setNeuRadius(neuRadius);
        setTopShadowColor(topShadowColor);
        setBottomShadowColor(bottomShadowColor);
        setBackgroundColor(backgroundColor);
        setPaddingRelative(0,0,0,0);
    }


    private int calculateMinWidth(DisplayMetrics metrics, Rect textBounds, int neuElevation)
    {
        int width;
        if (getText().length() > 0)
        {
            if (textBounds.width() < metrics.widthPixels - neuElevation*2 - Utilities.dpToPx(metrics,24))
            {
                if (textBounds.width() + neuElevation*2 + Utilities.dpToPx(metrics,16) >= getSuggestedMinimumWidth())
                {
                    width = (int) (textBounds.width() + neuElevation*2 + Utilities.dpToPx(metrics,32));
                }else
                {
                    width = getSuggestedMinimumWidth() + neuElevation*2;
                }
            }else
            {
                width = metrics.widthPixels;
            }
        }else
        {
            width = getSuggestedMinimumWidth() + neuElevation*2 ;
        }

        return width;
    };


    private int calculateMinHeight(DisplayMetrics metrics, Rect textBounds, int neuElevation)
    {
        int height;
        if (getText().length() > 0)
        {
            double lines = Math.ceil(textBounds.width() / (double) (metrics.widthPixels - Utilities.dpToPx(metrics,12) - neuElevation*2 - getPaddingLeft() - getPaddingRight()));
            System.out.println(metrics.widthPixels - Utilities.dpToPx(metrics,12) - neuElevation*2 - getPaddingLeft() - getPaddingRight());
            if (textBounds.height() * lines < metrics.heightPixels - neuElevation*2)
            {
                if (lines > 1)
                {
                    height = (int) (getMinHeight() + getLineHeight() * lines);
                    System.out.println(textBounds.width());
                    System.out.println(lines);
                }else
                {
                    height = getSuggestedMinimumHeight() + neuElevation*2;
                }
            }else
            {
                height = metrics.heightPixels + neuElevation*2;
            }
        }else
        {
            height = getSuggestedMinimumHeight() + neuElevation*2;
        }

        return height;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        getPaint().getTextBounds(getText().toString(),0,getText().length(),textBounds);

        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST)
        {
            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)
            {
                int calculatedWidth = calculateMinWidth(metrics,textBounds,(int) neuElevation);
                super.onMeasure(MeasureSpec.makeMeasureSpec(calculatedWidth,MeasureSpec.EXACTLY),heightMeasureSpec);

            }else if (widthMode == MeasureSpec.AT_MOST)
            {
                setMeasuredDimension(calculateMinWidth(metrics,textBounds,(int) neuElevation), heightSize);
            }else
            {
                setMeasuredDimension(widthSize, calculateMinHeight(metrics,textBounds,(int) neuElevation));
            }
        }else
        {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) getLayoutParams();

        backgroundRect.set(left + getNeuElevation() + Utilities.dpToPx(metrics,8) - marginParams.leftMargin
                , top + getNeuElevation() + Utilities.dpToPx(metrics,8)
                , right - getNeuElevation() - Utilities.dpToPx(metrics,8) - marginParams.leftMargin
                , bottom - getNeuElevation() - Utilities.dpToPx(metrics,8));
    }


    private void drawBackground(Canvas canvas)
    {
        topPaint.setStyle(Paint.Style.FILL);
        topPaint.setColor(ContextCompat.getColor(getContext(),getBackgroundColor()));
        topPaint.setShadowLayer(28f,-getNeuElevation(),-getNeuElevation(),ContextCompat.getColor(getContext(),getTopShadowColor()));
        bottomPaint.setStyle(Paint.Style.FILL);
        bottomPaint.setColor(ContextCompat.getColor(getContext(),getBackgroundColor()));
        bottomPaint.setShadowLayer(28f,+getNeuElevation(),+getNeuElevation(),ContextCompat.getColor(getContext(),getBottomShadowColor()));

        canvas.drawRoundRect(backgroundRect,getNeuRadius(),getNeuRadius(),topPaint);
        canvas.drawRoundRect(backgroundRect,getNeuRadius(),getNeuRadius(),bottomPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
//        canvas.getClipBounds(mainRect);
//        Matrix matrix = getMatrix();
//        matrix.setRectToRect(new RectF(mainRect),backgroundRect, Matrix.ScaleToFit.CENTER);
//        //matrix.setScale(1.1f,1.1f);
//        canvas.setMatrix(matrix);
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
        int leftP = (int) (left + neuElevation + Utilities.dpToPx(metrics,8));
        int topP = (int) (top + neuElevation + Utilities.dpToPx(metrics,8));
        int rightP = (int) (right + neuElevation + Utilities.dpToPx(metrics,8));
        int bottomP = (int) (bottom + neuElevation + Utilities.dpToPx(metrics,8));
        super.setPadding(leftP, topP, rightP, bottomP);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        int startP = (int) (start + neuElevation + Utilities.dpToPx(metrics,8));
        int topP = (int) (top + neuElevation + Utilities.dpToPx(metrics,8));
        int endP = (int) (end + neuElevation + Utilities.dpToPx(metrics,8));
        int bottomP = (int) (bottom + neuElevation + Utilities.dpToPx(metrics,8));
        super.setPaddingRelative(startP, topP, endP, bottomP);
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
