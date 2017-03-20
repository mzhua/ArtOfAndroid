package im.hua.artofandroid.chapter_4;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hua on 2017/2/25.
 */

public class HorizontalScrollView extends ViewGroup {
    public HorizontalScrollView(Context context) {
        super(context);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childCount = getChildCount();

        int measuredWidth = 0;
        int measuredHeight = 0;

        if (childCount > 0) {
            View childView = getChildAt(0);

            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
                measuredWidth = childView.getMeasuredWidth() * childCount;
                measuredHeight = childView.getMeasuredHeight();
            } else if (widthMode == MeasureSpec.AT_MOST) {
                measuredWidth = childView.getMeasuredWidth() * childCount;
                measuredHeight = heightSize;
            } else if (heightMode == MeasureSpec.AT_MOST) {
                measuredWidth = widthSize;
                measuredHeight = childView.getMeasuredHeight();
            } else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
                measuredWidth = widthSize;

            }
            Log.d("HorizontalScrollView", "measuredWidth:measuredHeight:" + measuredWidth + ":" + measuredHeight);
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int childLeft = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                childView.layout(childLeft, 0, childLeft + childView.getMeasuredWidth(), childView.getMeasuredHeight());
                childLeft += childView.getMeasuredWidth();
            }
        }
    }
}
