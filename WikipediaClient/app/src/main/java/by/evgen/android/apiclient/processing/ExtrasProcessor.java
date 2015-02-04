package by.evgen.android.apiclient.processing;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.utils.Constant;
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
        JSONObject query = jsonObject.getJSONObject(Constant.QUERY);
        JSONObject pages = query.getJSONObject(Constant.PAGES);
        Iterator<?> i = pages.keys();
        JSONObject pagesId = pages.getJSONObject(i.next().toString());
        List<Category> noteArray = new ArrayList<Category>(pagesId.length());
        Category category = new Category(pagesId);
        noteArray.add(category);
        return noteArray;
    }

}
