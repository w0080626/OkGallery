package com.lcl.okgallery.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcl.okgallery.OkGallery;
import com.lcl.okgallery.R;
import com.lcl.okgallery.adapter.PhotoPickerAdapter;
import com.lcl.okgallery.bean.ImageFolder;
import com.lcl.okgallery.imageloader.RVOnScrollListener;
import com.lcl.okgallery.util.LoadPhotoTask;
import com.lcl.okgallery.util.OkGalleryAsyncTask;
import com.lcl.okgallery.util.OkGallerySpaceItemDecoration;
import com.lcl.okgallery.view.PhotoFolderPw;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2016/11/18
 * Description:
 */
public class PhotoPickerActivity extends PhotoBaseActivity implements OkGalleryAsyncTask.Callback<ArrayList<ImageFolder>>, BGAOnItemChildClickListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;

    private RecyclerView recycler;
    private Button btn_confirm;  //右下角完成
    private TextView tv_preview; //左下角预览
    private TextView tv_title;  //标题
    private ImageView iv_arrow; //箭头
    private ImageView iv_back; //返回
    private RelativeLayout topBar; //toolbar
    private FrameLayout bottomBar;

    private OkGallery okGallery;

    private PhotoPickerAdapter mAdapter;
    /**
     * 图片目录数据集合
     */
    private ArrayList<ImageFolder> mImageFolders;
    private ImageFolder mCurrentImageFolder;
    private PhotoFolderPw mPhotoFolderPw;
    /**
     * 上一次显示图片目录的时间戳，防止短时间内重复点击图片目录菜单时界面错乱
     */
    private long mLastShowPhotoFolderTime;
    private AppCompatDialog mLoadingDialog;
    private LoadPhotoTask mLoadPhotoTask;

    @Override
    protected void onStart() {
        super.onStart();
        showLoadingDialog();
        mLoadPhotoTask = new LoadPhotoTask(this, this, okGallery.isShowCamera()).perform();
    }

    private void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new AppCompatDialog(this);
            mLoadingDialog.setContentView(R.layout.dialog_loading);
            mLoadingDialog.setCancelable(false);
        }
        mLoadingDialog.show();
    }

    private void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_picker);
        okGallery = OkGallery.getInstance();

        recycler = $(R.id.recycler);
        btn_confirm = $(R.id.btn_confirm);
        tv_preview = $(R.id.tv_preview);
        tv_title = $(R.id.tv_title);
        iv_arrow = $(R.id.iv_arrow);
        iv_back = $(R.id.iv_back);
        topBar = $(R.id.topBar);
        bottomBar = $(R.id.bottomBar);
    }

    @Override
    protected void setListener() {
        btn_confirm.setOnClickListener(this);
        tv_preview.setOnClickListener(this);
        tv_title.setOnClickListener(this);
        iv_arrow.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        mAdapter = new PhotoPickerAdapter(this, recycler, okGallery.isMultiMode());
        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, OkGallerySpaceItemDecoration.SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new OkGallerySpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.OkGallery_photo_divider)));

        if (okGallery.isPauseOnScroll()) {
            recycler.addOnScrollListener(new RVOnScrollListener(this));
        }
        recycler.setAdapter(mAdapter);

        List<String> selectedImages = okGallery.getSelectedImages();
        if (selectedImages != null && selectedImages.size() > okGallery.getSelectLimit()) {
            selectedImages = selectedImages.subList(0, okGallery.getSelectLimit());
        }
        mAdapter.setSelectedImages(selectedImages);

        tv_title.setText(R.string.OkGallery_all_image);
        if (mCurrentImageFolder != null) {
            tv_title.setText(mCurrentImageFolder.name);
        }
        bottomBar.setVisibility(okGallery.isMultiMode() ? View.VISIBLE : View.GONE);
        updateConfirmBtnStatus();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            showToast(mAdapter.getSelectedImages().toString());
        } else if (v.getId() == R.id.tv_preview) {

        } else if ((v.getId() == R.id.tv_title || v.getId() == R.id.iv_arrow) && mImageFolders != null && mImageFolders.size() > 0 && System.currentTimeMillis() - mLastShowPhotoFolderTime > PhotoFolderPw.ANIM_DURATION) {
            showPhotoFolderPw();
        } else if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    private void showPhotoFolderPw() {
        if (mPhotoFolderPw == null) {
            mPhotoFolderPw = new PhotoFolderPw(this, topBar, new PhotoFolderPw.Delegate() {
                @Override
                public void onSelectedFolder(int position) {
                    reloadPhotos(position);
                }

                @Override
                public void executeDismissAnim() {
                    ViewCompat.animate(iv_arrow).setDuration(PhotoFolderPw.ANIM_DURATION).rotation(0).start();
                }
            });
        }
        mPhotoFolderPw.setData(mImageFolders);
        mPhotoFolderPw.show();

        ViewCompat.animate(iv_arrow).setDuration(PhotoFolderPw.ANIM_DURATION).rotation(-180).start();
    }

    /**
     * 更新完成按钮
     */
    private void updateConfirmBtnStatus() {
        if (mAdapter.getSelectedCount() == 0) {
            btn_confirm.setEnabled(false);
            tv_preview.setText(getString(R.string.OkGallery_preview));
        } else {
            btn_confirm.setEnabled(true);
            tv_preview.setText(getString(R.string.OkGallery_preview) + "(" + mAdapter.getSelectedCount() + "/" + okGallery.getSelectLimit() + ")");
        }
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View view, int position) {
        if (view.getId() == R.id.iv_item_photo_picker_flag) {
            handleClickSelectFlagIv(position);
        } else if (view.getId() == R.id.iv_item_photo_picker_photo) {
            if (okGallery.isMultiMode()) {
                showToast("进入预览");
            } else {
                showToast("返回图片 " + mCurrentImageFolder.getImages().get(position));

            }
        }
    }

    /**
     * 处理点击选择按钮事件
     *
     * @param position 当前点击的item的索引位置
     */
    private void handleClickSelectFlagIv(int position) {
        String currentImage = mAdapter.getItem(position);
        if (!mAdapter.getSelectedImages().contains(currentImage) && mAdapter.getSelectedCount() == okGallery.getSelectLimit()) {
            toastMaxCountTip();
        } else {
            if (mAdapter.getSelectedImages().contains(currentImage)) {
                mAdapter.getSelectedImages().remove(currentImage);
            } else {
                mAdapter.getSelectedImages().add(currentImage);
            }
            mAdapter.notifyItemChanged(position);
            updateConfirmBtnStatus();
        }
    }

    @SuppressLint("StringFormatMatches")
    private void toastMaxCountTip() {
        showToast(getString(R.string.OkGallery_toast_checked_max, okGallery.getSelectLimit()));
    }

    private void reloadPhotos(int position) {
        if (position < mImageFolders.size()) {
            mCurrentImageFolder = mImageFolders.get(position);
            if (tv_title != null) {
                tv_title.setText(mCurrentImageFolder.name);
            }
            mAdapter.setImageFolderModel(mCurrentImageFolder);
        }
    }

    @Override
    public void onPostExecute(ArrayList<ImageFolder> imageFolders) {
        dismissLoadingDialog();
        mLoadPhotoTask = null;
        mImageFolders = imageFolders;
        reloadPhotos(mPhotoFolderPw == null ? 0 : mPhotoFolderPw.getCurrentPosition());
    }

    @Override
    public void onTaskCancelled() {
        dismissLoadingDialog();
        mLoadPhotoTask = null;
    }

    private void cancelLoadPhotoTask() {
        if (mLoadPhotoTask != null) {
            mLoadPhotoTask.cancelTask();
            mLoadPhotoTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        dismissLoadingDialog();
        cancelLoadPhotoTask();

        okGallery.clearSelectedImages();
        topBar = null;
        bottomBar = null;
        tv_title = null;
        iv_back = null;
        iv_arrow = null;
        btn_confirm = null;
        tv_preview = null;
        recycler = null;
        mAdapter = null;
        mImageFolders = null;
        mPhotoFolderPw = null;
        mCurrentImageFolder = null;
        super.onDestroy();
    }
}
