package by.evgen.android.apiclient.processing;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.db.StorageDBHelper;
import by.evgen.android.apiclient.db.WikiContentProvider;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 07.02.2015.
 */
public class StorageGetProcessor implements Processor<List<String>,InputStream> {

    public Context mContext;

    public StorageGetProcessor (Context context) {
        mContext = context;
    }

    @Override
    public List<String> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONArray array = (JSONArray) jsonObject.get(Constant.RESPONSE);
        List<String> noteArray = new ArrayList<>(array.length());
        mContext.getContentResolver().delete(WikiContentProvider.WIKI_STORAGE_URI, null, null);
        ContentValues[] valueses = new ContentValues[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Category category = new Category(object);
            String stringValue = category.getValue();
            ContentValues cv = new ContentValues();
            cv.put(StorageDBHelper.WIKI_NAME, stringValue);
            valueses[i] = cv;
            noteArray.add(stringValue);
            Log.d(getClass(), stringValue);
        }
        if (mContext != null) {
            mContext.getContentResolver().bulkInsert(WikiContentProvider.WIKI_STORAGE_URI, valueses);
        }
        return noteArray;
    }

}
