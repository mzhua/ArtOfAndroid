package im.hua.artofandroid.chapter_245;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by hua on 2017/1/29.
 */

public class BookProvider extends ContentProvider {
    public static final String AUTHORITY = "im.hua.artofandroid.chapter_245.BookProvider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int BOOK_URI_CODE = 0;
    private static final int USER_URI_CODE = 1;

    private DBOpenHelper mDBOpenHelper;


    static {
        mUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        mUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (mUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DBOpenHelper.TABLE_NAME_BOOK;
                break;
            case USER_URI_CODE:
                tableName = DBOpenHelper.TABLE_NAME_USER;
                break;
        }
        return tableName;
    }

    @Override
    public boolean onCreate() {
        mDBOpenHelper = new DBOpenHelper(getContext(), null, null, 0);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d("BookProvider", "query");
        String tableName = getTableName(uri);
        if (null == tableName) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        SQLiteDatabase readableDatabase = mDBOpenHelper.getReadableDatabase();
        return readableDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d("BookProvider", "insert");
        String tableName = getTableName(uri);
        if (null == tableName) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        SQLiteDatabase writableDatabase = mDBOpenHelper.getWritableDatabase();
        writableDatabase.insert(tableName, null, values);
        getContext().getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d("BookProvider", "delete");
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d("BookProvider", "update");
        return 0;
    }
}
