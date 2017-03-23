package im.hua.artofandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import im.hua.artofandroid.ipc.SecondActivity;

import static android.content.pm.PackageManager.GET_ACTIVITIES;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ActivityRVAdapter mActivityRVAdapter;

    private ActivityInfo[] mActivityInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mActivityRVAdapter = new ActivityRVAdapter();

        try {
            mActivityInfos = getPackageManager().getPackageInfo(getPackageName(), GET_ACTIVITIES).activities;
            mRecyclerView.setAdapter(mActivityRVAdapter);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    class ActivityRVAdapter extends RecyclerView.Adapter<ActivityRVAdapter.ItemViewHolder> {

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            final ActivityInfo activityInfo = mActivityInfos[position];
            int labelRes = activityInfo.labelRes;
            if (labelRes > 0) {
                holder.mTextView.setText(getResources().getString(labelRes));
            } else {
                holder.mTextView.setText(activityInfo.name);
            }
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClassName(getPackageName(), activityInfo.name);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mActivityInfos == null ? 0 : mActivityInfos.length;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView mTextView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(android.R.id.text1);
                mTextView.setClickable(true);
                int[] attrs = new int[]{R.attr.selectableItemBackground};
                TypedArray array = itemView.getContext().obtainStyledAttributes(attrs);
                int backgroundResId = array.getResourceId(0, 0);
                mTextView.setBackgroundDrawable(getResources().getDrawable(backgroundResId));
                array.recycle();
            }
        }
    }
}
