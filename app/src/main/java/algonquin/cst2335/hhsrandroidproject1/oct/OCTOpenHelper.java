package algonquin.cst2335.hhsrandroidproject1.oct;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class to manage database creation and version management.
 * @author Rong Fu
 * @version 1.0
 */

public class OCTOpenHelper extends SQLiteOpenHelper {
    /** This sets the database name as "OCTDatabase"*/
    public static final String name = "OCTDatabase";
    /** Represent the data column of the database*/
    public static final int version = 1;
    /** The name of the Favourites table"*/
    public static final String TABLE_NAME = "Histories";
    /** The column name of the station number */
    public static final String col_stationNumber = "StationNumber";
    /** The column name of the search button */
    public static final String col_search_button = "Search";

    /** This constructor creates a helper object to create, open, and manage a database.
     *
     * @param context Context object that is the activity context.
     */
    public OCTOpenHelper(Context context) {
        super(context, name, null, version);
    }

    /**
     * This function calls when the database is created for the first time.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + col_stationNumber + " TEXT,"
                + col_search_button + " INTEGER);");
    }

    /**
     * This function calls when the database needs to be updated.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL( "drop table if exists " + TABLE_NAME);
         onCreate(db);

    }
}
