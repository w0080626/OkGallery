package com.lcl.okgallery.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2016/11/21
 * Description:
 */
public abstract class ImageLoader {

    protected String getPath(String path) {
        if (path == null) {
            path = "";
        }

        if (!path.startsWith("http") && !path.startsWith("file")) {
            path = "file://" + path;
        }
        return path;
    }

    public abstract void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, DisplayDelegate delegate);

    public abstract void downloadImage(Context context, String path, DownloadDelegate delegate);

    public abstract void pause(Activity activity);

    public abstract void resume(Activity activity);

    public interface DisplayDelegate {
        void onSuccess(View view, String path);
    }

    public interface DownloadDelegate {
        void onSuccess(String path, Bitmap bitmap);

        void onFailed(String path);
    }
}
