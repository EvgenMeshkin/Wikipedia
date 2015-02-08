package by.evgen.android.apiclient.fragment;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.DateAdapter;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.db.HistoryDBHelper;
import by.evgen.android.apiclient.db.WikiContentProvider;

/**
 * Created by evgen on 06.01.2015.
 */
public class WatchListFragment extends AbstractDbFragment {

    @Override
    public CursorLoader getCursorLoader(String val) {
        return new CursorLoader(getActivity(), WikiContentProvider.WIKI_HISTORY_URI, mFrom, "name LIKE ?", new String[]{"%" + val + "%"}, null);
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
