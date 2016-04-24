package us.fishops.android.fishops;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.widget.TextView;

public class ResourcesActivity extends AppCompatActivity {

    Typeface cardo;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        cardo = Typeface.createFromAsset(getApplicationContext().getAssets(), "Cardo-Regular.ttf");
        tv = (TextView) findViewById(R.id.resourcesView);
        tv.setTypeface(cardo);
        tv.setTextSize(24);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tv.setMovementMethod(new ScrollingMovementMethod());
    }
}
