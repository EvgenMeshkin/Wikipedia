package by.evgen.android.apiclient.processing;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import by.evgen.android.apiclient.db.HistoryDBHelper;
import by.evgen.android.apiclient.db.StorageDBHelper;
import by.evgen.android.apiclient.db.provider.WikiContentProvider;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 21.01.2015.
 */
public class StorageGetKeysProcessor implements Processor<List<String>,InputStream> {

    public Context mContext;

    public StorageGetKeysProcessor (Context context) {
        mContext = context;
    }

    @Override
    public List<String> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONArray array = (JSONArray) jsonObject.get(Constant.RESPONSE);
        List<String> noteArray = new ArrayList<String>(array.length());
        mContext.getContentResolver().delete(WikiContentProvider.WIKI_STORAGE_URI, null, null);
        ContentValues[] valueses = new ContentValues[array.length()];
        for (int i = 0; i < array.length(); i++) {
            String stringValue = array.getString(i);
            ContentValues cv = new ContentValues();
            cv.put(StorageDBHelper.WIKI_NAME, stringValue);
            valueses[i] = cv;
            noteArray.add(stringValue);
            Log.text(getClass(), stringValue);
        }
        if (!mContext.equals(null)) {
            mContext.getContentResolver().bulkInsert(WikiContentProvider.WIKI_STORAGE_URI, valueses);
        }
        return noteArray;
    }

}
