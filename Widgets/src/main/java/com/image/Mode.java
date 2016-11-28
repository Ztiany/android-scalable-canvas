package com.image;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <br/>   Description：
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2016-11-21 15:11
 */

@IntDef(value = {
        Mode.PEN,
        Mode.ERASER
})
@Retention(RetentionPolicy.SOURCE)
public @interface Mode {
    int PEN = 1;
    int ERASER = 2;
}
