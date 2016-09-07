package com.example.szh.mytest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        initdata();

        findviews();

        bindviews();

        setListener();
    }

    protected void initdata() {

    }

    protected void findviews() {

    }

    protected void bindviews() {

    }

    protected void setListener() {

    }
}
