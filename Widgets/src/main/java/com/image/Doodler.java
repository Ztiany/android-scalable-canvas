package com.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.ColorInt;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

class Doodler implements View.OnTouchListener, IDoodler {

    private Draw mCurrentAction;
    private List<Draw> mActions;
    private DoodleView mHost;
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mDrawBitmap;
    private Paint mClearPaint;
    private float[] mValue = new float[9];

    private int mColor = Color.BLACK;
    private float mStrokeWidth;
    private int mAlpha = 255;
    private int mMode = Mode.PEN;

    Doodler(DoodleView host) {
        mHost = host;
        init();
    }

    private void init() {
        mActions = new ArrayList<>();
        mStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, mHost.getResources().getDisplayMetrics());

        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        checkCanvas();
        int action = event.getAction();
        mHost.getImageMatrix().getValues(mValue);

        float touchX = event.getX();
        float touchY = event.getY();
        touchX -= mValue[Matrix.MTRANS_X];
        touchY -= mValue[Matrix.MTRANS_Y];
        touchX /= mValue[Matrix.MSCALE_X];
        touchY /= mValue[Matrix.MSCALE_Y];

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mCurrentAction = createNewDraw();
                mCurrentAction.onDown(touchX, touchY);
                mHost.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentAction.onMove(touchX, touchY);
                mCurrentAction.draw();
                mHost.invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mCurrentAction.onUp(touchX, touchY);
                mActions.add(mCurrentAction);

                mCurrentAction.draw();
                mHost.invalidate();
                break;
        }
        return true;
    }

    private void checkCanvas() {
        if (mDrawBitmap == null) {
            mDrawBitmap = Bitmap.createBitmap(mHost.getMeasuredWidth(), mHost.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mDrawBitmap);
            mCanvas.drawColor(0x00FFFFFF);
        }
    }

    private Draw createNewDraw() {
        return createPathDraw();
    }

    private Draw createPathDraw() {
        PathDraw pathDraw = new PathDraw();
        pathDraw.setPaint(getCurrentAttrPaint());
        pathDraw.setCanvas(mCanvas);
        return pathDraw;
    }

    private Paint getCurrentAttrPaint() {
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setDither(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setColor(mColor);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setAlpha(mAlpha);

            // 设置混合模式为DST_IN
            if (mMode == Mode.PEN) {
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            } else if (mMode == Mode.ERASER) {
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            }

        }
        return mPaint;
    }

    void onDraw(Canvas canvas) {
        if (mDrawBitmap != null) {
            canvas.drawBitmap(mDrawBitmap, mHost.getImageMatrix(), null);
        }
    }

    @Override
    public IDoodler setColor(@ColorInt int color) {
        mColor = color;
        invalidatePaint();
        return this;
    }

    @Override
    public IDoodler setStrokeWidth(float width) {
        mStrokeWidth = width;
        invalidatePaint();
        return this;
    }

    @Override
    public IDoodler setAlpha(int alpha) {
        mAlpha = alpha;
        invalidatePaint();
        return this;
    }

    @Override
    public IDoodler setModel(@Mode int model) {
        mMode = model;
        invalidatePaint();
        return this;
    }

    @Override
    public IDoodler undo() {
        if (mCanvas == null || mActions.isEmpty()) {
            return this;
        }
        mCanvas.drawPaint(mClearPaint);
        mCanvas.drawColor(0x00FFFFFF);
        mActions.remove(mActions.size() - 1);
        for (Draw action : mActions) {
            action.draw();
        }
        mHost.invalidate();
        return this;
    }

    // TODO: 2016/11/21 0021 support redo
    @Override
    public IDoodler redo() {
        return this;
    }

    // TODO: 2016/11/28 0028   support type
    @Override
    public IDoodler setType(@Type int type) {
        return this;
    }

    private void invalidatePaint() {
        mPaint = null;
    }

}
