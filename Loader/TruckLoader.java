package logistic.compare.comparelogistic.Loader;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.TimerTask;

import logistic.compare.comparelogistic.R;

public class TruckLoader extends View {
    private String mExampleString;
    private int mExampleColor = Color.RED;
    private float mExampleDimension = 0;
    private Drawable mExampleDrawable;
    float originX = 0f, originY = 0f;
    float destX;
    float dextY;
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    Bitmap bmp;
    float x = 0, y = 0;

    public float getOriginX() {
        return originX;
    }

    public void setOriginX(float originX) {
        this.originX = originX;
    }

    public float getOriginY() {
        return originY;
    }

    public void setOriginY(float originY) {
        this.originY = originY;
    }

    public float getDestX() {
        return destX;
    }

    public void setDestX(float destX) {
        this.destX = destX;
        invalidateTextPaintAndMeasurements();
    }

    public float getDextY() {
        return dextY;
    }

    public void setDextY(float dextY) {
        this.dextY = dextY;
        invalidateTextPaintAndMeasurements();
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
        invalidateTextPaintAndMeasurements();
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
        invalidateTextPaintAndMeasurements();
    }

    public TruckLoader(Context context) {
        super(context);
        init(null, 0);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.loadertruck);
    }

    public TruckLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.loadertruckn);
    }

    public TruckLoader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.loadertruck);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TruckLoader, defStyle, 0);

        destX = a.getFloat(R.styleable.TruckLoader_destx, destX);
        dextY = a.getFloat(R.styleable.TruckLoader_desty, dextY);

        a.recycle();
        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        y = canvas.getHeight() / 2;
        destX = canvas.getWidth();

        if (x < destX) {
            x = x + 1;
        } else if (x == destX) {
            x = 0;
        }
        canvas.drawBitmap(bmp, x, y, mTextPaint);
        canvas.drawLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight(), mTextPaint);
        invalidate();
    }


    public String getExampleString() {
        return mExampleString;
    }

    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    public int getExampleColor() {
        return mExampleColor;
    }

    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
