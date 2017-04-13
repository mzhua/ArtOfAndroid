package im.hua.artofandroid.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by hua on 2017/4/13.
 */

public class MagicBallBezierView extends View {
    public static final int CONTROL_OFFSET = 20;
    private float mCircleRadius;//dpi

    private Paint mPaint;
    private ControlPoint mLeftPoint;
    private ControlPoint mRightPoint;
    private ControlPoint mTopPoint;
    private ControlPoint mBottomPoint;

    private Path mPath;

    public MagicBallBezierView(Context context) {
        this(context, null);
    }

    public MagicBallBezierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicBallBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        mCircleRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mPath = new Path();
        mLeftPoint = new ControlPoint();
        mTopPoint = new ControlPoint();
        mRightPoint = new ControlPoint();
        mBottomPoint = new ControlPoint();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mLeftPoint.setData(-mCircleRadius, 0);
        mLeftPoint.setFirstControl(mLeftPoint.data.x, mLeftPoint.data.y + CONTROL_OFFSET);
        mLeftPoint.setSecondControl(mLeftPoint.data.x, mLeftPoint.data.y - CONTROL_OFFSET);

        mTopPoint.setData(mLeftPoint.data.x + mCircleRadius, mLeftPoint.data.y - mCircleRadius);
        mTopPoint.setFirstControl(mTopPoint.data.x - CONTROL_OFFSET, mTopPoint.data.y);
        mTopPoint.setSecondControl(mTopPoint.data.x + CONTROL_OFFSET, mTopPoint.data.y);

        mRightPoint.setData(mLeftPoint.data.x + 2 * mCircleRadius + 30, mLeftPoint.data.y);
        mRightPoint.setFirstControl(mRightPoint.data.x, mRightPoint.data.y - CONTROL_OFFSET);
        mRightPoint.setSecondControl(mRightPoint.data.x, mRightPoint.data.y + CONTROL_OFFSET);

        mBottomPoint.setData(mTopPoint.data.x, mLeftPoint.data.y + mCircleRadius);
        mBottomPoint.setFirstControl(mBottomPoint.data.x + CONTROL_OFFSET, mBottomPoint.data.y);
        mBottomPoint.setSecondControl(mBottomPoint.data.x - CONTROL_OFFSET, mBottomPoint.data.y);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.translate(width / 2, height / 2);

        canvas.drawLine(0, 0, width, 0, mPaint);
        canvas.drawLine(0, 0, 0, height, mPaint);

        mPath.reset();
        mPath.moveTo(mLeftPoint.data.x, mLeftPoint.data.y);
        mPath.cubicTo(mLeftPoint.secondControl.x, mLeftPoint.secondControl.y
                , mTopPoint.firstControl.x, mTopPoint.firstControl.y
                , mTopPoint.data.x, mTopPoint.data.y);

        mPath.cubicTo(mTopPoint.secondControl.x, mTopPoint.secondControl.y
                , mRightPoint.firstControl.x, mRightPoint.firstControl.y
                , mRightPoint.data.x, mRightPoint.data.y);

        mPath.cubicTo(mRightPoint.secondControl.x, mRightPoint.secondControl.y
                , mBottomPoint.firstControl.x, mBottomPoint.firstControl.y
                , mBottomPoint.data.x, mBottomPoint.data.y);

        mPath.cubicTo(mBottomPoint.secondControl.x, mBottomPoint.secondControl.y
                , mLeftPoint.firstControl.x, mLeftPoint.firstControl.y
                , mLeftPoint.data.x, mLeftPoint.data.y);

        canvas.drawPath(mPath, mPaint);
    }

    /**
     * first和second的顺序按圆的逆时针方向来确定
     */
    private class ControlPoint {
        private PointF data;
        private PointF firstControl;
        private PointF secondControl;

        public ControlPoint() {
            data = new PointF();
            firstControl = new PointF();
            secondControl = new PointF();
        }

        public void setData(float x, float y) {
            data.x = x;
            data.y = y;
        }

        public void setFirstControl(float x, float y) {
            firstControl.x = x;
            firstControl.y = y;
        }

        public void setSecondControl(float x, float y) {
            secondControl.x = x;
            secondControl.y = y;
        }
    }

}
