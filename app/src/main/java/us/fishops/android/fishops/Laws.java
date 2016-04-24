package us.fishops.android.fishops;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class Laws extends AppCompatActivity {

    Typeface ebGaramond;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laws);
        ebGaramond = Typeface.createFromAsset(getApplicationContext().getAssets(), "EBGaramondSC08-Regular.ttf");
        tv = (TextView) findViewById(R.id.lawsView);
        tv.setTypeface(ebGaramond);
        tv.setTextSize(24);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tv.setMovementMethod(new ScrollingMovementMethod());

    }
}
