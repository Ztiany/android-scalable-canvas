package com.image;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * <br/>    Description  :
 * <br/>    Email    : ztiany3@gmail.com
 *
 * @author Ztiany
 *         <p>
 *         Date : 2016-11-19 15:46
 */

interface Draw {

    void onMove(float x, float y);

    void onDown(float x, float y);

    void onUp(float x, float y);


    void draw();

    void setPaint(Paint paint);

    void setCanvas(Canvas canvas);

}
