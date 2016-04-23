package us.fishops.android.fishops;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Random;

import us.fishops.android.fishops.R;

/**
 * Created by Josh on 4/23/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

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
                values.put(KEY_RESTRICTIONS, R.string.no_restrictions);
            } else if(rand < 8){
                values.put(KEY_RESTRICTIONS, R.string.some_restrictions);
            } else {
                values.put(KEY_RESTRICTIONS, R.string.no_fishing);
            }

            //insert content
            db.insert(TABLE_MPA, null, values);

            Log.d(TAG, "database insert: " + mpaArray[i]);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}