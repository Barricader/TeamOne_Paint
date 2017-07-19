package com.jojones.teamone_paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;
//https://github.com/yukuku/ambilwarna

public class MainActivity extends AppCompatActivity {
    CustomCanvasView customCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customCanvas = (CustomCanvasView) findViewById(R.id.customCanvas);

    }
    public void changeBrushSize(View view){customCanvas.changeBrushSize();}
    public void clearCanvas(View view) {
        customCanvas.clearCanvas();
    }
    public void eraserButtonOnClick(View v)
    {
        customCanvas.eraserButtonOnClick();
    }

    public void openOptions(View v) {
        ConstraintLayout opts = (ConstraintLayout) findViewById(R.id.options);

        findViewById(R.id.imgOpenOptions).setVisibility(View.INVISIBLE);

        LayoutParams params = opts.getLayoutParams();

        // Changes the height and width to the specified *pixels*
//        float heightDP = 604f;
//        float scale = getResources().getDisplayMetrics().density;
//
//        params.height = (int) (heightDP * scale + 0.5f);
//        opts.setLayoutParams(params);
        expand(opts);
    }

    public void closeOptions(View v) {
        ConstraintLayout opts = (ConstraintLayout) findViewById(R.id.options);

        findViewById(R.id.imgOpenOptions).setVisibility(View.VISIBLE);

        LayoutParams params = opts.getLayoutParams();

        // Changes the height and width to the specified *pixels*
//        params.height = 1;
//        opts.setLayoutParams(params);

        collapse(opts);
    }

    public static void expand(final View v) {
        float heightDP = 604f;
        float scale = v.getContext().getResources().getDisplayMetrics().density;

        final int targetHeight = (int) (heightDP * scale + 0.5f);

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? targetHeight
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
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

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    protected void ChangeColorHandler(View view){
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
}
