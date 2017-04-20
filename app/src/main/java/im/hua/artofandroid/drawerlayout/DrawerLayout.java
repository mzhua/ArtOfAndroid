package im.hua.artofandroid.drawerlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import im.hua.artofandroid.R;

import static android.support.v4.widget.ViewDragHelper.EDGE_LEFT;

/**
 * TODO: document your custom view class.
 */
public class DrawerLayout extends ViewGroup {
    private ViewDragHelper mViewDragHelper;

    private View mDrawerView;
    private View mContentView;

    private final int mDrawerDefaultOffset = 300;

    private Paint mScrimPaint = new Paint();

    public DrawerLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public DrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mScrimPaint.setStyle(Paint.Style.FILL);
        mScrimPaint.setColor(Color.GRAY);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DrawerLayout, defStyle, 0);

        a.recycle();

        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mContentView || child == mDrawerView;
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                MarginLayoutParams layoutParams = (MarginLayoutParams) releasedChild.getLayoutParams();

                int left = layoutParams.leftMargin;
                int top = layoutParams.topMargin;

                if (xvel < 0 || (getDrawerViewOffsetPercent() < 0.5f && xvel == 0)) {
                    //close
                    if (isContentView(releasedChild)) {
                        left = layoutParams.leftMargin;
                    } else if (isDrawerView(releasedChild)) {
                        left = -mDrawerView.getWidth() + layoutParams.leftMargin;
                    }
                } else {
                    //open
                    if (isContentView(releasedChild)) {
                        left = layoutParams.leftMargin + mDrawerView.getWidth();
                    } else if (isDrawerView(releasedChild)) {
                        left = layoutParams.leftMargin;
                    }
                }
                mViewDragHelper.settleCapturedViewAt(left, top);
                invalidate();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (isContentView(changedView)) {
                    float contentViewOffsetPercent = 1.0f * left / mContentView.getWidth();
                    mDrawerView.setLeft(-(int) ((1 - contentViewOffsetPercent) * mDrawerView.getWidth()));

//                    ViewCompat.offsetLeftAndRight(mDrawerView, dx);
                } else if (isDrawerView(changedView)) {
//                    ViewCompat.offsetLeftAndRight(mContentView, dx);
                    float contentViewOffsetPercent = 1.0f * Math.abs(left) / mDrawerView.getWidth();
                    mContentView.setLeft((int) (contentViewOffsetPercent * mContentView.getWidth()));
                }
                invalidate();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mDrawerView.getWidth();
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int clampLeft = left;
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (isContentView(child)) {
                    clampLeft = Math.min(Math.max(lp.leftMargin, left), mDrawerView.getWidth());
                } else if (isDrawerView(child)) {
                    clampLeft = Math.max(Math.min(lp.leftMargin, left), -mDrawerView.getWidth());
                }
                return clampLeft;
            }
        });
        mViewDragHelper.setEdgeTrackingEnabled(EDGE_LEFT);
    }

    private float getDrawerViewOffsetPercent() {
        int availableOffsetRange = mDrawerView.getWidth() - mDrawerDefaultOffset;
        int currentOffset = availableOffsetRange - Math.abs(mDrawerView.getLeft());
        return 1.0f * (currentOffset) / availableOffsetRange;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureView();
        mDrawerView = getChildAt(0);
        mContentView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            if (isInEditMode()) {
                if (widthMode == MeasureSpec.AT_MOST) {
                    widthMode = MeasureSpec.EXACTLY;
                } else if (widthMode == MeasureSpec.UNSPECIFIED) {
                    widthMode = MeasureSpec.EXACTLY;
                    widthSize = 300;
                }
                if (heightMode == MeasureSpec.AT_MOST) {
                    heightMode = MeasureSpec.EXACTLY;
                } else if (heightMode == MeasureSpec.UNSPECIFIED) {
                    heightMode = MeasureSpec.EXACTLY;
                    heightSize = 300;
                }
            } else {
                throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            }
        }
        setMeasuredDimension(widthSize, heightSize);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            if (isDrawerView(childView)) {
                int childWidthSpec = MeasureSpec.makeMeasureSpec(layoutParams.width - layoutParams.leftMargin - layoutParams.rightMargin, MeasureSpec.EXACTLY);
                int childHeightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height - layoutParams.topMargin - layoutParams.bottomMargin, MeasureSpec.EXACTLY);
                childView.measure(childWidthSpec, childHeightSpec);

            } else if (isContentView(childView)) {
                int childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - layoutParams.leftMargin - layoutParams.rightMargin, MeasureSpec.EXACTLY);
                int childHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - layoutParams.topMargin - layoutParams.bottomMargin, MeasureSpec.EXACTLY);
                childView.measure(childWidthSpec, childHeightSpec);
            }
        }
    }

    private void ensureView() {
        int childCount = getChildCount();
        if (2 != childCount) {
            throw new IllegalArgumentException("DrawerLayout cat only hold two child view.");
        }
    }

    private boolean isDrawerView(View view) {
        ensureView();
        return view == getChildAt(0);
    }

    private boolean isContentView(View view) {
        ensureView();
        return view == getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            final int childWidth = childView.getMeasuredWidth();
            final int childHeight = childView.getMeasuredHeight();
            int childLeft;
            if (isContentView(childView)) {
                childLeft = lp.leftMargin;
            } else {
                childLeft = -childWidth + mDrawerDefaultOffset;
            }
            childView.layout(childLeft, lp.topMargin, childLeft + childWidth, lp.topMargin + childHeight);
        }
    }


    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean drawChild = super.drawChild(canvas, child, drawingTime);
        if (isContentView(child)) {
            mScrimPaint.setAlpha((int) (200 * getDrawerViewOffsetPercent()));
            canvas.drawRect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom(), mScrimPaint);
        }

        return drawChild;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
