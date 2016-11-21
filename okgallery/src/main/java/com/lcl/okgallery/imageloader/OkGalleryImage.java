package com.lcl.okgallery.imageloader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2016/11/21
 * Description:自动选择图片加载器 感谢王浩 https://github.com/bingoogolapple 提供的解决方案
 */
public class OkGalleryImage {
    private static ImageLoader sImageLoader;


    private OkGalleryImage() {

    }

    private static final ImageLoader getImageLoader() {
        if (sImageLoader == null) {
            synchronized (OkGalleryImage.class) {
                if (sImageLoader == null) {
                    if (isClassExists("com.bumptech.glide.Glide")) {
                        sImageLoader = new GlideImageLoader();
                    } else if (isClassExists("com.squareup.picasso.Picasso")) {
                        sImageLoader = new PicassoImageLoader();
                    } else if (isClassExists("com.nostra13.universalimageloader.core.ImageLoader")) {
                        sImageLoader = new UILImageLoader();
                    } else if (isClassExists("org.xutils.x")) {
                        sImageLoader = new XUtilsImageLoader();
                    } else {
                        throw new RuntimeException("必须在你的build.gradle文件中配置「Glide、Picasso、universal-image-loader、XUtils3」中的某一个图片加载库的依赖");
                    }
                }
            }
        }
        return sImageLoader;
    }

    private static final boolean isClassExists(String classFullName) {
        try {
            Class.forName(classFullName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, final ImageLoader.DisplayDelegate delegate) {
        getImageLoader().displayImage(activity, imageView, path, loadingResId, failResId, width, height, delegate);
    }

    public static void downloadImage(Context context, String path, final ImageLoader.DownloadDelegate delegate) {
        getImageLoader().downloadImage(context, path, delegate);
    }

    public static void pause(Activity activity) {
        getImageLoader().pause(activity);
    }

    public static void resume(Activity activity) {
        getImageLoader().resume(activity);
    }


}
