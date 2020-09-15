package com.example.capture;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class MainActivity extends Activity implements TextureView.SurfaceTextureListener, View.OnClickListener {
    MediaPlayer mediaPlayer;
    TextureView textureView;
    ImageView imageView;
    Button captureButton;
    Button textureViewButton;

    private Messenger mServiceMessenger = null;
    private boolean mIsBound;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStartService();

        imageView = findViewById(R.id.imageView);

        textureView = findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

        captureButton = findViewById(R.id.captureButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessageToService(CaptureManagerService.MSG_STORE_BMP_TO_SERVICE, Util.setBitmapWithBundle(textureView.getBitmap()));
            }
        });

        textureViewButton = findViewById(R.id.textureViewButton);
        textureViewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessageToService(CaptureManagerService.MSG_LOAD_BMP_TO_ACTIVITY, null);
            }
        });
    }

    /** 서비스 시작 및 Messenger 전달 */
    private void setStartService() {
        startService(new Intent(MainActivity.this, CaptureManagerService.class));
        bindService(new Intent(this, CaptureManagerService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    /** 서비스 정지 */
    private void setStopService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        stopService(new Intent(MainActivity.this, CaptureManagerService.class));
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG,"onServiceConnected");
            mServiceMessenger = new Messenger(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG,"onServiceDisconnected");
            setStopService();
            mServiceMessenger = null;
        }
    };

    /** 서비스로 부터 메시지를 받음 */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.d(TAG, "Message from CaptureManagerService msg.what : " + Util.getCommandStirng(msg.what));
            switch (msg.what) {
                case CaptureManagerService.MSG_STORE_BMP_TO_SERVICE:
                    Log.d(TAG, "do nothing, not yet...");
                    break;
                case CaptureManagerService.MSG_LOAD_BMP_TO_ACTIVITY:
                    Log.d(TAG, "Draw Bitmap");
                    drawBitmap(msg.getData().getByteArray("BitmapFromActivity"));
                    break;
            }
            return false;
        }
    }));

    /** 서비스로 메시지를 보냄 */
    private void sendMessageToService(int cmd, Bundle bundle) {
        if (mIsBound && mServiceMessenger != null) {
            try {
                    Message msg = Message.obtain(null, cmd);
                    if (bundle != null) {
                        msg.setData(bundle);
                    }
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        Log.d(TAG,"surfaceCreated");
        Surface surfaceTexture = new Surface(surface);

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {
            String path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
            mediaPlayer.setDataSource(path);
            mediaPlayer.setSurface(surfaceTexture);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare(); // 비디오 load 준비
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG,"surface view error : " + e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        Log.e(TAG,"surfaceDestroyed");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        setStopService();

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    @Override
    public void onClick(View v) {

    }

    public void drawBitmap(byte[] byteArray) {
        Bitmap bmp = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        imageView.setImageBitmap(bmp);
    }
}