package by.evgen.android.apiclient.db.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import by.evgen.android.apiclient.db.HistoryDBHelper;

/**
 * Created by evgen on 13.12.2014.
 */
public class WikiContentProvider extends ContentProvider {

    final String LOG_TAG = WikiContentProvider.class.getSimpleName();
    // Table
    static final String WIKI_TABLE = "history";
    // Items
    static final String WIKI_ID = "_id";
    static final String WIKI_DATE = "wikidate";
    // Uri
    // authority
    //TODO why GeoData equals HISTORY and usedin WatchList ?
    static final String AUTHORITY = "by.evgen.android.apiclient.WikiData";
    static final String HISTORY = "history";
    static final String STORAGE = "storage";

    public static final Uri WIKI_HISTORY_URI = Uri.parse("content://"
            + AUTHORITY + "/" + HISTORY);
    static final String WIKI_HISTORY_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + HISTORY;
    static final String WIKI_HISTORY_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + HISTORY;

    public static final Uri WIKI_STORAGE_URI = Uri.parse("content://"
            + AUTHORITY + "/" + STORAGE);
    static final String WIKI_STORAGE_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + STORAGE;
    static final String WIKI_STORAGE_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + STORAGE;
    // UriMatcher
    static final int URI_HISTORY = 1;
    static final int URI_HISTORY_ID = 2;
    static final int URI_STORAGE = 3;
    static final int URI_STORAGE_ID = 4;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, HISTORY, URI_HISTORY);
        uriMatcher.addURI(AUTHORITY, HISTORY + "/#", URI_HISTORY_ID);
        uriMatcher.addURI(AUTHORITY, STORAGE, URI_STORAGE);
        uriMatcher.addURI(AUTHORITY, STORAGE + "/#", URI_STORAGE_ID);
    }

    private HistoryDBHelper historyDbHelper;
    private SQLiteDatabase db;

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        historyDbHelper = new HistoryDBHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_HISTORY:
                Log.d(LOG_TAG, "URI_HISTORY");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "date(" + WIKI_DATE + ") DESC";
                }
                break;
            case URI_HISTORY_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_HISTORY_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = WIKI_ID + " = " + id;
                } else {
                    selection = selection + " AND " + WIKI_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = historyDbHelper.getWritableDatabase();
        Cursor cursor = db.query(WIKI_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                WIKI_HISTORY_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_HISTORY)
            throw new IllegalArgumentException("Wrong URI: " + uri);
        db = historyDbHelper.getWritableDatabase();
        long rowID = db.insert(WIKI_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(WIKI_HISTORY_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    //TODO add overides to all overrided methods
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_HISTORY:
                Log.d(LOG_TAG, "URI_HISTORY");
                break;
            case URI_HISTORY_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_HISTORY_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = WIKI_ID + " = " + id;
                } else {
                    selection = selection + " AND " + WIKI_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = historyDbHelper.getWritableDatabase();
        //TODO
        int cnt = db.delete(WIKI_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_HISTORY:
                Log.d(LOG_TAG, "URI_HISTORY");
                break;
            case URI_HISTORY_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_HISTORY_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = WIKI_ID + " = " + id;
                } else {
                    selection = selection + " AND " + WIKI_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = historyDbHelper.getWritableDatabase();
        int cnt = db.update(WIKI_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_HISTORY:
                return WIKI_HISTORY_TYPE;
            case URI_HISTORY_ID:
                return WIKI_HISTORY_ITEM_TYPE;
        }
        return null;
    }
}