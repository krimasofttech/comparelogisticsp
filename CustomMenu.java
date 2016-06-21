package logistic.compare.comparelogistic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Sony on 8/30/2015.
 */
public class CustomMenu extends View implements View.OnTouchListener {
    Paint myPaint;
    int height, width;

    int startX = 0, startY = 8;
    int padding = 20;
    int paddingTop;
    Canvas canvas;
    boolean touch = false;

    public CustomMenu(Context context) {
        super(context);
        this.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        myPaint = new Paint();
        Paint p = new Paint();
        p.setColor(Color.DKGRAY);
        p.setStrokeWidth(1);
        p.setTextSize(20);
        height = this.getHeight();
        width = this.getWidth();
        myPaint.setStrokeWidth(1);
        this.canvas = canvas;

        if (touch == false) {
            myPaint.setColor(Color.GRAY);
            canvas.drawLine(startX, startY, startX + (width), startY, myPaint);
        } else {
            p.setColor(Color.BLUE);
            canvas.drawLine(startX, startY, startX + (width), startY, p);
        }
        ///p.setColor(Color.GRAY);
        ///   canvas.drawText("Inbox", width / 2 + 5, 13, p);
    }


    public CustomMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    public CustomMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(this);
    }

    public void changeColor() {

        //if (this.canvas != null && p != null) {
        if (touch == false) {
            touch = true;
        } else {
            touch = false;
        }
        invalidate();
        //}
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("touch worked", "true");
                changeColor();
                return true;
            case MotionEvent.ACTION_UP:
                return true;
        }
        return false;
    }

}
