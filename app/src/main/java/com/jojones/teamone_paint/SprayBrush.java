package com.jojones.teamone_paint;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Logan on 7/19/2017.
 */

public class SprayBrush {
    float x;
    float y;
    float size;
    int color;
    Bitmap bitmap;
    public SprayBrush(float x, float y, float size, int color, Bitmap bitmap){
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.bitmap = bitmap;
    }
}
