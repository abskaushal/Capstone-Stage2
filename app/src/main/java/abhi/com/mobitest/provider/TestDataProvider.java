package abhi.com.mobitest.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Abhishek on 08-Jan-17.
 */
public class TestDataProvider extends ContentProvider {

    public final static String AUTHORITY = "abhi.com.mobitest.data.provider";
    public static final String TEST_URL = "content://" + AUTHORITY + "/tests";
    public static final Uri CONTENT_URI = Uri.parse(TEST_URL);

    public static final String ACTION_TEST_UPDATE = "abhi.com.mobitest.provider.ACTION_TEST_UPDATE";

    public static String _ID = "id";
    public static String TITLE = "title";
    public static String DURATION = "duration";
    public static String USER_ID = "user_id";
    public static String TEST_ID = "test_id";
    public static String QUESTION = "question";
    public static String DESCRIPTION = "description";


    private static HashMap<String, String> TESTS_PROJECTION_MAP;

    static final int TESTS = 1;
    static final int TESTS_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "tests", TESTS);
        uriMatcher.addURI(AUTHORITY, "tests/#", TESTS_ID);
    }


    /**
     * Database specific constant declarations
     */

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Test";
    static final String TESTS_TABLE_NAME = "tests";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + TESTS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " title TEXT NOT NULL, " +
                    " user_id TEXT NOT NULL, " +
                    " test_id TEXT NOT NULL, " +
                    " question TEXT NOT NULL, " +
                    " description TEXT NOT NULL, " +
                    " duration TEXT NOT NULL);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  TESTS_TABLE_NAME);
            onCreate(db);
        }
    }




    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TESTS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case TESTS:
                qb.setProjectionMap(TESTS_PROJECTION_MAP);
                break;

            case TESTS_ID:
                qb.appendWhere( TEST_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        /*if (sortOrder == null || sortOrder == ""){
            *//**
             * By default sort on id
             *//*
            sortOrder = _ID;
        }*/

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, null);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all test records
             */
            case TESTS:
                return "vnd.android.cursor.dir/vnd.example.tests";
            /**
             * Get a particular test
             */
            case TESTS_ID:
                return "vnd.android.cursor.item/vnd.example.tests";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = db.insert(	TESTS_TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            updateWidget();
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case TESTS:
                count = db.delete(TESTS_TABLE_NAME, selection, selectionArgs);
                break;

            case TESTS_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( TESTS_TABLE_NAME, _ID +  " = " + id +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case TESTS:
                count = db.update(TESTS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case TESTS_ID:
                count = db.update(TESTS_TABLE_NAME, values,
                        _ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private void updateWidget(){
        Intent intent = new Intent(ACTION_TEST_UPDATE).setPackage(getContext().getPackageName());
        getContext().sendBroadcast(intent);
    }

}
