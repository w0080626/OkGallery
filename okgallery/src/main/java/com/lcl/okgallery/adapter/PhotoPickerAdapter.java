package com.lcl.okgallery.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.lcl.okgallery.R;
import com.lcl.okgallery.bean.ImageFolder;
import com.lcl.okgallery.imageloader.OkGalleryImage;
import com.lcl.okgallery.util.OkGalleryUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2016/11/22
 * Description:
 */
public class PhotoPickerAdapter extends BGARecyclerViewAdapter<String> {
    private List<String> mSelectedImages = new ArrayList<>();
    private int mImageWidth;
    private int mImageHeight;
    private boolean mTakePhotoEnabled;
    private Activity mActivity;
    private boolean mIsMultiMode;

    public PhotoPickerAdapter(Activity activity, RecyclerView recyclerView,boolean isMultiMode) {
        super(recyclerView, R.layout.item_photo_picker);
        mImageWidth = OkGalleryUtil.getScreenWidth(recyclerView.getContext()) / 6;
        mImageHeight = mImageWidth;
        mActivity = activity;
        mIsMultiMode = isMultiMode;
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper helper) {
        helper.setItemChildClickListener(R.id.iv_item_photo_picker_flag);
        helper.setItemChildClickListener(R.id.iv_item_photo_picker_photo);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, String model) {
        if (mTakePhotoEnabled && position == 0) {
            helper.setTag(R.id.iv_item_photo_picker_photo, null);

            helper.getImageView(R.id.iv_item_photo_picker_photo).setScaleType(ImageView.ScaleType.CENTER);
            helper.setImageResource(R.id.iv_item_photo_picker_photo, R.drawable.ic_camera);

            helper.setVisibility(R.id.iv_item_photo_picker_flag, View.INVISIBLE);
            helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(null);
        } else {
            helper.getImageView(R.id.iv_item_photo_picker_photo).setScaleType(ImageView.ScaleType.CENTER_CROP);
            OkGalleryImage.displayImage(mActivity, helper.getImageView(R.id.iv_item_photo_picker_photo), model, R.drawable.ic_default_img, R.drawable.ic_default_img, mImageWidth, mImageHeight, null);

            if (mIsMultiMode){
                helper.setVisibility(R.id.iv_item_photo_picker_flag, View.VISIBLE);
                if (mSelectedImages.contains(model)) {
                    helper.setImageResource(R.id.iv_item_photo_picker_flag, R.drawable.ic_checked);
                    helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(helper.getConvertView().getResources().getColor(R.color.photo_selected_mask));
                } else {
                    helper.setImageResource(R.id.iv_item_photo_picker_flag, R.drawable.ic_normal);
                    helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(null);
                }
            }else{
                helper.setVisibility(R.id.iv_item_photo_picker_flag, View.GONE);
            }
        }
    }

    public void setSelectedImages(List<String> selectedImages) {
        if (selectedImages != null) {
            mSelectedImages = selectedImages;
        }
        notifyDataSetChanged();
    }

    public List<String> getSelectedImages() {
        return mSelectedImages;
    }

    public int getSelectedCount() {
        return mSelectedImages.size();
    }

    public void setImageFolderModel(ImageFolder imageFolder) {
        mTakePhotoEnabled = imageFolder.isTakePhotoEnabled();
        setData(imageFolder.getImages());
    }
}
