package com.jojones.teamone_paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

// Code from https://examples.javacodegeeks.com/android/core/graphics/canvas-graphics/android-canvas-example/

public class CustomCanvasView extends View {
    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private Paint mPaintResized;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private float brushSizeIncrementer = 20;
    ArrayList<Paint> brushes = new ArrayList<>();
    Paint myRedPaintFill;
    Paint myGreenPaintStroke;
    Path myPath;

//    public DrawView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//
//    }
//    private void init(){
//        myRedPaintFill = new Paint();
//        myRedPaintFill.setColor(Color.RED);
//        myRedPaintFill.setStyle(Paint.Style.FILL);
//
//        myGreenPaintStroke = new Paint();
//        myGreenPaintStroke.setColor(0xff337722); // aarrggbb alpha is first
//        myGreenPaintStroke.setStyle(Paint.Style.STROKE);
//        myGreenPaintStroke.setStrokeWidth(10);
//    }
//
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        canvas.drawLine(0, 0, 200, 200, myGreenPaintStroke);
//        canvas.drawCircle(100, 300, 40, myRedPaintFill);
//        canvas.drawRect(200,300, 250, 350, myGreenPaintStroke);
//
//        myPath = new Path();
//        myPath.moveTo(400,400);
//        myPath.lineTo(500,600);
//        myPath.lineTo(300,600);
//        canvas.drawPath(myPath, myRedPaintFill);
//
//    }

    public CustomCanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // we set a new Path
        mPath = new Path();


        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        brushes.add(mPaint);
    }

    public void changeBrushSize(){

        brushes.add(new Paint(){{
            setAntiAlias(true);
            setColor(Color.BLACK);
            setStyle(Paint.Style.STROKE);
            setStrokeJoin(Paint.Join.ROUND);
            setStrokeWidth(10 +brushSizeIncrementer);
        }});
        brushSizeIncrementer += 10;
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw
        canvas.drawPath(mPath, brushes.get(brushes.size() -1));

    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }
}