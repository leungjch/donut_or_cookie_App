package com.example.justin.donutorcookie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
public class DrawView extends View{
    public Bitmap mBitmap;
    public Canvas mCanvas;
    private Paint paint = new Paint();
    private Path path = new Path();
    public static int BRUSH_SIZE = 40;
    public static final int DEFAULT_COLOR = Color.WHITE;
    public static final int DEFAULT_BG_COLOR = Color.BLACK;
    float mX, mY;
    private ArrayList<Point> circlePoints;

    public DrawView(Context context) {
        this(context, null);
        setupPaint();
        circlePoints = new ArrayList<Point>();

    }

    public DrawView(Context context, AttributeSet attrs){
        super(context,attrs);
        setupPaint();
        circlePoints = new ArrayList<Point>();


    }
    public void setupPaint(){
        paint = new Paint();
        paint.setColor(DEFAULT_COLOR);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(BRUSH_SIZE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setDither(true);
        paint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.NORMAL));

    }
    public Bitmap getBitmap(){
        return mBitmap;
    }
    @Override
    protected void onDraw(Canvas canvas){

        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }
        mCanvas.drawColor(Color.BLACK);
        mCanvas.drawPath(path,paint);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        Log.i("i", Integer.toString(mCanvas.getWidth()) + ", "  + Integer.toString(mCanvas.getHeight()));
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        mX = event.getX();
        mY = event.getY();
//        circlePoints.add(new Point(Math.round(mX), Math.round(mY)));
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // start new path
                path.moveTo(mX, mY);
                break;
            case MotionEvent.ACTION_MOVE:
                // draw line
                path.lineTo(mX,mY);
                break;
            default:
                return false;
        }
        // indicate view should be redrawn
        postInvalidate();
        return true;
    }


//    public void init(DisplayMetrics metrics){
//        int height = metrics.heightPixels;
//        int width = metrics.widthPixels;
//    }


}
