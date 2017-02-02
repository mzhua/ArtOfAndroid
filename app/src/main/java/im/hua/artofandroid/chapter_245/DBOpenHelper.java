package im.hua.artofandroid.chapter_245;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hua on 2017/2/1.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "book_provider.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME_BOOK = "book";
    public static final String TABLE_NAME_USER = "user";
    private final String CREATE_TABLE_BOOK = "create table if not exists "
            + TABLE_NAME_BOOK + "(_id integer primary key,"
            + "name text)";
    private final String CREATE_TABLE_USER = "create table if not exists "
            + TABLE_NAME_USER + "(_id integer primary key,"
            + "name text)";



    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOK);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
