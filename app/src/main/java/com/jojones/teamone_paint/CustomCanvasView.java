package com.jojones.teamone_paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

// Code from https://examples.javacodegeeks.com/android/core/graphics/canvas-graphics/android-canvas-example/

public class CustomCanvasView extends View {
    public enum Tool {
        Pencil,
        Eraser,
        Circle,
        Square,
        Text
    }

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private Paint mPaintResized;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private float brushSizeIncrementer = 0;
    ArrayList<Paint> brushes = new ArrayList<>();
    Paint myRedPaintFill;
    Paint myGreenPaintStroke;
    Path myPath;
    int currColor = Color.BLACK;
    public boolean isOn;
    public Tool currentTool;

//    public ArrayList<Stroke> allStrokes = new ArrayList<Stroke>();
//    public ArrayList<Circle> circles = new ArrayList<>();
//    public ArrayList<Square> squares = new ArrayList<>();
//    public ArrayList<String> texts = new ArrayList<>();

    public ArrayList<Object> drawables = new ArrayList<>();

    public long layer;

    boolean changeBrushSize = false;

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
        isOn = true;
        currentTool = Tool.Pencil;
        layer = 0;
    }

    public void changeBrushSize()
    {
        //Moved the logic to change brush size to StartTouch method - Juan

        //brushes.add(new Paint(){{
            //setAntiAlias(true);
            //setColor(Color.BLACK);
            //setStyle(Paint.Style.STROKE);
            //setStrokeJoin(Paint.Join.ROUND);
            //setStrokeWidth(10 +brushSizeIncrementer);
        //}});

        brushSizeIncrementer += 10;

        changeBrushSize = true;
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

        ////canvas.drawPath(mPath, brushes.get(brushes.size() -1));

//        for (Stroke s : allStrokes) {
//            canvas.drawPath(s.get_path(), s.get_paint());
//        }
//
//        for (Circle c : circles) {
//            canvas.drawCircle(c.x, c.y, c.r, c.p);
//        }
//
//        for (Square s : squares) {
//            canvas.drawRect(s.x, s.y, s.x + s.w, s.y + s.h, s.p);
//        }

        for (Object o : drawables) {
            if (o instanceof Stroke) {
                Stroke s = (Stroke)o;
                canvas.drawPath(s.get_path(), s.get_paint());
            }
            else if (o instanceof Circle) {
                Circle c = (Circle)o;
                canvas.drawCircle(c.x, c.y, c.r, c.p);
            }
            else if (o instanceof Square) {
                Square s = (Square)o;
                canvas.drawRect(s.x, s.y, s.x + s.w, s.y + s.h, s.p);
            }
        }

//        for (String s : texts) {
//            canvas.drawCircle(c.x, c.y, c.r, c.p);
//        }
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        if (currentTool == Tool.Pencil || currentTool == Tool.Eraser) {
            ////mPath.moveTo(x, y);
            Path p = new Path();
            Paint pt = new Paint();
            pt.setAntiAlias(true);
            pt.setColor(currColor);
            pt.setStyle(Paint.Style.STROKE);
            pt.setStrokeJoin(Paint.Join.ROUND);
            pt.setStrokeWidth(4f);
            Stroke s = new Stroke(p, pt);
//            allStrokes.add(s);
            drawables.add(s);
//            allStrokes.get(allStrokes.size() - 1).get_path().moveTo(x, y);
            ((Stroke)drawables.get(drawables.size() - 1)).get_path().moveTo(x, y);

            if (currentTool == Tool.Eraser) {
//                allStrokes.get(allStrokes.size() - 1).get_paint().setColor(Color.WHITE);
//                allStrokes.get(allStrokes.size() - 1).get_paint().setStrokeWidth(40f);
                ((Stroke)drawables.get(drawables.size() - 1)).get_paint().setColor(Color.WHITE);
                ((Stroke)drawables.get(drawables.size() - 1)).get_paint().setStrokeWidth(40f);
            }

            //This is where the brush size is changed - Juan
            if (changeBrushSize) {
//                allStrokes.get(allStrokes.size() - 1).get_paint().setStrokeWidth(brushSizeIncrementer);
                ((Stroke)drawables.get(drawables.size() - 1)).get_paint().setStrokeWidth(brushSizeIncrementer);
                changeBrushSize = false;
            }
            mX = x;
            mY = y;
        }

        if (currentTool == Tool.Circle) {
            circleTouch(x, y);
            layer++;
        }

        if (currentTool == Tool.Square) {
            squareTouch(x, y);
            layer++;
        }
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        if (currentTool == Tool.Pencil || currentTool == Tool.Eraser) {
//            Path p = allStrokes.get(allStrokes.size() - 1).get_path();
            Path p = ((Stroke)drawables.get(drawables.size() - 1)).get_path();
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOLERANCE || dy >= TOLERANCE) {
                ////Changed "mPath" to "p"
                p.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        if (currentTool == Tool.Circle) {
            circleTouch(x, y);
            layer++;
        }

        if (currentTool == Tool.Square) {
            squareTouch(x, y);
            layer++;
        }
    }

    public void circleTouch(float x, float y) {
        Paint pt = new Paint();
        pt.setAntiAlias(true);
        pt.setColor(currColor);
        pt.setStyle(Paint.Style.STROKE);
        pt.setStrokeWidth(10f);

//        circles.add(new Circle(x, y, 30, pt));
        drawables.add(new Circle(x, y, 30, pt));
    }

    public void squareTouch(float x, float y) {
        Paint pt = new Paint();
        pt.setAntiAlias(true);
        pt.setColor(currColor);
        pt.setStyle(Paint.Style.STROKE);
        pt.setStrokeWidth(10f);

//        squares.add(new Square(x, y, 50, 50, pt));
        drawables.add(new Square(x, y, 50, 50, pt));
    }

    public void clearCanvas() {
//        for (Stroke s : allStrokes)
//        {
//            s.get_path().reset();
//        }

        drawables.clear();

//        circles.clear();
        ////mPath.reset();
        invalidate();
    }

    // when ACTION_UP stop touch
    private void upTouch()
    {
        if (currentTool == Tool.Pencil || currentTool == Tool.Eraser) {
//            allStrokes.get(allStrokes.size() - 1).get_path().lineTo(mX, mY);
            ((Stroke)drawables.get(drawables.size() - 1)).get_path().lineTo(mX, mY);
        }
        ////mPath.lineTo(mX, mY);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOn) {
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

        return false;
    }

    public void changeTool(Tool t) {
        switch (t) {
            case Pencil:
                break;
            case Eraser:
                break;
            case Circle:
                break;
        }

        currentTool = t;
    }

    public void setColor(int color){
        currColor = color;
    }
}
