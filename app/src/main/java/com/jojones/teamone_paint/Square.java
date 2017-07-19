package com.jojones.teamone_paint;

import android.graphics.Paint;

class Square {
    public float x;
    public float y;
    public int w;
    public int h;
    public Paint p;

    public Square(float x, float y, int w, int h, Paint p) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.p = p;
    }
}
