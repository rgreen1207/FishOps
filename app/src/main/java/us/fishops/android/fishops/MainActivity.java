package us.fishops.android.fishops;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    final Handler handler = new Handler();
    Timer    timer = new Timer();
    MapsActivity mappy = new MapsActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            getWeather(mappy.getMarkerPosition());
                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10);
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void getWeather(LatLng markerPos){
        ArrayList<String> p = new ArrayList<>();
        //getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, new SnowFragment()).commit();
        ForecastConfiguration configuration = new ForecastConfiguration.Builder("4c53088bf37f39b40f21165b81d5b69f")
            .setCacheDirectory(getCacheDir()).build();
        ForecastClient.create(configuration);
        ForecastClient.getInstance().getForecast(markerPos.latitude, markerPos.longitude, new Callback<Forecast>(){
            @Override
            public void onResponse(Response<Forecast> response, ArrayList p){
                String title, description, uri;
                Date expires;
                if (response.isSuccess()){
                    Forecast forecast = response.body();
                    if (!(forecast.getAlerts().isEmpty())) {
                        for (Alert a : forecast.getAlerts()) {
                            title = a.getTitle();
                            description = a.getDescription();
                            uri = a.getUri();
                            expires = a.getExpires();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setMessage(
                                    "@strings/justAlerts\n" +
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

                    //p.add(new ArrayList<String>(forecast.getCurrently().getSummary(), forecast.getCurrently().getApparentTemperature().toString(), forecast.getCurrently().getNearestStormDistance().toString()));
                }
            }
            @Override
            public void onFailure(Throwable t){
                t.printStackTrace();
            }
        });
        return p;
    }
}