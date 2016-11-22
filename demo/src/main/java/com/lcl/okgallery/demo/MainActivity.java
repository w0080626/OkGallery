package com.lcl.okgallery.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lcl.okgallery.OkGallery;
import com.lcl.okgallery.activity.PhotoPickerActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_openGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }

    private void initView() {
        btn_openGallery = $(R.id.openGallery_btn);
    }

    private void setListener() {
        btn_openGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openGallery_btn:
                openGallery();
                break;
        }
    }

    private void openGallery() {
        OkGallery okGallery = OkGallery.getInstance();
        okGallery.setSelectLimit(9);
        okGallery.setPauseOnScroll(false);
        okGallery.setShowCamera(true);
        startActivity(new Intent(this, PhotoPickerActivity.class));
    }

    @Nullable
    private <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

}
