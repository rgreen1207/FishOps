package us.fishops.android.fishops;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;
import android.zetterstrom.com.forecast.models.Alert;
import android.zetterstrom.com.forecast.models.Forecast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {
    TextView tv;
    final Handler handler = new Handler();
    Timer timer = new Timer();
    MapsActivity mappy;
    private static final int REQUEST_CODE_LOCATION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mappy = new MapsActivity();
        getWeather(getPosition());
    }

    public LatLng getPosition() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true), currentCity;

        Location location = null;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request missing location permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION);
        } else {
            location = locationManager.getLastKnownLocation(provider);
        }


        location = locationManager.getLastKnownLocation(provider);

        if (location == null) {
        } else {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            LatLng ll = new LatLng(lat, lng);

            return ll;
        }
        return new LatLng(0, 0);
    }

    public void getWeather(LatLng markerPos){
        ArrayList<String> p = new ArrayList<>();
        //getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, new SnowFragment()).commit();
        ForecastConfiguration configuration = new ForecastConfiguration.Builder("4c53088bf37f39b40f21165b81d5b69f")
                .setCacheDirectory(getCacheDir()).build();
        ForecastClient.create(configuration);
        ForecastClient.getInstance().getForecast(markerPos.latitude, markerPos.longitude, new Callback<Forecast>(){
            @Override
            public void onResponse(Response<Forecast> response){
                String title, description, uri, buildString = "";
                Date expires;
                if (response.isSuccess()){
                    Forecast forecast = response.body();
                    Log.i("j", "HERE IT IS: " + forecast.getAlerts());
                    if (!(forecast.getAlerts() == null)) {
                        for (Alert a : forecast.getAlerts()) {
                            title = a.getTitle();
                            description = a.getDescription();
                            uri = a.getUri();
                            expires = a.getExpires();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(WeatherActivity.this);
                            builder1.setMessage(
                                    getString(R.string.justAlerts) + "\n" +
                                            title + "\n" + description + "\n" + uri + "\n" + expires
                            );
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder1.create();
                            alert.show();
                        }
                    }

                    tv = (TextView) findViewById(R.id.textView);
                    buildString += getString(R.string.currentWeather) + " ";
                    switch (forecast.getCurrently().getIcon().toString()){
                        case "Clear-day": buildString += getString(R.string.clearDay);
                            break;
                        case "Clear-night": buildString += getString(R.string.clearNight);
                            break;
                        case "Rain": buildString += getString(R.string.rain);
                            break;
                        case "Snow": buildString += getString(R.string.snow);
                            break;
                        case "Sleet": buildString += getString(R.string.sleet);
                            break;
                        case "Wind": buildString += getString(R.string.wind);
                            break;
                        case "Fog": buildString += getString(R.string.fog);
                            break;
                        case "Cloudy": buildString += getString(R.string.cloudy);
                            break;
                        case "Partly-cloudy-day": buildString += getString(R.string.partlyCloudyDay);
                            break;
                        case "Partly-cloudy-night": buildString += getString(R.string.partlyCloudyNight);
                            break;
                        case "Hail": buildString += getString(R.string.hail);
                            break;
                        case "Thunderstorm": buildString += getString(R.string.thunderstorm);
                            break;
                        case "Tornado": buildString += getString(R.string.tornado);
                            break;
                        default: buildString += getString(R.string.defaultWeather);
                            break;
                    }
                    buildString += "\n" + getString(R.string.tempOf) + " " + (forecast.getCurrently().getApparentTemperature() != null ? forecast.getCurrently().getApparentTemperature().toString() : getString(R.string.defaultWeather)) + " " + getString(R.string.Farenheit) + "\n";
                    buildString += getString(R.string.nearestStorm) + " " + (forecast.getCurrently().getNearestStormDistance() != null ? forecast.getCurrently().getNearestStormDistance().toString() : getString(R.string.defaultWeather)) + " " + getString(R.string.miles);
                    tv.setText(buildString);
                }
            }
            @Override
            public void onFailure(Throwable t){
                t.printStackTrace();
            }
        });
    }
}
