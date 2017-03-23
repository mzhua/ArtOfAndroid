package im.hua.artofandroid.behavior;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import im.hua.artofandroid.BuildConfig;

/**
 * Created by hua on 2017/3/22.
 */

public class ImageViewBlurRender {
    private ImageView mImageView;
    private RenderScript mRenderScript;
    private Bitmap mDrawingCache;
    private Bitmap mOutBitmap;
    private Allocation mAllocationIn;
    private Allocation mAllocationOut;
    private ScriptIntrinsicBlur mScriptIntrinsicBlur;
    private Handler mUiHandler;
    private boolean mIsRendering;

    public ImageViewBlurRender() {
        mUiHandler = new Handler(Looper.getMainLooper());
    }

    private void securityCheck() {
        if (mImageView == null) {
            throw new NullPointerException("ImageView is null, please setImageView first");
        }
        if (mRenderScript == null || mScriptIntrinsicBlur == null || mUiHandler == null) {
            throw new NullPointerException("initial failed");
        }
    }

    public void setImageView(@NonNull ImageView imageView) {
        mImageView = imageView;
        mImageView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                destroy();
            }
        });
        mRenderScript = RenderScript.create(mImageView.getContext().getApplicationContext());
        mScriptIntrinsicBlur = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
    }

    public void blur(@FloatRange(from = 0.0f, to = 25.0f) float radius) {
        securityCheck();

        if (mIsRendering && radius > 0.0f) {
            return;
        }
        mIsRendering = true;
        if (radius <= 0.0f) {
            radius = 0.0001f;
        }
        mImageView.setDrawingCacheEnabled(true);
        final float finalRadius = radius;
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                invalidate(finalRadius);
            }
        });
    }

    private void invalidate(@FloatRange(from = 0.0f, to = 25.0f) float radius) {
        if (null == mDrawingCache) {
            mDrawingCache = mImageView.getDrawingCache();
            mAllocationIn = Allocation.createFromBitmap(mRenderScript, mDrawingCache);
            mScriptIntrinsicBlur.setInput(mAllocationIn);
        }
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        if (null == mOutBitmap) {
            mOutBitmap = Bitmap.createBitmap(mDrawingCache.getWidth(), mDrawingCache.getHeight(), Bitmap.Config.ARGB_8888);
            mAllocationOut = Allocation.createFromBitmap(mRenderScript, mOutBitmap);
        } /*else {
            mOutBitmap.eraseColor(mImageView.getResources().getColor(android.R.color.transparent));
        }*/
        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps

        //Set the radius of the blur: 0 < radius <= 25
        mScriptIntrinsicBlur.setRadius(radius);

        new Thread("Thread:[blur]") {
            @Override
            public void run() {
                super.run();
                //Perform the Renderscript
                mScriptIntrinsicBlur.forEach(mAllocationOut);
                mAllocationOut.copyTo(mOutBitmap);
                new Handler(Looper.getMainLooper())
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                setImageBitmap(mOutBitmap);
                            }
                        });
            }
        }.start();
    }

    private void setImageBitmap(final Bitmap bitmap) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageBitmap(bitmap);
                mIsRendering = false;
            }
        });
    }

    private void destroy() {
        if (null != mDrawingCache) {
            mDrawingCache.recycle();
            mDrawingCache = null;
        }
        if (null != mRenderScript) {
            mRenderScript.destroy();
            mRenderScript = null;
        }
        if (null != mAllocationIn) {
            mAllocationIn.destroy();
            mAllocationIn = null;
        }
        if (null != mAllocationOut) {
            mAllocationOut.destroy();
            mAllocationOut = null;
        }
        if (null != mScriptIntrinsicBlur) {
            mScriptIntrinsicBlur.destroy();
            mScriptIntrinsicBlur = null;
        }
        if (null != mImageView) {
            mImageView.destroyDrawingCache();
            mImageView = null;
        }
        if (null != mOutBitmap) {
            mOutBitmap.recycle();
            mOutBitmap = null;
        }
        mUiHandler = null;
        if (BuildConfig.DEBUG) Log.d("ImageViewBlurRender", "blur destroy");
    }
}
