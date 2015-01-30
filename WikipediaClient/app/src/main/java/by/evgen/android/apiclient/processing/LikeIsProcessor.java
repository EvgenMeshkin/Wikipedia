package by.evgen.android.apiclient.processing;

import org.json.JSONObject;

import java.io.InputStream;

import by.evgen.android.apiclient.utils.Constant;

/**
 * Created by User on 12.01.2015.
 */
public class LikeIsProcessor implements Processor<String,InputStream>{

    @Override
    public String process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject query = jsonObject.getJSONObject(Constant.getResponse());
        String liked = query.getString(Constant.getLiked());
        return liked;
    }

}
