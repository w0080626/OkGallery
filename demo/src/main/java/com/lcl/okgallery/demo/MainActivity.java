package com.lcl.okgallery.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lcl.okgallery.OkGallery;
import com.lcl.okgallery.activity.PhotoPickerActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private Button btn_openGallery;
    private CheckBox cb_show_camera, cb_pause_on_scroll;
    private SeekBar sb_select_limit;
    private TextView tv_select_limit;
    private OkGallery okGallery;
    private RadioButton rb_muti_select, rb_single_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okGallery = OkGallery.getInstance();
        initView();
        setListener();
    }

    private void initView() {
        btn_openGallery = $(R.id.btn_open_gallery);
        cb_show_camera = $(R.id.cb_show_camera);
        sb_select_limit = $(R.id.sb_select_limit);
        tv_select_limit = $(R.id.tv_select_limit);
        rb_muti_select = $(R.id.rb_muti_select);
        rb_single_select = $(R.id.rb_single_select);
        cb_pause_on_scroll = $(R.id.cb_pause_on_scroll);
    }

    private void setListener() {
        btn_openGallery.setOnClickListener(this);
        sb_select_limit.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_gallery:
                openGallery();
                break;
        }
    }

    private void openGallery() {
        okGallery.setMultiMode(rb_muti_select.isChecked());
        okGallery.setShowCamera(cb_show_camera.isChecked());
        okGallery.setPauseOnScroll(cb_pause_on_scroll.isChecked());
        startActivity(new Intent(this, PhotoPickerActivity.class));
    }

    @Nullable
    private <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (i > 0) {
            tv_select_limit.setText(String.valueOf(i));
            okGallery.setSelectLimit(i);
        } else {
            tv_select_limit.setText("1");
            okGallery.setSelectLimit(1);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
