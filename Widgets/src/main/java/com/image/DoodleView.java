package com.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.photoview.PhotoView;
import com.photoview.PhotoViewAttacher;

public class DoodleView extends PhotoView {

    private PhotoViewAttacher mPhotoViewAttacher;
    private Doodler mDoodler;

    public DoodleView(Context context) {
        this(context, null);
    }

    public DoodleView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public DoodleView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        mDoodler = new Doodler(this);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        if (l instanceof PhotoViewAttacher) {
            if (mPhotoViewAttacher != l) {
                mPhotoViewAttacher = (PhotoViewAttacher) l;
            }
        }
        super.setOnTouchListener(l);
    }

    public void startDoodle() {
        super.setOnTouchListener(mDoodler);
    }

    public void startGestures() {
        super.setOnTouchListener(mPhotoViewAttacher);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDoodler.onDraw(canvas);
    }

    /**
     * Create capture of the drawing view as bitmap
     */
    public Bitmap createCapture() {
        mPhotoViewAttacher.setScale(mPhotoViewAttacher.getMinimumScale(), false);
        Bitmap b = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        draw(c);
        return b;
    }

    public IDoodler getDoodler() {
        return mDoodler;
    }

}
