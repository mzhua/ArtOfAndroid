package im.hua.artofandroid.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import im.hua.artofandroid.R;

/**
 * Created by hua on 2017/4/11.
 */

public class QuadBezierView extends View{
    private Paint mPaint;
    private int mCenterX, mCenterY;

    private PointF mStart,mEnd, mControl;
    private Path mPath;

    public QuadBezierView(Context context) {
        this(context,null);
    }

    public QuadBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(60);

        mStart = new PointF(0, 0);
        mEnd = new PointF(0, 0);
        mControl = new PointF(0, 0);

        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;

        mStart.x = mCenterX - 200;
        mStart.y = mCenterY;
        mEnd.x = mCenterX + 200;
        mEnd.y = mCenterY;

        mControl.x = mCenterX;
        mControl.y = mCenterY - 100;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mControl.x = event.getX();
        mControl.y = event.getY();
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(mStart.x,mStart.y,mPaint);
        canvas.drawPoint(mEnd.x,mEnd.y,mPaint);
        canvas.drawPoint(mControl.x,mControl.y,mPaint);

        //画辅助线
        mPaint.setStrokeWidth(4);
        canvas.drawLine(mStart.x,mStart.y,mControl.x,mControl.y,mPaint);
        canvas.drawLine(mControl.x,mControl.y,mEnd.x,mEnd.y,mPaint);

        //话Bezier曲线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);

        mPath.reset();
        mPath.moveTo(mStart.x,mStart.y);
        mPath.quadTo(mControl.x,mControl.y,mEnd.x,mEnd.y);
        canvas.drawPath(mPath,mPaint);
    }
}
