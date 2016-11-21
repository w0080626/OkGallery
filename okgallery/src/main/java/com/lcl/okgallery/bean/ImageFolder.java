package com.lcl.okgallery.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2016/11/21
 * Description:图片文件夹信息实体类
 */
public class ImageFolder implements Serializable {
    public String name;
    public String coverPath;
    private ArrayList<String> mImages = new ArrayList<>();
    private boolean mTakePhotoEnabled;

    public ImageFolder(boolean takePhotoEnabled) {
        mTakePhotoEnabled = takePhotoEnabled;
        if (takePhotoEnabled) {
            // 拍照
            mImages.add("");
        }
    }

    public ImageFolder(String name, String coverPath) {
        this.name = name;
        this.coverPath = coverPath;
    }

    public boolean isTakePhotoEnabled() {
        return mTakePhotoEnabled;
    }

    public void addLastImage(String imagePath) {
        mImages.add(imagePath);
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public int getCount() {
        return mTakePhotoEnabled ? mImages.size() - 1 : mImages.size();
    }
}
