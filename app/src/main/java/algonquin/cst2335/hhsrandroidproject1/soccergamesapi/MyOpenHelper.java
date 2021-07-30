package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class to manage database creation and version management.
 * @author Minghui Liao
 * @version 1.0
 */
public class MyOpenHelper extends SQLiteOpenHelper {
    /**This sets the database name as "TheDatabase".*/
    public static final String name = "TheSoccerGameDatabase";
    /**Represent the data column of the database*/
    public static final int version = 1;
    /**The name of the article table*/
    public static final String TABLE_NAME = "Article";
    /**This is column name of article title*/
    public static final String col_title = "Title";
    /**This is the column name of article date*/
    public static final String col_date = "Date";
    /**This is the column name of the image url.*/
    public static final String col_image_url = "ImageUrl";
    /**This is the column name of the article url*/
    public static final String col_article_url = "ArticleUrl";
    /**This is the column name of the article description*/
    public static final String col_description = "Description";

    /**
     * This constructor creates a helper object to create, open, and/or manage a database.
     * @param context Context object that is the activity context.
     */
    public MyOpenHelper(Context context) {
        super(context, name, null, version);
    }

    /**
     * Called when the database is created for the first time.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + "("
                + col_title + " TEXT,"
                + col_date + " TEXT,"
                + col_description + " TEXT,"
                + col_article_url + " TEXT,"
                + col_image_url + " TEXT);");
    }

    /**
     * Called when the database needs to be upgraded.
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
