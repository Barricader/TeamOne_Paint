package com.jojones.teamone_paint;

import android.graphics.Paint;

class Square {
    float x;
    float y;
    int w;
    int h;
    Paint p;

    Square(float x, float y, int w, int h, Paint p) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.p = p;
    }
}
