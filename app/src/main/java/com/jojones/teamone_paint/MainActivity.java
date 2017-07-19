package com.jojones.teamone_paint;
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
