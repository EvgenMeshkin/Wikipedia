package by.evgen.android.apiclient.processing;

import org.json.JSONObject;

import java.io.InputStream;

import by.evgen.android.apiclient.utils.Constant;

/**
 * Created by evgen on 13.01.2015.
 */
public class NoteProcessor implements Processor<Long,InputStream>{

    @Override
    public Long process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        return jsonObject.getLong(Constant.RESPONSE);
    }

}