package by.evgen.android.apiclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import by.evgen.android.apiclient.utils.Log;


/**
 * Created by User on 03.02.2015.
 */
public class StorageDBHelper extends SQLiteOpenHelper {

    public static final String WIKI_TABLE = "storage";
    public static final String WIKI_ID = "_id";
    public static final String WIKI_NAME = "title";
    static final String DB_NAME = "wikdb";
    static final int DB_VERSION = 2;
    static final String WIKI_CONTENT = "content";
    static final String WIKI_DATE = "wikidate";

    static final String DB_CREATE = "create table " + WIKI_TABLE + "("
            + WIKI_ID + " integer primary key autoincrement, "
            + WIKI_NAME + " text, "  + WIKI_CONTENT + " text, " + WIKI_DATE + " integer" + ");";

    public StorageDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(getClass(), "--- onCreate database ---");
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
