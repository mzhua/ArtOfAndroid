package im.hua.artofandroid.scroller;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hua on 2017/2/10.
 */

public class FullscreenScrollView extends View {
    private ScrollerCompat mScrollerCompat;

    public FullscreenScrollView(Context context) {
        this(context,null);
    }

    public FullscreenScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FullscreenScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScrollerCompat = ScrollerCompat.create(context);
    }

    private int mLastX;
    private int mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) x;
                mLastY = (int) y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) x - mLastX;
                int dy = (int) (y - mLastY);

                ViewCompat.setTranslationX(this, dx);
                ViewCompat.setTranslationY(this, dy);

                /*Log.d("FullscreenScrollView", "mLastX:" + mLastX);
                Log.d("FullscreenScrollView", "mLastY:" + mLastY);
                Log.d("FullscreenScrollView", "dx:" + dx);
                Log.d("FullscreenScrollView", "dy:" + dy);
                mScrollerCompat.startScroll(mLastX, mLastY, dx, dy);*/
                mLastX = (int) x;
                mLastY = (int) y;
//                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScrollerCompat.computeScrollOffset()) {
            ViewCompat.setTranslationX(this, mScrollerCompat.getCurrX());
            ViewCompat.setTranslationY(this, mScrollerCompat.getCurrY());
            postInvalidate();
        } else {
            super.computeScroll();
        }
    }
}
