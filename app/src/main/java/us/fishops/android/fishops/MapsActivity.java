package us.fishops.android.fishops;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<Polygon, String> hMap = new HashMap<Polygon, String>();
    private final static String TAG = "j";
    private static final int REQUEST_CODE_LOCATION = 2;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    private ArrayList<LatLng> points = new ArrayList<LatLng>();

    // Pass an array of Lat/Long and String name
    private void createPoly(ArrayList<LatLng> points, String name) {

//        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//        .add(new LatLng(14.675757,120.619596),new LatLng(14.655245,120.892407),new LatLng(14.517079,120.903715),new LatLng(14.506132,120.647866))
//                .strokeColor(Color.RED)
//                .strokeWidth(3)
//                .fillColor(getColorWithAlpha(Color.RED, 0.4f)));
//        polygon.setClickable(true);

        PolygonOptions options = new PolygonOptions();
        options.addAll(points);
        options.strokeColor(Color.RED);
        options.strokeWidth(3);
        options.fillColor(getColorWithAlpha(Color.RED, 0.4f));
        Polygon polygon = mMap.addPolygon(options);
        polygon.setClickable(true);
        hMap.put(polygon, name);
    }

    private void parse() {
        String name, cords;

        final String[] names = getResources().getStringArray(R.array.names_array);
        final String[] locs = getResources().getStringArray(R.array.loc_array);
        for (int i = 0; i < names.length; i++) {
            name = names[i];
            cords = locs[i];
            points = generatePoints(cords);
            createPoly(points, name);
        }
    }

    private ArrayList<LatLng> generatePoints(String s) {
        ArrayList<LatLng> p = new ArrayList<LatLng>();
        String tokens[] = s.split(" ");
        for (int i = 0; i < tokens.length - 1; i++) {
            String cords[] = tokens[i].split(",");
            p.add(new LatLng(Double.parseDouble(cords[1]), Double.parseDouble(cords[0])));

        }
        return p;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

//        Add a marker in Sydney and move the camera
//        LatLng manila = new LatLng(14.5995, 120.9842);
//        mMap.addMarker(new MarkerOptions().position(manila).title("Marker in Manila"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(manila));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(14.5995, 120.9842),9.0f));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom - 0.5f));
//        createPoly();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }

        Location location = null;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request missing location permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        } else {

            // Location permission has been granted, continue as usual.
        }

        location = locationManager.getLastKnownLocation(provider);

        if(location == null) {
            Log.i(TAG, "location object is null");

            LatLng manila = new LatLng(14.5995, 120.9842);
            mMap.addMarker(new MarkerOptions().position(manila).draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(manila));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(manila,10.0f));
            Log.i(TAG, "Marker set in Manila");

//            LocationListener locationListener = new LocationListener() {
//                void onLocationChanged(Location location) {
//                    double lat = location.getLatitude();
//                    double lng = location.getLongitude();
//                    LatLng ll = new LatLng(lat, lng);
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 20));
//                }
//            };

        }
        else{
            double lat= location.getLatitude();
            double lng = location.getLongitude();
            LatLng ll = new LatLng(lat, lng);
            Log.i(TAG, "LatLng set from location object");

            mMap.addMarker(new MarkerOptions().position(ll).draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 10.0f));
            Log.i(TAG, "marker added LatLng");
        }

//        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        mMap.setMyLocationEnabled(true);
//        Log.i(TAG, "before setting myLatLong");
//        Log.i(TAG, "Lat: " + location.getLatitude());
//        LatLng myLatLong = new LatLng(location.getLatitude(),location.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(myLatLong).title("My Location"));
//        Log.i(TAG, "before camera zoom");
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLong, 12.0f));
//        Log.i(TAG, "after camera zoom");

        parse();

        googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                String name = hMap.get(polygon);
                sendMessage(name);
            }
        });

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {

                //Toast.makeText(MapsActivity.this, marker.getPosition().toString(), Toast.LENGTH_SHORT).show();

                boolean contained = false;
                String name = "";
                for(Map.Entry<Polygon, String> entry : hMap.entrySet()) {
                    Polygon key = entry.getKey();
                    String value = entry.getValue();
                    if(PolyUtil.containsLocation(marker.getPosition(), key.getPoints(), false)){
                        contained = true;
                        name = value;
                    }
                }
                if(contained){

                    //Toast.makeText(MapsActivity.this, "Entered a MPA", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
                    builder1.setMessage(getResources().getString(R.string.alert_dialog_1) + " " + name + getResources().getString(R.string.alert_dialog_2));
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
        });
    }

    public void sendMessage(String name) {

        //Toast.makeText(MapsActivity.this, name, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, DisplayInformation.class);
        intent.putExtra(DisplayInformation.EXTRA_MESSAGE, name);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.

                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),

                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://us.fishops.android.fishops/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.

                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),

                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://us.fishops.android.fishops/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }
}