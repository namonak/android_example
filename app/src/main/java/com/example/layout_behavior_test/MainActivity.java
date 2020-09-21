package com.example.layout_behavior_test;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.Nullable;

public class MainActivity extends Activity{
    MySurfaceView mMySurfaceView;
    MyTextView mMyTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMySurfaceView = new MySurfaceView(this);
        setContentView(mMySurfaceView);

        mMyTextView = new MyTextView(this);
        addContentView(mMyTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
}
