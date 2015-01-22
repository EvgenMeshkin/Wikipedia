package by.evgen.android.apiclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by evgen on 13.12.2014.
 */
public class DBHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "wikidb";
    static final int DB_VERSION = 1;
    static final String WIKI_TABLE = "geodata";
    static final String WIKI_ID = "_id";
    static final String WIKI_NAME = "name";
    static final String WIKI_EMAIL = "koordinaty";
    static final String WIKI_CONTENT = "content";
    static final String WIKI_DATE = "wikidate";

    static final String DB_CREATE = "create table " + WIKI_TABLE + "("
            + WIKI_ID + " integer primary key autoincrement, "
            + WIKI_NAME + " text, " + WIKI_EMAIL + " text, " + WIKI_CONTENT + " text, " + WIKI_DATE + " integer" + ");";
    final static String LOG_TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
