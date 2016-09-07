package com.example.szh.mytest;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by moram on 2016/9/7.
 */
public class TestActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }


    @Override
    protected void initdata() {
        super.initdata();
    }

    @Override
    protected void findviews() {
        super.findviews();
    }

    @Override
    protected void bindviews() {
        super.bindviews();
    }

    @Override
    protected void setListener() {
        super.setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View findViewById(@IdRes int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
