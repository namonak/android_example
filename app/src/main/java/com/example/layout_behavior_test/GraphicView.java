package com.example.layout_behavior_test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GraphicView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;

    public static final int FIGURE_TYPE_CIRCLE = 0;
    public static final int FIGURE_TYPE_RECTANGLE = 1;
    public static final int FIGURE_TYPE_ARC = 2;
    public static final int FIGURE_TYPE_RESOURCE_PNG = 3;

    public static final int POSITION_TOP = 50;
    public static final int POSITION_BOTTOM = 830;
    public static final int POSITION_LEFT = 50;
    public static final int POSITION_RIGHT = 1670;

    public GraphicView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    protected void clearSuraface() {
        Canvas canvas = new Canvas();
        try {
            canvas = surfaceHolder.lockCanvas(null);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (surfaceHolder != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    protected void draw(Drawable drawable, ShapeDrawable shapeDrawable) {
        clearSuraface();
        Canvas canvas = new Canvas();
        try {
            canvas = surfaceHolder.lockCanvas(null);
            if (drawable != null) {
                drawable.draw(canvas);
            } else if (shapeDrawable != null)  {
                shapeDrawable.draw(canvas);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (surfaceHolder != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void drawFigure(int type) {
        Drawable drawable = null;
        ShapeDrawable shapeDrawable = null;

        switch (type) {
            case FIGURE_TYPE_CIRCLE:
                shapeDrawable = new ShapeDrawable(new OvalShape());
                shapeDrawable.getPaint().setColor(Color.GREEN);
                shapeDrawable.setBounds(POSITION_LEFT, POSITION_TOP, POSITION_LEFT + 200, POSITION_TOP + 200);
                break;
            case FIGURE_TYPE_RECTANGLE:
                shapeDrawable = new ShapeDrawable(new RectShape());
                shapeDrawable.getPaint().setColor(Color.RED);
                shapeDrawable.setBounds(POSITION_LEFT, POSITION_BOTTOM, POSITION_LEFT + 200, POSITION_BOTTOM + 200);
                break;
            case FIGURE_TYPE_ARC:
                shapeDrawable = new ShapeDrawable(new ArcShape(70, 100));
                shapeDrawable.getPaint().setColor(Color.BLUE);
                shapeDrawable.setBounds(POSITION_RIGHT, POSITION_BOTTOM, POSITION_RIGHT + 200, POSITION_BOTTOM + 200);
                break;
            case FIGURE_TYPE_RESOURCE_PNG:
                drawable = getResources().getDrawable(R.drawable.penguin);
                drawable.setBounds(POSITION_RIGHT, POSITION_TOP, POSITION_RIGHT + 200, POSITION_TOP + 200);
                break;
            default:
                Log.e("GraphicView", "invalid figure type");
        }
        draw(drawable, shapeDrawable);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
