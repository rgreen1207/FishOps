package us.fishops.android.fishops;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mpaBtn;
    Button weatherBtn;
    Button lawBtn;
    Button decreeBtn;
    Button settingsBtn;
    Typeface ebGaramond;
    Typeface oswald;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);

        mpaBtn = (Button) findViewById(R.id.mpa_btn);
        weatherBtn = (Button) findViewById(R.id.weather_btn);
        lawBtn = (Button) findViewById(R.id.laws_btn);
        decreeBtn = (Button) findViewById(R.id.decree_btn);
        settingsBtn = (Button) findViewById(R.id.settings_button);

        ebGaramond = Typeface.createFromAsset(getApplicationContext().getAssets(), "EBGaramondSC08-Regular.ttf");
        oswald = Typeface.createFromAsset(getApplicationContext().getAssets(), "Oswald-Bold.ttf");

        mpaBtn.setTypeface(oswald);
        weatherBtn.setTypeface(oswald);
        lawBtn.setTypeface(oswald);
        decreeBtn.setTypeface(oswald);
        settingsBtn.setTypeface(oswald);


    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
