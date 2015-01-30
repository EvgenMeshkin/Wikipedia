package by.evgen.android.apiclient.processing;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 24.01.2015.
 */
public class MainPageProcessor implements Processor<List<Category>,InputStream> {

    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject parse = jsonObject.getJSONObject(Constant.getParse());
        Log.text(getClass(), "MainPage");
        String text = parse.getString(Constant.getText());
        List<Category> category = new ArrayList<Category>();
        category.add(new Category(parse.getJSONObject(Constant.getText())));
        Log.text(getClass(), text);
        return category;
    }

}
