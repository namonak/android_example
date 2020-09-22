package com.example.layout_behavior_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GraphicView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    Bitmap bitmap;
    Paint paint;

    public static final int Circle = 0;
    public static final int Rectangle = 1;

    public GraphicView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    protected void draw() {
        try {
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawBitmap(bitmap, 0,0, paint);
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (surfaceHolder != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void drawFigure(int type) {
        bitmap = Bitmap.createBitmap( getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);

        switch (type) {
            case Circle:
                canvas.drawCircle(240, 100, 70, paint);
                canvas.drawText("Circle", 200, 190, paint);
                break;
            case Rectangle:
                canvas.drawRect(190,200,290,300, paint);
                canvas.drawText("Rect", 200, 320, paint);
                break;
            default:
                Log.e("GraphicView", "invalid figure type");
        }
        draw();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        canvas = new Canvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setTextSize(22);
        paint.setAntiAlias(true);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
