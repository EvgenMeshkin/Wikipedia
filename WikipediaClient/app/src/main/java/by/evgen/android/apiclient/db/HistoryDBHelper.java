package by.evgen.android.apiclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import by.evgen.android.apiclient.utils.Log;


/**
 * Created by evgen on 13.12.2014.
 */
public class HistoryDBHelper extends SQLiteOpenHelper {

    public static final String WIKI_TABLE = "history";
    public static final String WIKI_ID = "_id";
    public static final String WIKI_NAME = "name";
    public static final String WIKI_DATE = "wikidate";
    static final String DB_NAME = "wikidb";
    static final int DB_VERSION = 2;
    static final String WIKI_CONTENT = "content";

    static final String DB_CREATE = "create table " + WIKI_TABLE + "("
            + WIKI_ID + " integer primary key autoincrement, "
            + WIKI_NAME + " text, " + WIKI_CONTENT + " text, " + WIKI_DATE + " integer" + ");";

    public HistoryDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.text(getClass(), "--- onCreate database ---");
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
