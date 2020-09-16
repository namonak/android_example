package com.example.capture;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

public class CaptureManagerService extends Service {
    public static final int MSG_STORE_BMP_TO_SERVICE = 0;
    public static final int MSG_LOAD_BMP_TO_ACTIVITY = 1;

    private Messenger mClient = null;   // Activity 에서 가져온 Messenger

    MemoryFile mf;
    int captureImageSize = 0;

    public static final String TAG = "CaptureManagerService";

    @Override
    public void onCreate() {
        super.onCreate();
        createMEM();
    }

    @Override
    public void onDestroy() {
        freeMEM();
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
                    try {
                        storeBitmap(msg.getData().getByteArray("BitmapFromActivity"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_LOAD_BMP_TO_ACTIVITY:
                    sendMsgToActivity(CaptureManagerService.MSG_LOAD_BMP_TO_ACTIVITY, null);
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
                switch (cmd) {
                    case MSG_STORE_BMP_TO_SERVICE:
                        Log.d(TAG, "do nothing");
                        break;
                    case MSG_LOAD_BMP_TO_ACTIVITY:
                        msg.obj = mf;
                        msg.arg1 = captureImageSize;
                        break;
                }
                msg.replyTo = mMessenger;
                mClient.send(msg);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException");
                e.printStackTrace();
            }
        }
    }

    private void storeBitmap(byte[] byteArray) throws IOException {
        Log.d(TAG, "Stored Bitmap " + byteArray.length);
        captureImageSize = byteArray.length;
        mf.writeBytes(byteArray, 0, 0, byteArray.length);
    }

    private void createMEM() {
        try {
            mf = new MemoryFile("mf", 1024 * 1024 * 5);
            Log.d("AndroidIPC::SHM", "Allocated MemoryFile " + mf.length());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void freeMEM() {
        if (mf.length() != 0) {
            mf.close();
        }
    }
}
