package by.evgen.android.apiclient.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 13.12.2014.
 */
public class WikiContentProvider extends ContentProvider {

    public static final String AUTHORITY = "by.evgen.android.apiclient.WikiData";
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
    private StorageDBHelper storageDBHelper;
    private SQLiteDatabase db;

    public boolean onCreate() {
        Log.d(getClass(), "onCreate");
        storageDBHelper = new StorageDBHelper(getContext());
        historyDbHelper = new HistoryDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(getClass(), "query, " + uri.toString());
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case URI_HISTORY:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "date(" + HistoryDBHelper.WIKI_DATE + ") ASC";
                }
                db = historyDbHelper.getWritableDatabase();
                cursor = db.query(HistoryDBHelper.WIKI_TABLE, projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        WIKI_HISTORY_URI);
                break;
            case URI_HISTORY_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = HistoryDBHelper.WIKI_ID + " = " + id;
                } else {
                    selection = selection + " AND " + HistoryDBHelper.WIKI_ID + " = " + id;
                }
                db = storageDBHelper.getWritableDatabase();
                cursor = db.query(HistoryDBHelper.WIKI_TABLE, projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        WIKI_HISTORY_URI);
                break;
            case URI_STORAGE:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = StorageDBHelper.WIKI_NAME + " ASC";
                }
                db = storageDBHelper.getWritableDatabase();
                cursor = db.query(StorageDBHelper.WIKI_TABLE, projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        WIKI_STORAGE_URI);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(getClass(), "insert, " + uri.toString());
        Uri resultUri = null;
        switch (uriMatcher.match(uri)) {
            case URI_HISTORY:
                db = historyDbHelper.getWritableDatabase();
                long rowID;
                db.beginTransaction();
                try {
                    rowID = db.insert(HistoryDBHelper.WIKI_TABLE, null, values);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                resultUri = ContentUris.withAppendedId(WIKI_HISTORY_URI, rowID);
                break;
            case URI_HISTORY_ID:
                break;
            case URI_STORAGE:
                db = storageDBHelper.getWritableDatabase();
                long storID;
                db.beginTransaction();
                try {
                    storID = db.insert(StorageDBHelper.WIKI_TABLE, null, values);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                resultUri = ContentUris.withAppendedId(WIKI_STORAGE_URI, storID);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(getClass(), "delete, " + uri.toString());
        int cnt;
        switch (uriMatcher.match(uri)) {
            case URI_HISTORY:
                db = historyDbHelper.getWritableDatabase();
                cnt = db.delete(HistoryDBHelper.WIKI_TABLE, selection, selectionArgs);
                break;
            case URI_HISTORY_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = HistoryDBHelper.WIKI_ID + " = " + id;
                } else {
                    selection = selection + " AND " + HistoryDBHelper.WIKI_ID + " = " + id;
                }
                db = historyDbHelper.getWritableDatabase();
                cnt = db.delete(HistoryDBHelper.WIKI_TABLE, selection, selectionArgs);
                break;
            case URI_STORAGE:
                db = storageDBHelper.getWritableDatabase();
                cnt = db.delete(StorageDBHelper.WIKI_TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(getClass(), "update, " + uri.toString());
        int cnt;
        switch (uriMatcher.match(uri)) {
            case URI_HISTORY:
                db = historyDbHelper.getWritableDatabase();
                cnt = db.update(HistoryDBHelper.WIKI_TABLE, values, selection, selectionArgs);
                break;
            case URI_HISTORY_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = HistoryDBHelper.WIKI_ID + " = " + id;
                } else {
                    selection = selection + " AND " + HistoryDBHelper.WIKI_ID + " = " + id;
                }
                db = historyDbHelper.getWritableDatabase();
                cnt = db.update(HistoryDBHelper.WIKI_TABLE, values, selection, selectionArgs);
                break;
            case URI_STORAGE:
                db = storageDBHelper.getWritableDatabase();
                cnt = db.update(StorageDBHelper.WIKI_TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] allValues) {
        db = storageDBHelper.getWritableDatabase();
        int numInserted = 0;
        String table = StorageDBHelper.WIKI_TABLE;
        db.beginTransaction();
        try {
            for (ContentValues cv : allValues) {
                long newID = db.insertWithOnConflict(table, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                if (newID <= 0) {
                    throw new SQLException("Error to add: " + uri);
                }
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            numInserted = allValues.length;
        } finally {
            db.endTransaction();
        }
        return numInserted;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(getClass(), "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_HISTORY:
                return WIKI_HISTORY_TYPE;
            case URI_HISTORY_ID:
                return WIKI_HISTORY_ITEM_TYPE;
            case URI_STORAGE:
                return WIKI_STORAGE_TYPE;
            case URI_STORAGE_ID:
                return WIKI_STORAGE_ITEM_TYPE;
        }
        return null;
    }
}