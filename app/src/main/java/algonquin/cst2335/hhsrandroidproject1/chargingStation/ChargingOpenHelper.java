package algonquin.cst2335.hhsrandroidproject1.chargingStation;

import android.app.Activity;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChargingOpenHelper extends SQLiteOpenHelper {

    /**
     * database name
     */
    public static final String DATABASE_NAME = "ChargingStation";
    /**
     * version of a database
     */
    public static final int VERSION_NUM = 1;
    /**
     * table name
     */
    public static final String TABLE_NAME = "Stations";
    /**
     * column name in a table
     */
    public static final String COL_ID = "_id";
    /**
     * column name in a table
     */
    public static final String COL_TITLE = "TITLE";
    /**
     * column name in a table
     */
    public static final String COL_LATITUDE = "LATITUDE";
    /**
     * column name in a table
     */
    public static final String COL_LONGITUDE = "LONGITUDE";
    /**
     * column name in a table
     */
    public static final String COL_PHONE = "PHONE";

    public ChargingOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_LATITUDE + " REAL, "
                + COL_LONGITUDE + " REAL, " + COL_PHONE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
