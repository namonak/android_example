package com.example.capture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends Activity implements TextureView.SurfaceTextureListener, View.OnClickListener {
    MediaPlayer mediaPlayer;
    TextureView textureView;
    ImageView imageView;
    Button captureButton;
    Button textureViewButton;
    public static String TAG = "MainActivity";
    public static String IMG = "/Pictures/TEST.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        textureView = findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

        captureButton = findViewById(R.id.captureButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                getBitmap(textureView);
            }
        });

        textureViewButton = findViewById(R.id.textureViewButton);
        textureViewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                drawBitmap();
            }
        });
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        Log.d("MyTag","surfaceCreated");
        Surface surfaceTexture = new Surface(surface);

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {

            String path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
            mediaPlayer.setDataSource(path);

            //mediaPlayer.setVolume(0, 0); //볼륨 제거
            //mediaPlayer.setDisplay(surfaceHolder); // 화면 호출
            mediaPlayer.setSurface(surfaceTexture);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare(); // 비디오 load 준비

            //mediaPlayer.setOnCompletionListener(completionListener); // 비디오 재생 완료 리스너

            mediaPlayer.start();

        } catch (Exception e) {
            Log.e("MyTag","surface view error : " + e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        Log.e("MyTag","surfaceDestroyed");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    @Override
    public void onClick(View v) {

    }

    public void getBitmap(TextureView vv)
    {
        String mPath = Environment.getExternalStorageDirectory().toString()
                + IMG;
        Log.d(TAG, mPath);

        Bitmap bm = vv.getBitmap();
        if(bm == null) {
            Log.e(TAG, "bitmap is null");
        }

        OutputStream file_out;
        File imageFile = new File(mPath);

        try {
            file_out = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.PNG, 90, file_out);
            file_out.flush();
            file_out.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IOException");
            e.printStackTrace();
        }
    }

    public void drawBitmap() {
        String mPath = Environment.getExternalStorageDirectory().toString()
                + IMG;
        Log.d(TAG, mPath);

        Uri uri = Uri.fromFile(new File(mPath));
        Bitmap bmp = BitmapFactory.decodeFile(uri.getPath());
        imageView.setImageBitmap(bmp);
    }
}