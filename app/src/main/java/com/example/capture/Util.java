package com.example.capture;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class Util {
    static public Bundle setBitmapWithBundle(Bitmap bm) {
        if(bm == null) {
            Log.e(MainActivity.TAG, "bitmap is null");
        }
        /** Bitmap 이미지를 byte[]로 변환 **/
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        /** Bundle 객체에 byte[]로 타입으로 변환된 Bitmap 이미지를 저장 **/
        Bundle bundle = new Bundle();
        bundle.putByteArray("BitmapFromActivity", byteArray);

        return bundle;
    }
}
