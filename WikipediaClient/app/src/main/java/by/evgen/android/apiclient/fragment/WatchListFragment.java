package by.evgen.android.apiclient.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.DateAdapter;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.db.HistoryDBHelper;
import by.evgen.android.apiclient.db.StorageDBHelper;
import by.evgen.android.apiclient.db.provider.WikiContentProvider;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.FindResponder;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 06.01.2015.
 */
public class WatchListFragment extends AbstractDbFragment {

    @Override
    public CursorLoader getCursorLoader(String val) {
        return new CursorLoader(getActivity(), WikiContentProvider.WIKI_HISTORY_URI, mFrom, "name LIKE  '%" + val + "%'", null, null);
    }

    @Override
    public SimpleCursorAdapter getAdapter(Context context, Cursor data) {
        mFrom = new String[]{HistoryDBHelper.WIKI_ID, HistoryDBHelper.WIKI_NAME, HistoryDBHelper.WIKI_DATE};
        mTo = new int[]{R.id.title, R.id.content};
        return new DateAdapter(context, R.layout.adapter_item, data, mFrom, mTo, 0);
    }

    @Override
    public Void sync() {
        return null;
    }

    @Override
    public NoteGsonModel getNote(Cursor cursor) {
        return new NoteGsonModel(cursor.getLong(cursor.getColumnIndex(HistoryDBHelper.WIKI_ID)),
                cursor.getString(cursor.getColumnIndex(HistoryDBHelper.WIKI_NAME)),
                cursor.getString(cursor.getColumnIndex(HistoryDBHelper.WIKI_DATE)));
    }

}
