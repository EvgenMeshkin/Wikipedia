package com.example.evgenmeshkin.myasynctask;



import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;


import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> {

    private static  int CM_DELETE_ID = 1;
    ListView lvData;
    DB db;
    SimpleCursorAdapter scAdapter;
    final Uri CONTACT_URI = Uri
            .parse("content://ru.startandroid.providers.AdressBook/contacts");

    final String CONTACT_NAME = "name";
    final String CONTACT_EMAIL = "email";

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // открываем подключение к БД
        db = new DB(this);
        db.open();

        // формируем столбцы сопоставления
        String[] from = new String[] { DB.COLUMN_IMG, DB.COLUMN_TXT };
        int[] to = new int[] { R.id.ivImg, R.id.tvText };

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.adapter_item, null, from, to, 0);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);
        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);


    /*    Cursor cursor = getContentResolver().query(CONTACT_URI, null, null,
                null, null);
        startManagingCursor(cursor);

        String from[] = { "name", "email" };
        int to[] = { android.R.id.text1, android.R.id.text2 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.adapter_item, cursor, from, to);

        ListView lvContact = (ListView) findViewById(R.id.lvData);
        lvContact.setAdapter(adapter);*/
    }

    // обработка нажатия кнопки
    public void onAddClick(View view) {
        // добавляем запись
        db.addRec("sometext " + (scAdapter.getCount() + 1), R.drawable.ic_launcher);
        // получаем новый курсор с данными
        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    public void onDeleteClick(View view) {
        db.delRec(CM_DELETE_ID);
        CM_DELETE_ID++;
        // получаем новый курсор с данными
        getSupportLoaderManager().getLoader(0).forceLoad();
    }



    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {

        DB db;

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData();
            return cursor;
        }

    }
}

