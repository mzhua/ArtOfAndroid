package im.hua.artofandroid.behavior;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import im.hua.artofandroid.R;

public class WeicoUserDetailActivity extends AppCompatActivity {

    private ImageView mAvatar;
    private Animator mHideAnimator;
    private Animator mShowAnimator;

    private boolean mAvatarShowing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weico_user_detail);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mAvatar = (ImageView) findViewById(R.id.iv_avatar);

        mHideAnimator = AnimatorInflater.loadAnimator(WeicoUserDetailActivity.this, R.animator.scale_to_hide);
        mHideAnimator.setInterpolator(new AccelerateInterpolator());
        mHideAnimator.setTarget(mAvatar);
        mShowAnimator = AnimatorInflater.loadAnimator(WeicoUserDetailActivity.this, R.animator.scale_to_show);
        mShowAnimator.setInterpolator(new DecelerateInterpolator());
        mShowAnimator.setTarget(mAvatar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("WeicoUserDetailActivity", "verticalOffset:" + verticalOffset);
                float percent = (float) (Math.abs(verticalOffset) * 1.0 / appBarLayout.getTotalScrollRange());
                if (percent >= 0.5) {
                    if (!mHideAnimator.isStarted() && mAvatarShowing) {
                        mAvatarShowing = false;
                        mShowAnimator.cancel();
                        mHideAnimator.start();
                    }
                } else {
                    if (!mShowAnimator.isStarted() && !mAvatarShowing) {
                        mAvatarShowing = true;
                        mHideAnimator.cancel();
                        mShowAnimator.start();
                    }
                }
            }
        });
    }
}
