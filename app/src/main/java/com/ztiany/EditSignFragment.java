package com.ztiany;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.image.DoodleView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2016-11-18 15:47
 */
public class EditSignFragment extends Fragment {

    @BindView(R.id.design_dv_doodle)
    DoodleView mDoodleView;
    private SignEditorTools mSignEditorTools;

    public static EditSignFragment newInstance() {
        return new EditSignFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.add("undo")
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mSignEditorTools.undo();
                        return true;
                    }
                });

        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.design_fragment_sign_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        View signToolsLayout = view.findViewById(R.id.design_view_sign_tools);
        mSignEditorTools = new SignEditorTools(signToolsLayout, mDoodleView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(this)
                .load("http://f.hiphotos.baidu.com/image/pic/item/b151f8198618367a9f738e022a738bd4b21ce573.jpg")
                .into(mDoodleView);
    }


}
