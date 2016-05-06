package us.fishops.android.fishops;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
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
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {
    private TextView tv;
    private static final int REQUEST_CODE_LOCATION = 2;
    private DecimalFormat df = new DecimalFormat("#.#");
    //private static final String TAG = "WeatherActivity";
    private static boolean mapsStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent = getIntent();
        mapsStarted = intent.getBooleanExtra("mapsStarted", false);
    }

    @Override
    public void onStart(){
        super.onStart();
        getWeather(getPosition());
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onPause(){
        super.onPause();
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
        double lat, lng;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request missing location permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
        }

        // When in debug mode, grab the mock location's coordinates. It shouldn't be grabbed in release mode.
        if (mapsStarted && BuildConfig.DEBUG){
            //return MapsActivity.currentMarker.getPosition();
            lat = MapsActivity.getCurrentMarker().getPosition().latitude;
            lng = MapsActivity.getCurrentMarker().getPosition().longitude;
        } else {
            Location location = locationManager.getLastKnownLocation(provider);

            // Default to Manilla in the Philippines if location object is null
            if (location == null) {
                // These coordinates are for Manilla in the Philippines
                lat= 14.5995;
                lng = 120.9842;
            } else {
                // Location object isn't null, so get the last known location of the user
                lat= location.getLatitude();
                lng = location.getLongitude();
            }
        }

        return new LatLng(lat, lng);
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
                    String weatherType;
                    if (forecast.getCurrently() != null &&
                            forecast.getCurrently().getIcon() != null &&
                            forecast.getCurrently().getIcon().getText() != null){
                        weatherType = forecast.getCurrently().getIcon().getText().toLowerCase();
                    } else {
                        weatherType = "unknown";
                    }

                    buildString += "\n\n" + getResources().getString(R.string.location) + ": " +
                                   "\n\t" + parseReverseGeocode(getReverseGeocode(markerPos.latitude, markerPos.longitude)).toUpperCase() +
                                   "\n\t" + getString(R.string.latitude) + ": " + markerPos.latitude +
                                   "\n\t" + getString(R.string.longitude) + ": " + markerPos.longitude +
                                   "\n\n" + getString(R.string.currentWeather) + " \n\t";

                    if (weatherType.matches(".*clear.*day.*")){ buildString += getString(R.string.clearDay);
                    } else if (weatherType.matches(".*clear.*night.*")){ buildString += getString(R.string.clearNight);
                    } else if (weatherType.matches(".*rain.*")){ buildString += getString(R.string.rain);
                    } else if (weatherType.matches(".*snow.*")){ buildString += getString(R.string.snow);
                    } else if (weatherType.matches(".*sleet.*")){ buildString += getString(R.string.sleet);
                    } else if (weatherType.matches(".*wind.*")){ buildString += getString(R.string.wind);
                    } else if (weatherType.matches(".*fog.*")){ buildString += getString(R.string.fog);
                    } else if (weatherType.matches(".*cloudy.*")){ buildString += getString(R.string.cloudy);
                    } else if (weatherType.matches(".*partly.*cloudy.*day.*")){ buildString += getString(R.string.partlyCloudyDay);
                    } else if (weatherType.matches(".*partly.*cloudy.*night.*")){ buildString += getString(R.string.partlyCloudyNight);
                    } else if (weatherType.matches(".*hail.*")){ buildString += getString(R.string.hail);
                    } else if (weatherType.matches(".*thunderstorm.*")){ buildString += getString(R.string.thunderstorm);
                    } else if (weatherType.matches(".*tornado.*")){ buildString += getString(R.string.tornado);
                    } else { buildString += getString(R.string.defaultWeather); }

                    // Display the temperature in F and C
                    buildString += "\n\n" + getString(R.string.tempOf) + " \n\t" + // Display the temperature label
                        (forecast.getCurrently().getApparentTemperature() != null ? // We ask: is there a valid temperature?
                            df.format(forecast.getCurrently().getApparentTemperature()) + " \u2109 / " + // If so: show in Fahrenheit
                                df.format(((5.0 / 9.0) * (forecast.getCurrently().getApparentTemperature() - 32.0))) + " \u2103" : // If so: show in Celsius as well
                            getString(R.string.defaultWeather)) + // If no valid temperature, show "UNKNOWN"
                                   "\n\n" + getString(R.string.nearestStorm) + " \n\t" + // Display the nearest storm label
                        (forecast.getCurrently().getNearestStormDistance() != null ? // We ask: is there a storm nearby?
                            forecast.getCurrently().getNearestStormDistance() == 0.0 ? // If so, we then ask: is that distance zero?
                                getString(R.string.vicinity) : // If zero: show "In your vicinity"
                                forecast.getCurrently().getNearestStormDistance() + " " + getString(R.string.miles) : // If not zero, show distance in miles
                            getString(R.string.defaultWeather)); // If no storm nearby, show "UNKNOWN"
                    tv.setText(buildString);
                }
            }
            @Override
            public void onFailure(Throwable t){
                t.printStackTrace();
            }
        });
    }

    protected List<Address> getReverseGeocode(double lat, double lng) {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(WeatherActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    protected String parseReverseGeocode(List<Address> addresses) {
        Address address;
        if (addresses == null || addresses.size() == 0) {
//            Toast.makeText(WeatherActivity.this, "No location found", Toast.LENGTH_SHORT)
//                    .show();
            return getResources().getString(R.string.defaultWeather);
        }
        address = addresses.get(0);

        String aLine = "";
        for (int addr = 0; addr <= address.getMaxAddressLineIndex() - 2; addr++) {
            aLine = aLine.length() > 0 ? aLine + "\n"
                    + String.valueOf(address.getAddressLine(addr)) : String
                    .valueOf(address.getAddressLine(addr));
        }
        address.setAddressLine(0, aLine);
//        Toast.makeText(WeatherActivity.this, aLine, Toast.LENGTH_LONG)
//                .show();
        return (aLine.matches("[ \\t\\n\\x0b\\r\\f]*") ? getResources().getString(R.string.defaultWeather) : aLine);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
