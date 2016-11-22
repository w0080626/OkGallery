package com.lcl.okgallery.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lcl.okgallery.R;
import com.lcl.okgallery.bean.ImageFolder;
import com.lcl.okgallery.imageloader.OkGalleryImage;
import com.lcl.okgallery.util.OkGalleryUtil;

import java.util.ArrayList;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2016/11/22
 * Description:
 */
public class PhotoFolderPw extends PopupWindow implements View.OnClickListener, BGAOnRVItemClickListener {
    public static final int ANIM_DURATION = 300;

    private Activity mActivity;
    private View mWindowRootView;
    private View mAnchorView;

    private LinearLayout mRootLl;
    private RecyclerView mContentRv;
    private FolderAdapter mFolderAdapter;
    private Delegate mDelegate;
    private int mCurrentPosition;

    public PhotoFolderPw(Activity activity, View anchorView, Delegate delegate) {
        super(View.inflate(activity, R.layout.pw_photo_folder, null), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);

        mDelegate = delegate;

        init(activity, anchorView);
        initView();
        setListener();
        processLogic();
    }

    private void init(Activity activity, View anchorView) {
        getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        // 如果想让在点击别的地方的时候 关闭掉弹出窗体 一定要记得给mPopupWindow设置一个背景资源
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAnchorView = anchorView;
        mActivity = activity;
        mWindowRootView = activity.getWindow().peekDecorView();
    }

    private void initView() {
        mRootLl = $(R.id.ll_photo_folder_root);
        mContentRv = $(R.id.recycler_photo_folder);
    }

    private void setListener() {
        mRootLl.setOnClickListener(this);
        mFolderAdapter = new FolderAdapter(mContentRv);
        mFolderAdapter.setOnRVItemClickListener(this);
    }

    private void processLogic() {
        setAnimationStyle(android.R.style.Animation);
        setBackgroundDrawable(new ColorDrawable(0x90000000));

        mContentRv.setLayoutManager(new LinearLayoutManager(mActivity));
        mContentRv.setAdapter(mFolderAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_photo_folder_root) {
            dismiss();
        }
    }

    /**
     * 设置目录数据集合
     *
     * @param datas
     */
    public void setData(ArrayList<ImageFolder> datas) {
        mFolderAdapter.setData(datas);
    }

    public void show() {
        showAsDropDown(mAnchorView);
        ViewCompat.animate(mContentRv).translationY(-mWindowRootView.getHeight()).setDuration(0).start();
        ViewCompat.animate(mContentRv).translationY(0).setDuration(ANIM_DURATION).start();
        ViewCompat.animate(mRootLl).alpha(0).setDuration(0).start();
        ViewCompat.animate(mRootLl).alpha(1).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void dismiss() {
        ViewCompat.animate(mContentRv).translationY(-mWindowRootView.getHeight()).setDuration(ANIM_DURATION).start();
        ViewCompat.animate(mRootLl).alpha(1).setDuration(0).start();
        ViewCompat.animate(mRootLl).alpha(0).setDuration(ANIM_DURATION).start();

        if (mDelegate != null) {
            mDelegate.executeDismissAnim();
        }

        mContentRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                PhotoFolderPw.super.dismiss();
            }
        }, ANIM_DURATION);
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if (mDelegate != null && mCurrentPosition != position) {
            mDelegate.onSelectedFolder(position);
        }
        mCurrentPosition = position;
        dismiss();
    }

    private class FolderAdapter extends BGARecyclerViewAdapter<ImageFolder> {
        private int mImageWidth;
        private int mImageHeight;

        public FolderAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_photo_folder);

            mData = new ArrayList<>();
            mImageWidth = OkGalleryUtil.getScreenWidth(mActivity) / 10;
            mImageHeight = mImageWidth;
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, ImageFolder model) {
            helper.setText(R.id.tv_item_photo_folder_name, model.name);
            helper.setText(R.id.tv_item_photo_folder_count, String.valueOf(model.getCount()));
            OkGalleryImage.displayImage(mActivity, helper.getImageView(R.id.iv_item_photo_folder_photo), model.coverPath, R.drawable.ic_default_img, R.drawable.ic_default_img, mImageWidth, mImageHeight, null);
        }
    }

    private <VT extends View> VT $(@IdRes int id) {
        return (VT) getContentView().findViewById(id);
    }

    public interface Delegate {
        void onSelectedFolder(int position);

        void executeDismissAnim();
    }
}
