package algonquin.cst2335.hhsrandroidproject1.oct;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class OCTOpenHelper extends SQLiteOpenHelper {
    public static final String name = "OCTDatabase";
    public static final int version = 1;
    public static final String TABLE_NAME = "Histories";
    public static final String col_stationNumber = "StationNumber";
    public static final String col_search_button = "Search";


    public OCTOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + col_stationNumber + " TEXT,"
                + col_search_button + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL( "drop table if exists " + TABLE_NAME);
         onCreate(db);

    }
}
