package com.image;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * <br/>    Description  :
 * <br/>    Email    : ztiany3@gmail.com
 *
 * @author Ztiany
 *         <p>
 *         Date : 2016-11-19 15:51
 */

class PathDraw implements Draw {

    private Path mPath;
    private Paint mPaint;
    private Canvas mCanvas;

    PathDraw() {
        mPath = new Path();
    }


    @Override
    public void onMove(float x, float y) {
        mPath.lineTo(x, y);
    }

    @Override
    public void onDown(float x, float y) {
        mPath.moveTo(x, y);
        mPath.lineTo(x, y);
    }

    @Override
    public void onUp(float x, float y) {
        mPath.lineTo(x, y);
    }

    @Override
    public void draw() {
        mCanvas.drawPath(mPath, mPaint);
    }


    @Override
    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    @Override
    public void setCanvas(Canvas canvas) {
        mCanvas = canvas;
    }


}
