package by.evgen.android.apiclient.processing;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.db.HistoryDBHelper;
import by.evgen.android.apiclient.db.provider.WikiContentProvider;
import by.evgen.android.apiclient.utils.Constant;

/**
 * Created by User on 08.01.2015.
 */
public class MobileViewProcessor extends WrapperArrayProcessor<Category>{

    public Context mContext;
    public String mTitle;

    public MobileViewProcessor (Context context, String title) {
        mContext = context;
        mTitle = title;
    }

    @Override
    protected Category createObject(JSONObject jsonObject) {
        return new Category(jsonObject);
    }

    @Override
    protected JSONArray createArray(JSONObject jsonObject) throws JSONException {
        JSONObject query = jsonObject.getJSONObject(Constant.getMobile());
        JSONArray array = (JSONArray)query.get(Constant.getSections());
        ContentValues cv = new ContentValues();
        cv.put(HistoryDBHelper.WIKI_NAME, mTitle);
        cv.put(HistoryDBHelper.WIKI_DATE, new java.util.Date().getTime());
        if (!cv.equals(null) && !mContext.equals(null)) {
            mContext.getContentResolver().insert(WikiContentProvider.WIKI_HISTORY_URI, cv);
        }
        return array;
    }

}