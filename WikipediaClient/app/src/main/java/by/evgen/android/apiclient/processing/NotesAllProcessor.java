package by.evgen.android.apiclient.processing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.utils.Constant;

/**
 * Created by User on 12.01.2015.
 */
public class NotesAllProcessor extends WrapperArrayProcessor<Category>{

    @Override
    protected Category createObject(JSONObject jsonObject) {
        return new Category(jsonObject);
    }

    @Override
    protected JSONArray createArray(JSONObject jsonObject) throws JSONException {
        JSONObject query = jsonObject.getJSONObject(Constant.RESPONSE);
        return (JSONArray)query.get(Constant.ITEMS);
    }

}
