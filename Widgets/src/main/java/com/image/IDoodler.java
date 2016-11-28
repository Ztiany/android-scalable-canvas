package com.image;

import android.support.annotation.ColorInt;

/**
 * <br/>    Description  :
 * <br/>    Email    : ztiany3@gmail.com
 *
 * @author Ztiany
 *         <p>
 *         Date : 2016-11-19 00:03
 */

public interface IDoodler {


    IDoodler setColor(@ColorInt int color);

    IDoodler setStrokeWidth(float width);

    IDoodler setAlpha(int alpha);

    IDoodler setModel(@Mode int model);

    IDoodler undo();

    IDoodler redo();

    IDoodler setType(@Type int type);


}
