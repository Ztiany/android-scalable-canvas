package com.image;

import android.support.annotation.ColorInt;


public interface IDoodler {

    IDoodler setColor(@ColorInt int color);

    IDoodler setStrokeWidth(float width);

    IDoodler setAlpha(int alpha);

    IDoodler setModel(@Mode int model);

    IDoodler undo();

    IDoodler redo();

    IDoodler setType(@Type int type);

}
