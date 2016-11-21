package com.lcl.okgallery.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lcl.okgallery.OkGallery;
import com.lcl.okgallery.R;
import com.lcl.okgallery.util.LoadPhotoTask;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2016/11/18
 * Description:
 */
public class PhotoPickerActivity extends PhotoBaseActivity {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;

    private RecyclerView recycler;
    private Button btn_confirm;  //右下角完成
    private TextView tv_preview; //左下角预览

    private OkGallery okGallery;


    /**
     * 上一次显示图片目录的时间戳，防止短时间内重复点击图片目录菜单时界面错乱
     */
    private long mLastShowPhotoFolderTime;
    private AppCompatDialog mLoadingDialog;
    private LoadPhotoTask mLoadPhotoTask;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_picker);
        recycler = $(R.id.recycler);
        btn_confirm = $(R.id.btn_confirm);
        tv_preview = $(R.id.tv_preview);
    }

    @Override
    protected void setListener() {
        btn_confirm.setOnClickListener(this);
        tv_preview.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        okGallery = OkGallery.getInstance();



    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {

        } else if (v.getId() == R.id.tv_preview) {

        }
    }
}
