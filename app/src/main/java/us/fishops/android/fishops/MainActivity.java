package us.fishops.android.fishops;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;
import android.zetterstrom.com.forecast.models.Forecast;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button mpaBtn;
    Button weatherBtn;
    Button lawBtn;
    Button decreeBtn;
    Typeface ebGaramond;
    Typeface oswald;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);

        //database stuff
        Log.d("Test", "Testy");
        MySQLiteHelper database = new MySQLiteHelper(this);
        SQLiteDatabase db = database.getWritableDatabase();

        //button stuff
        mpaBtn = (Button) findViewById(R.id.mpa_btn);
        weatherBtn = (Button) findViewById(R.id.weather_btn);
        lawBtn = (Button) findViewById(R.id.laws_btn);
        decreeBtn = (Button) findViewById(R.id.decree_btn);

        ebGaramond = Typeface.createFromAsset(getApplicationContext().getAssets(), "EBGaramondSC08-Regular.ttf");
        oswald = Typeface.createFromAsset(getApplicationContext().getAssets(), "Oswald-Bold.ttf");

        mpaBtn.setTypeface(oswald);
        weatherBtn.setTypeface(oswald);
        lawBtn.setTypeface(oswald);
        decreeBtn.setTypeface(oswald);
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void loadLaws(View view) {
        Intent intent = new Intent(this, LawsDecreesActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_setting) {
            Log.d("DEBUG", "Setting button selected");
        }
        return true;
    }
}
    public void getWeather(final double latitude, final double longitude){
        //getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, new SnowFragment()).commit();
        ForecastConfiguration configuration = new ForecastConfiguration.Builder("4c53088bf37f39b40f21165b81d5b69f")
            .setCacheDirectory(getCacheDir()).build();
        ForecastClient.create(configuration);
        ForecastClient.getInstance().getForecast(latitude, longitude, new Callback<Forecast>(){
            @Override
            public void onResponse(Response<Forecast> response){
                if (response.isSuccess()){
                    Forecast forecast = response.body();
                }
            }
            @Override
            public void onFailure(Throwable t){
                t.printStackTrace();
            }
        });
    }
}
