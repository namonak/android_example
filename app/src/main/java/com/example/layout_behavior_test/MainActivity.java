package com.example.layout_behavior_test;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.Nullable;

public class MainActivity extends Activity{
    VideoView videoView;
    VideoOverlayView videoOverlayView;
    GraphicView graphicView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoView = new VideoView(this);
        setContentView(videoView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        videoOverlayView = new VideoOverlayView(this);
        addContentView(videoOverlayView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        graphicView = new GraphicView(this);
        addContentView(graphicView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // Set transparency in GraphicView
        graphicView.setZOrderOnTop(true);
        SurfaceHolder holderTransparent = graphicView.getHolder();
        holderTransparent.setFormat(PixelFormat.TRANSPARENT);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_SPACE:
                if (videoView.mediaPlayer.isPlaying()) {
                    videoView.mediaPlayer.pause();
                } else {
                    videoView.mediaPlayer.start();
                }
                return true;
            case KeyEvent.KEYCODE_ENTER:
                videoOverlayView.setTextSize(70);
                videoOverlayView.setTextColor(0xFFFF0000);
                videoOverlayView.setGravity(Gravity.CENTER);
                videoOverlayView.setText("Alticast");
                return true;
            case KeyEvent.KEYCODE_DEL:
                videoOverlayView.setText("");
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
