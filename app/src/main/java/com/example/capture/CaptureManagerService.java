package com.example.capture;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CaptureManagerService extends Service {
    public static final int MSG_STORE_BMP_TO_SERVICE = 0;
    public static final int MSG_LOAD_BMP_TO_ACTIVITY = 1;

    private Messenger mClient = null;   // Activity 에서 가져온 Messenger

    public static final String TAG = "CaptureManagerService";
    public static final String IMG = "/Pictures/TEST.png";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    /** Activity로 부터 binding 된 Messenger **/
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.d(TAG, "Message from MainActivity msg.what : " + Util.getCommandStirng(msg.what));
            mClient = msg.replyTo;
            switch (msg.what) {
                case MSG_STORE_BMP_TO_SERVICE:
                    storeBitmap(msg.getData().getByteArray("BitmapFromActivity"));
                    break;
                case MSG_LOAD_BMP_TO_ACTIVITY:
                    sendMsgToActivity(CaptureManagerService.MSG_LOAD_BMP_TO_ACTIVITY, Util.setBitmapWithBundle(loadBitmap()));
                    break;
            }
            return false;
        }
    }));


    /** Activity로 메시지 전달 */
    private void sendMsgToActivity(int cmd, Bundle bundle) {
        if (mClient != null) {
            try {
                Message msg = Message.obtain(null, cmd);
                if (bundle != null) {
                    msg.setData(bundle);
                }
                msg.replyTo = mMessenger;
                mClient.send(msg);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException");
                e.printStackTrace();
            }
        }
    }

    private void storeBitmap(byte[] byteArray) {
        String mPath = Environment.getExternalStorageDirectory().toString()
                + IMG;
        Log.d(TAG, "Store bitmap to " + mPath);

        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        if (bm == null) {
            Log.e(TAG, "bitmap is null");
        }

        OutputStream file_out;
        File imageFile = new File(mPath);

        try {
            file_out = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.PNG, 100, file_out);
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

    private Bitmap loadBitmap() {
        String mPath = Environment.getExternalStorageDirectory().toString()
                + IMG;
        Log.d(TAG, "load bitmap from " + mPath);

        Uri uri = Uri.fromFile(new File(mPath));
        Bitmap bm = BitmapFactory.decodeFile(uri.getPath());
        if (bm == null) {
            Log.e(TAG, "bitmap is null");
            return null;
        }

        return bm;
    }
}
