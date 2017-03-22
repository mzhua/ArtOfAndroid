package im.hua.artofandroid.behavior;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import im.hua.artofandroid.R;
import im.hua.artofandroid.glide.CropCircleTransformation;

public class WeicoUserDetailActivity extends AppCompatActivity {

    private ImageView mAvatar;
    private ImageView mAppbarIamgeView;
    private Animator mHideAnimator;
    private Animator mShowAnimator;

    private boolean mAvatarShowing = true;
    private ImageViewBlurRender mImageViewBlurRender;

    private boolean mCanBlur;
    private LinearLayout mLinearLayout;
    private LayoutAnimationController mFlowUpController;
    private LayoutAnimationController mFlowDownController;
    private boolean mIsTitleShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weico_user_detail);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mAvatar = (ImageView) findViewById(R.id.iv_avatar);
        mAppbarIamgeView = (ImageView) findViewById(R.id.imageView);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_title);

        mHideAnimator = AnimatorInflater.loadAnimator(WeicoUserDetailActivity.this, R.animator.scale_to_hide);
        mHideAnimator.setInterpolator(new AccelerateInterpolator());
        mHideAnimator.setTarget(mAvatar);
        mShowAnimator = AnimatorInflater.loadAnimator(WeicoUserDetailActivity.this, R.animator.scale_to_show);
        mShowAnimator.setInterpolator(new DecelerateInterpolator());
        mShowAnimator.setTarget(mAvatar);

        mFlowUpController = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.flow_up));
        mFlowDownController = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.flow_down));

        Glide.with(this)
                .load("http://tva2.sinaimg.cn/crop.0.0.1342.1342.180/636796dfjw8f9cb2ipwprj211a11a420.jpg")
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(this))
                .into(mAvatar);

        Glide.with(this)
                .load("http://wx3.sinaimg.cn/mw690/6c2fc79ely1fdvbnh6micj20go09d3yt.jpg")
                .crossFade()
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mCanBlur = true;
                        return false;
                    }
                })
                .into(mAppbarIamgeView);

        mImageViewBlurRender = new ImageViewBlurRender();
        mImageViewBlurRender.setImageView(mAppbarIamgeView);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("WeicoUserDetailActivity", "verticalOffset:" + verticalOffset);
                float percent = (float) (Math.abs(verticalOffset) * 1.0 / appBarLayout.getTotalScrollRange());
                if (mCanBlur) {
                    mImageViewBlurRender.blur(percent * 25);
                }
                if (mIsTitleShowing && percent < 1.0) {
                    mLinearLayout.setLayoutAnimation(mFlowDownController);
                    mLinearLayout.startLayoutAnimation();
                    mIsTitleShowing = false;
                }
                if (percent >= 0.5) {
                    if (!mHideAnimator.isStarted() && mAvatarShowing) {
                        mAvatarShowing = false;
                        mShowAnimator.cancel();
                        mHideAnimator.start();
                    }
                    if (percent >= 1.0) {
                        if (!mIsTitleShowing) {
                            mLinearLayout.setLayoutAnimation(mFlowUpController);
                            mLinearLayout.setVisibility(View.VISIBLE);
                            mLinearLayout.startLayoutAnimation();
                            mIsTitleShowing = true;
                        }
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
