package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * The class is the database creation class for  application
 */
public class MovieHelper extends SQLiteOpenHelper {
    /**
     * Initialization for  the database variables
     */
    protected final static int VERSION_NUM = 2;
    protected final static String DATABASE_NAME = "MovieDB";

    /**
     * Initialization for both tables variables used
     *
     */
    public final static String TABLE_NAME1 = "Movies";
    public final static String TABLE_NAME2 = "Movie";
    public final static String T1Col1 = "id";
    public final static String T1Col2 = "MoviesID";
    public final static String T1Col3 = "ActorName";
    public final static String T1Col4 = "Year";
    public final static String T1Col5 = "Genre";
    public final static String T1Col6 = "Description";
    public final static String T1Col7 = "MoviesName";
    public final static String T2Col1 = "dMoID";
    public final static String T2Col2 = "MoviesID";
    public final static String T2Col3 = "MovieName";



    public MovieHelper( Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * String for creating the Movies table
         */
        String createTable1;
        createTable1 = "CREATE TABLE " + TABLE_NAME1 + "(" + T1Col1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                T1Col2+" INTEGER , " +
                T1Col3+" TEXT, " +
                T1Col4+" INTEGER, " +
                T1Col5+" TEXT, " +
                T1Col6+" TEXT, " +
                T1Col7+" TEXT)";

        /**
         * String for creating the Movie table
         */
        String createTable2;
        createTable2 = "CREATE TABLE " +TABLE_NAME2+"("+T2Col1+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                T2Col2+" INTEGER, " +
                T2Col3+" TEXT)";

        /**
         * Executing the strings
         */
        db.execSQL(createTable1);
        db.execSQL(createTable2);
    }

    /**
     * Method for on upgrade drops the tables
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME1);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2);
        onCreate(db);

    }

    /**
     * Method for on downgrade drops the tables
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("ChatDatabaseHelper", "Calling onDowngrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME1);
        Log.i("ChatDatabaseHelper", "Calling onDowngrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2);
        onCreate(db);
    }
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME1);
        db.execSQL("delete from "+ TABLE_NAME2);
        db.close();
    }

}
