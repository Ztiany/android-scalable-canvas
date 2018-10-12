package com.image;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef(value = {
        Mode.PEN,
        Mode.ERASER
})
@Retention(RetentionPolicy.SOURCE)
public @interface Mode {
    int PEN = 1;
    int ERASER = 2;
}
