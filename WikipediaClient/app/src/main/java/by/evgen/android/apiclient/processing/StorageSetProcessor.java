package by.evgen.android.apiclient.processing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import by.evgen.android.apiclient.bo.Category;

/**
 * Created by User on 21.01.2015.
 */
public class StorageSetProcessor implements Processor<Long,InputStream> {

    @Override
    public Long process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        Long id = jsonObject.getLong("response");
        return id;
    }

}