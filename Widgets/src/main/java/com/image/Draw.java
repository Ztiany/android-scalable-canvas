package com.image;

import android.graphics.Canvas;
import android.graphics.Paint;

interface Draw {

    void onMove(float x, float y);

    void onDown(float x, float y);

    void onUp(float x, float y);

    void draw();

    void setPaint(Paint paint);

    void setCanvas(Canvas canvas);

}
