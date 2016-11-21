package com.lcl.okgallery.imageloader;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2016/11/21
 * Description: 滑动暂停加载 不支持XUtils
 */
public class RVOnScrollListener extends RecyclerView.OnScrollListener {
    private Activity mActivity;

    public RVOnScrollListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            OkGalleryImage.resume(mActivity);
        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            OkGalleryImage.pause(mActivity);
        }
    }
}
