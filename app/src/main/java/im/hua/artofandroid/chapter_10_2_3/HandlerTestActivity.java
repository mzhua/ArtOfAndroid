package im.hua.artofandroid.chapter_10_2_3;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import im.hua.artofandroid.R;

public class HandlerTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_test);
        new Thread("Thread#12"){
            @Override
            public void run() {
                super.run();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HandlerTestActivity.this, "test", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }
}
