package by.evgen.android.apiclient.processing;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 28.01.2015.
 */
public class ExtrasProcessor implements Processor<List<Category>,InputStream>{

    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        Log.text(getClass(), "run");
        //TODO keys to constants
        JSONObject query = jsonObject.getJSONObject("query");
        Log.text(getClass(), "run1");
        JSONObject pages = query.getJSONObject("pages");
        Log.text(getClass(), "run2");
        Iterator<?> i = pages.keys();
        Log.text(getClass(), "run3");
        JSONObject pagesId = pages.getJSONObject(i.next().toString());
        Log.text(getClass(), "run4");
      //  JSONObject thumbnail = pagesId.getJSONObject("extract");
        Log.text(getClass(), "run5");
        List<Category> noteArray = new ArrayList<Category>(pagesId.length());
        Category category = new Category(pagesId);
        Log.text(getClass(), "run6");
        noteArray.add(category);
        return noteArray;
    }

}
