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

public class CustomCanvasView extends View {
    enum Tool {
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
    int currColor = Color.BLACK;
    public boolean isOn;
    public Tool currentTool;

    public ArrayList<Object> drawables = new ArrayList<>();

    public long layer;

    boolean changeBrushSize = false;

    public CustomCanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        mPath = new Path();

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

    public void changeBrushSize() {
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Object o : drawables) {
            if (o instanceof Stroke) {
                Stroke s = (Stroke) o;
                canvas.drawPath(s.get_path(), s.get_paint());
            } else if (o instanceof Circle) {
                Circle c = (Circle) o;
                canvas.drawCircle(c.x, c.y, c.r, c.p);
            } else if (o instanceof Square) {
                Square s = (Square) o;
                canvas.drawRect(s.x, s.y, s.x + s.w, s.y + s.h, s.p);
            }
        }
    }

    private void startTouch(float x, float y) {
        if (currentTool == Tool.Pencil || currentTool == Tool.Eraser) {
            Path p = new Path();
            Paint pt = new Paint();
            pt.setAntiAlias(true);
            pt.setColor(currColor);
            pt.setStyle(Paint.Style.STROKE);
            pt.setStrokeJoin(Paint.Join.ROUND);
            pt.setStrokeWidth(4f);
            Stroke s = new Stroke(p, pt);
            drawables.add(s);
            ((Stroke) drawables.get(drawables.size() - 1)).get_path().moveTo(x, y);

            if (currentTool == Tool.Eraser) {
                ((Stroke) drawables.get(drawables.size() - 1)).get_paint().setColor(Color.WHITE);
                ((Stroke) drawables.get(drawables.size() - 1)).get_paint().setStrokeWidth(40f);
            }

            //This is where the brush size is changed - Juan
            if (changeBrushSize) {
                ((Stroke) drawables.get(drawables.size() - 1)).get_paint().setStrokeWidth(brushSizeIncrementer);
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

    private void moveTouch(float x, float y) {
        if (currentTool == Tool.Pencil || currentTool == Tool.Eraser) {
            Path p = ((Stroke) drawables.get(drawables.size() - 1)).get_path();
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOLERANCE || dy >= TOLERANCE) {
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

        drawables.add(new Circle(x, y, 30, pt));
    }

    public void squareTouch(float x, float y) {
        Paint pt = new Paint();
        pt.setAntiAlias(true);
        pt.setColor(currColor);
        pt.setStyle(Paint.Style.STROKE);
        pt.setStrokeWidth(10f);

        drawables.add(new Square(x, y, 50, 50, pt));
    }

    public void clearCanvas() {
        drawables.clear();
        invalidate();
    }

    private void upTouch() {
        if (currentTool == Tool.Pencil || currentTool == Tool.Eraser) {
            ((Stroke) drawables.get(drawables.size() - 1)).get_path().lineTo(mX, mY);
        }
    }

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
        currentTool = t;
    }

    public void setColor(int color) {
        currColor = color;
    }
}
