package im.hua.artofandroid.behavior;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by hua on 2017/3/21.
 */

public class HeaderImageBehavior extends CoordinatorLayout.Behavior<View> {

    private Rect mOutRect;

    public HeaderImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mOutRect = new Rect();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        AppBarLayout appBarLayout = (AppBarLayout) dependency;
        Log.d("HeaderImageBehavior", "appBarLayout.getTotalScrollRange():" + appBarLayout.getTotalScrollRange());
        Log.d("HeaderImageBehavior", "appBarLayout.getHeight():" + appBarLayout.getHeight());
        Log.d("HeaderImageBehavior", "mOutRect.height():" + mOutRect.height());
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
