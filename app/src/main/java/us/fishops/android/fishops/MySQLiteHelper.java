package us.fishops.android.fishops;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

import us.fishops.android.fishops.R;

/**
 * Created by Josh on 4/23/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // Content
    private Context context;

    // Database Name - LibraryDB
    private static final String DATABASE_NAME = "FishDB";

    // Table Name - mpa
    private static final String TABLE_MPA = "mpa";

    // Columns Names of mpa Table
    private static final String KEY_NAME = "name";
    private static final String KEY_RESTRICTIONS = "restrictions";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Log TAG for debugging purpose
    private static final String TAG = "SQLiteAppLog";
    private Resources resources;

    private String[] mpaArray;

    // Constructor
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 7);
        this.context = context;

        mpaArray = context.getResources().getStringArray(R.array.names_array);
    }

    public void onCreate(SQLiteDatabase db) {

        // SQL statements to fill in data
        String CREATE_MPA_TABLE = "CREATE TABLE mpa ( " +
                "name TEXT, " +
                "restrictions TEXT)";

        //create the table
        db.execSQL(CREATE_MPA_TABLE);

        //set string array resource
        //String[] mpaArray;
        //mpaArray = getResources().getStringArray(R.array.names_array);

        //set up random ints
        Random r = new Random();

        //loop through array values and input into database
        for (int i = 0; i < mpaArray.length; i++) {

            //get new random value
            int rand = r.nextInt(10);


            //define content values
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, mpaArray[i]);


            //insert random restrictions value from strings
            if(rand < 4) {
                //val = "no_restrictions";

                values.put(KEY_RESTRICTIONS, context.getResources().getString(R.string.no_restrictions));
            } else if(rand < 8){
                //val = "some restrictions";
                values.put(KEY_RESTRICTIONS, context.getResources().getString(R.string.some_restrictions));
            } else {
                //val = "no fishign";
                values.put(KEY_RESTRICTIONS, context.getResources().getString(R.string.no_fishing));
            }
           //insert content
            db.insert(TABLE_MPA, null, values);

            //Log.d(TAG, "database insert: " + mpaArray[i]);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // Gets data from Database to send to DisplayInformation class
    public String getAllData(String LocationName) {
        String newDets = "Not Available";

        try {
            //access items from database
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT  * FROM mpa WHERE name=?", new String[]{LocationName});

            //get descriptions
            if (cursor != null && cursor.moveToFirst()) {
                do {
//                    cursor.getString(0);
                    newDets = cursor.getString(1);
                    //Log.d("Details", newDets);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Log.d(TAG,"Broken");
        }
        //return to function in DisplayInformation
        return newDets;
    }

}