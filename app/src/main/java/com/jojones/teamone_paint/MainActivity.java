package com.jojones.teamone_paint;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;
//https://github.com/yukuku/ambilwarna

public class MainActivity extends AppCompatActivity {
    CustomCanvasView customCanvas;
    ArrayList<View> viewTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customCanvas = (CustomCanvasView) findViewById(R.id.customCanvas);

        viewTools = new ArrayList<>();

        viewTools.add(findViewById(R.id.pencilButton));
        viewTools.add(findViewById(R.id.eraserButton));
        viewTools.add(findViewById(R.id.circleButton));
        viewTools.add(findViewById(R.id.squareButton));

        resetColor();
        findViewById(R.id.pencilButton).setBackgroundColor(Color.GRAY);
    }

    public void changeBrushSize(View view) {
        customCanvas.changeBrushSize();
    }

    public void clearCanvas(View view) {
        customCanvas.clearCanvas();
    }

    public void toolOnClick(View v) {
        resetColor();
        v.setBackgroundColor(Color.GRAY);

        switch (v.getId()) {
            case R.id.pencilButton:
                customCanvas.changeTool(CustomCanvasView.Tool.Pencil);
                break;
            case R.id.eraserButton:
                customCanvas.changeTool(CustomCanvasView.Tool.Eraser);
                break;
            case R.id.circleButton:
                customCanvas.changeTool(CustomCanvasView.Tool.Circle);
                break;
            case R.id.squareButton:
                customCanvas.changeTool(CustomCanvasView.Tool.Square);
                break;
            case R.id.sprayButton:
                customCanvas.changeTool(CustomCanvasView.Tool.SprayBrush);
        }
    }

    public void openOptions(View v) {
        ConstraintLayout opts = (ConstraintLayout) findViewById(R.id.options);
        findViewById(R.id.imgOpenOptions).setClickable(false);

        expand(opts);
    }

    public void closeOptions(View v) {
        ConstraintLayout opts = (ConstraintLayout) findViewById(R.id.options);
        findViewById(R.id.imgOpenOptions).setClickable(true);

        collapse(opts);
    }

    public void undo(View v) {
        customCanvas.undo();
    }

    public void resetColor() {
        for (View v : viewTools) {
            v.setBackgroundColor(Color.LTGRAY);
        }
    }

    public void expand(final View v) {
        final float heightDP = 604f;
        final float scale = v.getContext().getResources().getDisplayMetrics().density;

        final int targetHeight = (int) (heightDP * scale + 0.5f);

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.setVisibility(View.VISIBLE);
                v.getLayoutParams().height = (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        customCanvas.isOn = false;

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        customCanvas.isOn = true;

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    protected void ChangeColorHandler(View view) {
        openColorDialog(true);
    }

    private void openColorDialog(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, customCanvas.currColor, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                customCanvas.setColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "Action canceled!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void addTextOnClick(View v)
    {
        SharedPreferences sharedPreferences =
                this.getSharedPreferences("com.example.sharedpref",
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("one", ((EditText)findViewById(R.id.userInputEditText)).getText().toString()).apply();
        customCanvas.addTextOnClick();
    }
}
