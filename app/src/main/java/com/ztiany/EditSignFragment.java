package com.ztiany;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <br/>   Description：编辑标记，SignInfo中有designId表示修改，执行update操作；没有designId(= 0)表示添加标记，执行save操作。
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2016-11-18 15:47
 */

public class EditSignFragment extends Fragment {

    private static final String EDIT_SIGN_KEY = "edit_sign_key";

    @BindView(R.id.design_dv_doodle)
    DoodleView mDoodleView;

    @Inject
    EditSignContract.Presenter mPresenter;


    boolean mNeedPromptSave;//modify by mSignEditorTools
    private String mNotes;//记录备注
    private SignInfo mSignInfo;//自主设计参数
    private EditSignFragmentCallback mShowSignFragmentCallback;
    private File mSignPicture;


    public static EditSignFragment newInstance(SignInfo signInfo) {
        Bundle args = new Bundle();
        args.putParcelable(EDIT_SIGN_KEY, signInfo);
        EditSignFragment fragment = new EditSignFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mShowSignFragmentCallback = FragmentUtils.checkContextImplement(this, EditSignFragmentCallback.class);

        InjectHelper.getActivityComponent(this, CustomDesignComponent.class)
                .inject(this);
        mSignInfo = getArguments().getParcelable(EDIT_SIGN_KEY);
        MvpDelegate.attach(this, mPresenter);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.add(R.string.design_add_notes)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        showAddNotesDialog();
                        return true;
                    }
                });

        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
    }

    private void showAddNotesDialog() {
        DialogManager.showEnterContentDialog(
                getContext(),
                R.string.Confirm,
                R.string.Cancel,
                R.string.design_picture_notes,
                R.string.design_notes_tips,
                mNotes,
                new DialogManager.OnConfirmContentListener() {
                    @Override
                    public void onConfirm(Dialog dialog, String content) {
                        mNeedPromptSave = !TextUtils.equals(content, mNotes);
                        mNotes = content;
                        dialog.dismiss();
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentUtils.callFragmentResume(this, getString(R.string.design_personalized_sign));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.design_fragment_sign_editor, container, false);
    }

    @Override
    public boolean onBackPressed() {
        if (mNeedPromptSave) {
            showConfigExitSignDialog();
            return true;
        }
        return super.onBackPressed();
    }

    private void showConfigExitSignDialog() {

        DialogManager.showOkCancelDialog(getContext(), R.string.Confirm, R.string.Cancel,
                R.string.design_confirm_exit_sign_tips, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        view.findViewById(R.id.design_iv_sign_close)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View parent = (View) v.getParent();
                        parent.animate().translationY(-parent.getMeasuredHeight());
                    }
                });
        view.findViewById(R.id.design_iv_guide)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSignGuide();
                    }
                });
        View signToolsLayout = view.findViewById(R.id.design_view_sign_tools);

        new SignEditorTools(this, signToolsLayout, mDoodleView);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mSignInfo.hasDesignId()) {//有设计id表示是修改
            mNotes = mSignInfo.getNotes();
        }

        //加载图片
        ImageLoaderUtils.loadBitmapWithImageView(this, Source.create(mSignInfo.getModelSource()), true, mDoodleView
                , new ImageViewLoadListener() {
                    @Override
                    public void onLoadSuccess(ImageView imageView, Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadFail(ImageView imageView, Exception e) {
                        // TODO: 2016/11/23 0023
                    }
                });


    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewBehavior
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public String getDesignId() {
        return mSignInfo.getDesignId();
    }

    @Override
    public void showLoading(boolean cancelable) {
        showLoadingDialog().setCancelable(cancelable);
    }


    @Override
    public void showUpdateSignSuccess() {
        processSaveUpdateSignSuccess();
    }

    @Override
    public void showSaveSignSuccess(DesignSignId designId) {
        String signDesignId = designId.getMydesign_id();
        mSignInfo.setDesignId(signDesignId);//记录生成的id，下一次保存就是更新操作
        processSaveUpdateSignSuccess();
    }

    private void processSaveUpdateSignSuccess() {
        dismissLoadingDialog();
        mNeedPromptSave = false;
        showPrompt(getString(R.string.design_save_sign_success));
        //通知上一个界面
        mShowSignFragmentCallback.onSaveSignSuccess(mSignInfo);


    }

    @Override
    public void showSaveUpdateSignFail(String message) {
        dismissLoadingDialog();
        showPrompt(message);
    }

    @Override
    public void showLoadGuideListFail(String message) {
        dismissLoadingDialog();
        showPrompt(message);
    }

    @Override
    public String getNotes() {
        return mNotes;
    }

    @Override
    public String getFabricId() {
        return mSignInfo.getFabricId();
    }

    @Override
    public int getModelIndex() {
        return mSignInfo.getModelIndex();
    }

    @Override
    public String getGoodsId() {
        return mSignInfo.getGoodsId();
    }

    @Override
    public List<StyleIdPairVO> getStyleIdList() {
        return mSignInfo.getStyleIdList();
    }

    @Override
    public void showGuides(List<String> strings) {
        dismissLoadingDialog();
        DesignHelperDialogFragment.newInstance(new ArrayList<>(strings)).show(getChildFragmentManager(), DesignHelperDialogFragment.class.getName());
    }


    public interface EditSignFragmentCallback {

        /**
         * 标记保存成功
         */
        void onSaveSignSuccess(SignInfo signRecord);

    }


    ///////////////////////////////////////////////////////////////////////////
    // saveBitmap
    ///////////////////////////////////////////////////////////////////////////
    void saveSign(Bitmap bitmap) {

        final String tempPictureSavePath = AppDirectoryManager.createTempPictureSavePath(AppDirectoryManager.PICTURE_FORMAT_JPEG);
        try {

            //临时图片
            mSignPicture = new File(tempPictureSavePath);
            final FileOutputStream fileOutputStream = new FileOutputStream(tempPictureSavePath);//临时图片流

            Observable.just(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream))
                    .compose(this.<Boolean>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AppContext.getAppComponent().mainThreadSchedulers())
                    .doOnTerminate(new Action0() {
                        @Override
                        public void call() {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean success) {
                            if (success) {
                                doSaveSign(mSignPicture);
                            } else {
                                showPrompt(getString(R.string.design_save_sign_fail));
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            showPrompt(getString(R.string.design_save_sign_fail));
                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void doSaveSign(File signPicture) {

        if (mSignInfo.hasDesignId()) {//有id，更新
            mPresenter.updateSign(signPicture);
        } else {//没有id，保存
            mPresenter.saveSign(signPicture);
        }
    }


    /**
     * 获取标记指引
     */
    private void showSignGuide() {
        mPresenter.getSignGuideList();
    }
}
