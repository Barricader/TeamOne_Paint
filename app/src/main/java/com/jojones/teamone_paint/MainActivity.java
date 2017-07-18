package com.jojones.teamone_paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    CustomCanvasView customCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCanvas = (CustomCanvasView) findViewById(R.id.customCanvas);
    }

    public void clearCanvas(View view) {
        customCanvas.clearCanvas();
    }
}
