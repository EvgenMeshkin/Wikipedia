package by.evgen.android.apiclient.processing;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
        for (int i = 0; i < array.length(); i++) {
            String stringValue = array.getString(i);
            noteArray.add(stringValue);
            Log.d(getClass(), stringValue);
        }
        return noteArray;
    }

}
