package im.hua.artofandroid.chapter_81;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import im.hua.artofandroid.R;

public class FloatingWindowActivity extends AppCompatActivity {

    private WindowManager.LayoutParams mLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_window);

//        reqp
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},12345);
        addViewToWindow();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 12345 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            addViewToWindow();
        }
    }

    private void addViewToWindow() {
        final ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        WindowManager windowManager = getWindowManager();
        if (null == mLayoutParams) {
            mLayoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,0,0, PixelFormat.TRANSPARENT);
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            mLayoutParams.x = 100;
            mLayoutParams.y = 100;
            mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        }
        windowManager.addView(imageView, mLayoutParams);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_MOVE:
                        mLayoutParams.x = (int) event.getRawX();
                        mLayoutParams.y = (int) event.getRawY();
                        getWindowManager().updateViewLayout(v,mLayoutParams);
                        break;
                }
                return false;
            }
        });
    }
}
