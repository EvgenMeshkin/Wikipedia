package by.evgen.android.apiclient.fragment;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.adapters.StorageCursorAdapter;
import by.evgen.android.apiclient.auth.Authorized;
import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.db.StorageDBHelper;
import by.evgen.android.apiclient.db.WikiContentProvider;
import by.evgen.android.apiclient.utils.Constant;

/**
 * Created by evgen on 03.02.2015.
 */
public class StorageFragment extends AbstractDbFragment {

    @Override
    public CursorLoader getCursorLoader(String val) {
       return new CursorLoader(getActivity(), WikiContentProvider.WIKI_STORAGE_URI, mFrom, "title LIKE ?", new String[]{"%" + val + "%"}, null);
    }

    @Override
    public SimpleCursorAdapter getAdapter(Context context, Cursor data) {
         mFrom = new String[]{StorageDBHelper.WIKI_ID, StorageDBHelper.WIKI_NAME};
         mTo = new int[]{R.id.title, R.id.content};
         return new StorageCursorAdapter(context, R.layout.adapter_item, data, mFrom, mTo, 0);
    }

    @Override
    public Void sync() {
        if (Authorized.isLogged()) {
            Account vkAccount = new Account(getActivity().getString(R.string.acount_name), Constant.ACCOUNT_TYPE);
            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            ContentResolver.requestSync(vkAccount, WikiContentProvider.AUTHORITY, bundle);
        }
        return null;
    }

    @Override
    public NoteGsonModel getNote(Cursor cursor) {
        return new NoteGsonModel(cursor.getLong(cursor.getColumnIndex(StorageDBHelper.WIKI_ID)),
                        cursor.getString(cursor.getColumnIndex(StorageDBHelper.WIKI_NAME)),
                        cursor.getString(cursor.getColumnIndex(StorageDBHelper.WIKI_NAME)));
    }

}
