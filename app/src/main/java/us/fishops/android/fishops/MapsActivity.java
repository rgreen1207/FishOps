package us.fishops.android.fishops;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import java.io.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<Polygon, String> hMap = new HashMap<Polygon, String>();
    private final static String TAG="j";
    File MPA = new File("../../../../../res/PN_MPAs.txt");

    ///home/ryan/Desktop/FishOps/app/src/main/java/us/fishops/android/fishops/MapsActivity.java
    ///home/ryan/Desktop/FishOps/app/src/main/res/PN_MPAs
    String line = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public static int getColorWithAlpha(int color, float ratio){
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    private ArrayList<LatLng> points = new ArrayList<LatLng>();

    // Pass an array of Lat/Long and String name
    private void createPoly(ArrayList<LatLng> points, String name){

//        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//        .add(new LatLng(14.675757,120.619596),new LatLng(14.655245,120.892407),new LatLng(14.517079,120.903715),new LatLng(14.506132,120.647866))
//                .strokeColor(Color.RED)
//                .strokeWidth(3)
//                .fillColor(getColorWithAlpha(Color.RED, 0.4f)));
        //polygon.setClickable(true);
        PolygonOptions options = new PolygonOptions();
        options.addAll(points);
        options.strokeColor(Color.RED);
        options.strokeWidth(3);
        options.fillColor(getColorWithAlpha(Color.RED,0.4f));
        Polygon polygon = mMap.addPolygon(options);
        polygon.setClickable(true);
        hMap.put(polygon,name);
    }

    private void parse(){
        String s1 = "122.152777778000086,13.372777778000057 122.170277778000013,13.35444444400008 122.198611111000105,13.330555556000093 122.172222222000073,13.321944444000053 122.172222222000073,13.306388889000061 122.163888889000077,13.295 122.139444444000105,13.286111111000082 122.093888889000027,13.328055556000038 122.152777778000086,13.372777778000057";
        String n1 = "Torrijos Community";
        points.clear();
        points = generatePoints(s1);
        //Log.i(TAG, points.toString());
        createPoly(points,n1);
        String s2 = "123.775,13.416833333000056 123.798,13.413833333000071 123.795166667000103,13.40716666700007 123.772333333,13.410166667000055 123.775,13.416833333000056";
        String n2 = "San Miguel Island";
        points.clear();
        points = generatePoints(s2);
        //Log.i(TAG, points.toString());
        createPoly(points,n2);
        String s3 = "124.927600065000092,10.649884265000082 124.928591874000062,10.646550020000063 124.928736831000037,10.64454623000006 124.928851271000099,10.642309730000079 124.929598943000087,10.640236318000063 124.931246874000067,10.638558705000037 124.934237565000103,10.63569846300004 124.935809203000076,10.634299338000062 124.937403730000028,10.632900215000063 124.938525238000011,10.63111673700007 124.938883816000043,10.629003268000076 124.93853286600006,10.626823992000084 124.937830969000061,10.62463327100005 124.93724351000003,10.622377696000058 124.936518725000042,10.619754936000049 124.934397776000083,10.620157411000037 124.932292084000096,10.620271860000059 124.93027031500003,10.61991802600005 124.928782599000101,10.621452581000085 124.927714496000021,10.623301865000087 124.928614756,10.625428686000078 124.928065446,10.627824460000056 124.927340661000017,10.629768164000041 124.927989154000102,10.63177767500008 124.927119413000014,10.634236395000073 124.92514342100003,10.635391365000089 124.924952688000076,10.637418044000071 124.923190316000046,10.63837082200007 124.921488980000049,10.639503855000044 124.919528246000027,10.640710326000089 124.917651434000049,10.641472358000044 124.91602639000007,10.640281148000042 124.913745224000081,10.640371753000068 124.911769232000097,10.641294965000043 124.910815568000089,10.643343581000067 124.911593759000084,10.64568403700008 124.913020442000061,10.647188071000073 124.915133762000096,10.64721763600005 124.917445444,10.648070271000051 124.919352774,10.647461791000069 124.921366913000043,10.647271997000075 124.92326661300001,10.646363090000079 124.9254180800001,10.646624412000051 124.925837693000062,10.648776029000089 124.927600065000092,10.649884265000082";
        String n3 = "Kuapnit Balinsasayao";
        points.clear();
        points = generatePoints(s3);
        //Log.i(TAG, points.toString());
        createPoly(points,n3);
    }

    private ArrayList<LatLng> generatePoints(String s){
        ArrayList<LatLng> p = new ArrayList<LatLng>();
        String tokens[] = s.split(" ");
        for(int i = 0; i < tokens.length-1; i++){
            String cords[] = tokens[i].split(",");
            //Log.i(TAG, Double.parseDouble(cords[0]));
            p.add(new LatLng(Double.parseDouble(cords[1]),Double.parseDouble(cords[0])));

        }
        Log.i(TAG, p.toString());
        return p;
    }

//    private void createHashMap()
//    {
//        try{
//            FileReader fileReader = new FileReader(MPA);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            while(MPA != eof)
//            {
//                hMap.put(createPoly(),MPA.getLine());
//            }
//        }
//    }

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

        // Add a marker in Sydney and move the camera
        LatLng manila = new LatLng(14.5995, 120.9842);
        mMap.addMarker(new MarkerOptions().position(manila).title("Marker in Manila"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(manila));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(14.5995, 120.9842),9.0f));

        //createPoly();
        parse();
        createHashMap();

        googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                String name = hMap.get(polygon);
                Toast.makeText(MapsActivity.this, name, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
