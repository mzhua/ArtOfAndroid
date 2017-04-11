package im.hua.artofandroid.bezier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import im.hua.artofandroid.R;

public class CubicBezierActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private CubicBezierView cbView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cubic_bezier);
        cbView = (CubicBezierView) findViewById(R.id.cbView);
        RadioButton rb1 = (RadioButton) findViewById(R.id.rb1);
        RadioButton rb2 = (RadioButton) findViewById(R.id.rb2);
        rb1.setOnCheckedChangeListener(this);
        rb2.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb1:
                if (isChecked) {
                    cbView.setFirstControl(true);
                }
                break;
            case R.id.rb2:
                if (isChecked) {
                    cbView.setFirstControl(false);
                }
                break;
        }
    }
}
