package com.image;

import android.support.annotation.IntDef;

/**
 * <br/>    Description  :
 * <br/>    Email    : ztiany3@gmail.com
 *
 * @author Ztiany
 *         <p>
 *         Date : 2016-11-28 23:27
 */
@IntDef(
        {
                Type.PEN,
                Type.CIRCLE,
                Type.LINE,
                Type.SQUARE
        }
)
public @interface Type {
    int PEN = 1;
    int LINE = 2;
    int CIRCLE = 3;
    int SQUARE = 4;
}
