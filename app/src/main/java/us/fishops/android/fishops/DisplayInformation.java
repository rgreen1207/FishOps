package us.fishops.android.fishops;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteOpenHelper;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Created by Karen on 4/23/2016.
 */
public class DisplayInformation extends Activity {
    public static final String EXTRA_MESSAGE = "test";
    private static final String TAG = "SQLiteAppLog";
    Typeface oswald;
    //Typeface oswald;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate2");
        setContentView(R.layout.activity_display_information);

        Log.d(TAG, "onCreate");
        Intent intent = getIntent();
        String LocationName = intent.getStringExtra(EXTRA_MESSAGE);
        Log.d(TAG, "location");
        locationInformation(LocationName);
        Log.d(TAG, "locationInformation");

    }
    public void locationInformation(String LocationName){
        Log.d(TAG, "entered locationInformation");
        MySQLiteHelper database = new MySQLiteHelper(this);
        String result = database.getAllData(LocationName);
        //Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        TextView textElement = (TextView) findViewById(R.id.location);
        TextView text = (TextView) findViewById(R.id.restriction);
        //textElement.setText(LocationName);
        //Bitmap bitmap = new Bitmap();


        Log.d(TAG, "ifStatement");

        oswald = Typeface.createFromAsset(getApplicationContext().getAssets(), "Oswald-Bold.ttf");
        //Typeface.createFromAsset(getApplicationContext().getAssets(), "Oswald-Bold.ttf");
        
        //Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        if(result.equals("No Restrictions")){
            ImageView imageView = (ImageView) findViewById(R.id.bg);
            imageView.setImageResource(R.drawable.blue);
            textElement.setText(LocationName);
            text.setText(result);
            textElement.setTypeface(oswald);
            text.setTypeface(oswald);
        }
        else if(result.equals("Some Restrictions")){
            ImageView imageView = (ImageView) findViewById(R.id.bg);
            imageView.setImageResource(R.drawable.yellow);
            textElement.setText(LocationName);
            text.setText(result);
            textElement.setTypeface(oswald);
            text.setTypeface(oswald);
        }
        else if(result.equals("No Fishing")){
            ImageView imageView = (ImageView) findViewById(R.id.bg);
            imageView.setImageResource(R.drawable.red);;
            textElement.setText(LocationName);
            text.setText(result);
            textElement.setTypeface(oswald);
            text.setTypeface(oswald);
        }


    }

}
