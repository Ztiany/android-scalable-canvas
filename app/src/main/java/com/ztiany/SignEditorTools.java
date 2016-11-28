package com.ztiany;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.image.DoodleView;
import com.image.Mode;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

///////////////////////////////////////////////////////////////////////////
// UIController
///////////////////////////////////////////////////////////////////////////
final class SignEditorTools {

    @BindViews({R.id.design_iv_eraser, R.id.design_iv_brush, R.id.design_iv_drag})
    List<ImageView> mOperationIvs;
    @BindView(R.id.design_sb_eraser_width)
    SeekBar mEraserWidthSb;
    @BindView(R.id.design_ll_brush_content)
    View mBrushContentView;
    @BindView(R.id.design_iv_brush_red)
    ImageView mRedBrushIv;
    @BindView(R.id.design_iv_brush_black)
    ImageView mBlackBrushIv;
    @BindView(R.id.design_iv_save)
    ImageView mSaveIv;

    private int mSelectedOperationId;
    private int mSelectedColorId;
    private View mLayoutView;
    private DoodleView mDoodleView;

    SignEditorTools(View layout, DoodleView doodleView) {
        mLayoutView = layout;
        mDoodleView = doodleView;
        init();
    }


    private void init() {
        ButterKnife.bind(this, mLayoutView);
        mEraserWidthSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDoodleView.getDoodler().setStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //橡皮擦的范围
        mEraserWidthSb.setMax(40);
        mEraserWidthSb.setProgress(20);


        //默认选择手势缩放模式
        mOperationIvs.get(2).setSelected(true);
        mSelectedOperationId = mOperationIvs.get(2).getId();
        mRedBrushIv.setSelected(true);

    }

    @OnClick({R.id.design_iv_brush_red, R.id.design_iv_brush_black})
    void onSelectedColor(View view) {
        if (mSelectedColorId == view.getId()) {
            return;
        }
        mSelectedColorId = view.getId();
        mRedBrushIv.setSelected(mRedBrushIv == view);
        mBlackBrushIv.setSelected(mBlackBrushIv == view);
        startDoodle();
    }


    private void startDoodle() {
        mDoodleView.startDoodle();
        int color = mBlackBrushIv.isSelected() ? Color.BLACK : Color.RED;
        mDoodleView.getDoodler().setModel(Mode.PEN).setColor(color).setStrokeWidth(1.5F);
    }

    @OnClick({R.id.design_iv_eraser, R.id.design_iv_brush, R.id.design_iv_drag})
    void onClickAction(View view) {
        if (mSelectedOperationId == view.getId()) {
            return;
        }
        mSelectedOperationId = view.getId();
        for (ImageView operationIv : mOperationIvs) {
            if (operationIv.getId() == view.getId()) {
                operationIv.setSelected(true);
            } else {
                operationIv.setSelected(false);
            }
        }

        switch (view.getId()) {
            case R.id.design_iv_eraser: {

                mDoodleView.startDoodle();
                mDoodleView.getDoodler()
                        .setModel(Mode.ERASER)
                        .setStrokeWidth(mEraserWidthSb.getProgress());

                mEraserWidthSb.setVisibility(VISIBLE);
                mBrushContentView.setVisibility(INVISIBLE);
                break;
            }
            case R.id.design_iv_brush: {
                mEraserWidthSb.setVisibility(INVISIBLE);
                mBrushContentView.setVisibility(VISIBLE);
                startDoodle();
                break;
            }
            case R.id.design_iv_drag: {
                mDoodleView.startGestures();
                mEraserWidthSb.setVisibility(INVISIBLE);
                mBrushContentView.setVisibility(INVISIBLE);
                break;
            }
        }
    }

    void undo() {
        mDoodleView.getDoodler().undo();
    }
}
