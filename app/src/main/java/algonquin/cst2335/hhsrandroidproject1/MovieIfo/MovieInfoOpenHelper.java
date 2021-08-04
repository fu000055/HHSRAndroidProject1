package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The class to manage database creation and version management.
 * @author Simon Ao
 * @version 1.0
 */

public class MovieInfoOpenHelper extends SQLiteOpenHelper {

    public static final String name = "TheMovieDatabase";
    public static final int version = 1;
    public static final String TABLE_NAME = "Movies";

    public static final String col_title = "Title";
    public static final String col_year = "Year";
    public static final String col_rating = "Rating";
    public static final String col_runtime = "Runtime";
    public static final String col_actors = "Actors";
    public static final String col_plot = "Plot";

    public MovieInfoOpenHelper( Context context ) {
        super(context, name, null, version);
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + "("
                + col_title + " TEXT,"
                + col_year + " TEXT,"
                + col_rating + " TEXT,"
                + col_runtime + " TEXT,"
                + col_actors + " TEXT,"
                + col_plot + " TEXT);");

    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "drop table if exists " + TABLE_NAME);
        onCreate(db);

    }
}
