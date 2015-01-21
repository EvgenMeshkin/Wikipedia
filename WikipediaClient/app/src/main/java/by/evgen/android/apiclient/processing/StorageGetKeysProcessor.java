package by.evgen.android.apiclient.processing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import by.evgen.android.apiclient.bo.Category;

/**
 * Created by User on 21.01.2015.
 */
public class StorageGetKeysProcessor implements Processor<List<String>,InputStream> {

    @Override
    public List<String> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONArray array = (JSONArray) jsonObject.get("response");
        List<String> noteArray = new ArrayList<String>(array.length());
        for (int i = 0; i < array.length(); i++) {
            String stringValue = array.getString(i);
            noteArray.add(stringValue);
        }
        return noteArray;
    }

}
