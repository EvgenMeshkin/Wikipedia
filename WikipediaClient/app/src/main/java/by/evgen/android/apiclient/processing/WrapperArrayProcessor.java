package by.evgen.android.apiclient.processing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 22.10.2014.
 */
public abstract class WrapperArrayProcessor <T> implements Processor<List<T>,InputStream> {

    @Override
    public List<T> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONArray array = createArray(jsonObject);
        List<T> noteArray = new ArrayList<T>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            noteArray.add(createObject(object));
        }
        Log.d(getClass(), noteArray.toString());
        return noteArray;
    }

    protected abstract T createObject(JSONObject jsonObject);


    protected abstract JSONArray createArray (JSONObject jsonObject) throws JSONException;

}

