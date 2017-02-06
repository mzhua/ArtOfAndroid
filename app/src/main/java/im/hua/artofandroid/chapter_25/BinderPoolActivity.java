package im.hua.artofandroid.chapter_25;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import im.hua.artofandroid.R;

public class BinderPoolActivity extends AppCompatActivity {

    private static final String TAG = "BinderPoolActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        ISecurityCenter iSecurityCenter = SecurityCenterImpl.asInterface(securityBinder);
        String msg = "Hello Android";
        Log.d(TAG,"visit ISecurityCenter");
        Log.d(TAG,"content: " + msg);
        try {
            String password = iSecurityCenter.encrypt(msg);
            Log.d(TAG,"encrypt: " + password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.d(TAG,"visit ICompute");
        IBinder binderCompute = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        ICompute iCompute = ComputeImpl.asInterface(binderCompute);
        try {
            Log.d(TAG,"3 + 5 = " + iCompute.add(3, 5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
