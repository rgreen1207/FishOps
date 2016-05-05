package us.fishops.android.fishops;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;
import android.zetterstrom.com.forecast.models.Alert;
import android.zetterstrom.com.forecast.models.Forecast;
import com.google.android.gms.maps.model.LatLng;
import java.text.DecimalFormat;
import java.util.Date;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {
    private TextView tv;
    private static final int REQUEST_CODE_LOCATION = 2;
    private DecimalFormat df = new DecimalFormat("#.#");
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getWeather(getPosition());
    }

    @Override
    public void onResume(){
        super.onResume();
        getWeather(getPosition());
    }

    public LatLng getPosition() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request missing location permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        }

        location = locationManager.getLastKnownLocation(provider);
        if (location == null) return new LatLng(0, 0);
        else return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void getWeather(final LatLng markerPos){
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
                    buildString += "\n\n" + getString(R.string.location) + " \n\t" +
                                            getString(R.string.latitude) + ": " + markerPos.latitude + "\n\t" +
                                            getString(R.string.longitude) + ": " + markerPos.longitude +
                                   "\n\n" + getString(R.string.currentWeather) + " \n\t";
                    switch (forecast.getCurrently().getIcon().toString()){
                        case "Clear-day": buildString += getString(R.string.clearDay); break;
                        case "Clear-night": buildString += getString(R.string.clearNight); break;
                        case "Rain": buildString += getString(R.string.rain); break;
                        case "Snow": buildString += getString(R.string.snow); break;
                        case "Sleet": buildString += getString(R.string.sleet); break;
                        case "Wind": buildString += getString(R.string.wind); break;
                        case "Fog": buildString += getString(R.string.fog); break;
                        case "Cloudy": buildString += getString(R.string.cloudy); break;
                        case "Partly-cloudy-day": buildString += getString(R.string.partlyCloudyDay); break;
                        case "Partly-cloudy-night": buildString += getString(R.string.partlyCloudyNight); break;
                        case "Hail": buildString += getString(R.string.hail); break;
                        case "Thunderstorm": buildString += getString(R.string.thunderstorm); break;
                        case "Tornado": buildString += getString(R.string.tornado); break;
                        default: buildString += getString(R.string.defaultWeather); break;
                    }

                    // Display the temperature in F and C
                    buildString += "\n\n" + getString(R.string.tempOf) + " \n\t" +
                                          (forecast.getCurrently().getApparentTemperature()
                                                  != null ? df.format(forecast.getCurrently().getApparentTemperature()) + " \u2109 / " +
                                                            df.format(((5.0 / 9.0) * (forecast.getCurrently().getApparentTemperature() - 32.0))) + " \u2103"
                                                  : getString(R.string.defaultWeather)) +
                                    "\n\n" + getString(R.string.nearestStorm) + " \n\t" +
                                          (forecast.getCurrently().getNearestStormDistance()
                                                  != null ? forecast.getCurrently().getNearestStormDistance() + " " + getString(R.string.miles)
                                                  : getString(R.string.defaultWeather));
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
