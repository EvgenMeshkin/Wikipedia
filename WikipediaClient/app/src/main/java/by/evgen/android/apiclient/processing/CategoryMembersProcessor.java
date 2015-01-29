package by.evgen.android.apiclient.processing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import by.evgen.android.apiclient.bo.Category;

/**
 * Created by User on 28.01.2015.
 */
public class CategoryMembersProcessor extends WrapperArrayProcessor<Category> {

    @Override
    protected Category createObject(JSONObject jsonObject) {
        return new Category(jsonObject);
    }

    @Override
    protected JSONArray createArray(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObjectquery = jsonObject.getJSONObject("query");
        JSONArray array = (JSONArray)jsonObjectquery.get("categorymembers");
        return array;
    }

}