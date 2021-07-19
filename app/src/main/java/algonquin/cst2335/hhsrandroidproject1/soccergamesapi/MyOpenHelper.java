package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
    public static final String name = "TheDatabase";
    public static final int version = 1;
    public static final String TABLE_NAME = "Article";
    public static final String col_title = "Title";
    public static final String col_date = "Date";
    public static final String col_image_url = "ImageUrl";
    public static final String col_article_url = "ArticleUrl";
    public static final String col_description = "Description";

    public MyOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + "("
                + col_title + " TEXT,"
                + col_date + " TEXT,"
                + col_description + " TEXT,"
                + col_article_url + " TEXT,"
                + col_image_url + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
