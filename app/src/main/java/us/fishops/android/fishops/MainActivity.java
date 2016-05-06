package us.fishops.android.fishops;

import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //private final String TAG = "MainActivity";
    private boolean mapsStarted = false;
    private boolean weatherStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);

        //database stuff
        //Log.d("Test", "Testy");
        //MySQLiteHelper database = new MySQLiteHelper(this);
        //SQLiteDatabase db = database.getWritableDatabase();

        //button stuff
        Button mpaBtn = (Button) findViewById(R.id.mpa_btn);
        Button weatherBtn = (Button) findViewById(R.id.weather_btn);
        Button lawBtn = (Button) findViewById(R.id.laws_btn);
        Button resourcesBtn = (Button) findViewById(R.id.resources_btn);

        //Typeface ebGaramond = Typeface.createFromAsset(getApplicationContext().getAssets(), "EBGaramondSC08-Regular.ttf");
        Typeface oswald = Typeface.createFromAsset(getApplicationContext().getAssets(), "Oswald-Bold.ttf");

        if (mpaBtn != null) {
            mpaBtn.setTypeface(oswald);
        }
        if (weatherBtn != null) {
            weatherBtn.setTypeface(oswald);
        }
        if (lawBtn != null) {
            lawBtn.setTypeface(oswald);
        }
        if (resourcesBtn != null) {
            resourcesBtn.setTypeface(oswald);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        this.mapsStarted = true;
        intent.putExtra("weatherStarted", this.weatherStarted);
        startActivity(intent);
    }

    public void loadLaws(View view) {
        Intent intent = new Intent(this, LawsDecreesActivity.class);
        startActivity(intent);
    }

    public void loadResources(View view) {
        Intent intent = new Intent(this, ResourcesActivity.class);
        startActivity(intent);
    }

    public void goToWeather(View view){
        Intent intent = new Intent(this, WeatherActivity.class);
        this.weatherStarted = true;
        intent.putExtra("mapsStarted", this.mapsStarted);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}