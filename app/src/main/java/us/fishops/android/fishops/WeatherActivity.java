package us.fishops.android.fishops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
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
import android.zetterstrom.com.forecast.models.Forecast;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;
import android.zetterstrom.com.forecast.models.Alert;
import android.zetterstrom.com.forecast.models.Forecast;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Callback;
import retrofit2.Response;

import us.fishops.android.fishops.MapsActivity;
import us.fishops.android.fishops.MainActivity;

public class WeatherActivity extends AppCompatActivity {
    MainActivity a1 = new MainActivity();
    MapsActivity a2 = new MapsActivity();
    Forecast forecast;
    final Handler handler = new Handler();
    TextView tv;
    Timer    timer = new Timer();
    MapsActivity mappy = new MapsActivity();
    private String summary;
    private Double temperature, distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getWeather(mappy.getMarkerPosition());
        tv = (TextView) findViewById(R.id.box);
        tv.setText(forecast.getCurrently().getSummary());


    }

    public void getWeather(LatLng markerPos){
        ForecastConfiguration configuration = new ForecastConfiguration.Builder("4c53088bf37f39b40f21165b81d5b69f")
                .setCacheDirectory(getCacheDir()).build();
        ForecastClient.create(configuration);
        ForecastClient.getInstance().getForecast(markerPos.latitude, markerPos.longitude, new Callback<Forecast>(){
            public void onResponse(Response<Forecast> response){
                String title, description, uri;
                Date expires;
                if (response.isSuccess()){
                    forecast = response.body();
                    summary = forecast.getCurrently().getSummary();
                    temperature = forecast.getCurrently().getApparentTemperature();
                    distance = forecast.getCurrently().getNearestStormDistance();
                }
            }
            @Override
            public void onFailure(Throwable t){
                t.printStackTrace();
            }
        });
    }
}
