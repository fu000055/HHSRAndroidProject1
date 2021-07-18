package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
    public static  final  String name = "TheDatabase";
    public static  final  int version = 1;

    public static  final  String TABLE_NAME = "Messages";
    public static  final  String col_message = "Message";
    public static  final  String col_sent_receive = "SendOrReceive";
    public static  final  String col_time_sent = "TimeSent";

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + col_message + " Text,"
                + col_sent_receive + " INTERGE,"
                + col_time_sent + " TEXT);");
    }

    public MyOpenHelper(Context context) {
        super(context, name, null, version);
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME); //delete the table
        onCreate(db);
    }
}
