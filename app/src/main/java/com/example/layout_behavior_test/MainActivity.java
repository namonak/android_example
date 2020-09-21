package com.example.layout_behavior_test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.Nullable;

public class MainActivity extends Activity{
    MySurfaceView mMySurfaceView;
    MyTextView mMyTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMySurfaceView = new MySurfaceView(this);
        setContentView(mMySurfaceView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mMyTextView = new MyTextView(this);
        addContentView(mMyTextView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_SPACE:
                if (mMySurfaceView.mediaPlayer.isPlaying()) {
                    mMySurfaceView.mediaPlayer.pause();
                } else {
                    mMySurfaceView.mediaPlayer.start();
                }
                return true;
            case KeyEvent.KEYCODE_ENTER:
                mMyTextView.setTextSize(70);
                mMyTextView.setTextColor(0xFFFF0000);
                mMyTextView.setGravity(Gravity.CENTER);
                mMyTextView.setText("Alticast");
                return true;
            case KeyEvent.KEYCODE_DEL:
                mMyTextView.setText("");
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
