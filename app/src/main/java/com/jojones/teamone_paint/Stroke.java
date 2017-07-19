package com.jojones.teamone_paint;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by jaguilar on 7/18/2017.
 */

public class Stroke
{
    private Path _path;
    private Paint _paint;

    public Stroke(Path path, Paint paint)
    {
        _path = path;
        _paint = paint;
    }

    public Path get_path()
    {
        return _path;
    }

    public void set_path(Path _path)
    {
        this._path = _path;
    }

    public Paint get_paint()
    {
        return _paint;
    }

    public void set_paint(Paint _paint)
    {
        this._paint = _paint;
    }
}
