package by.evgen.android.apiclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.helper.wikihelper.LoaderRandomArray;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Decoder;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 28.01.2015.
 */
public class RandomArrayAdapter extends ArrayAdapter<Category> {

    private LoaderRandomArray mLoader;
    private Context mContext;

    public RandomArrayAdapter(Context context, int resource, List<Category> objects) {
        super(context, resource, objects);
        mContext = context;
        mLoader = LoaderRandomArray.get(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_item_cardview, null);
        }
        Category item = getItem(position);
        Log.d(getClass(), item.getTitle());
        TextView title = (TextView) convertView.findViewById(android.R.id.title);
        title.setText(Constant.EMPTY);
        TextView content = (TextView) convertView.findViewById(android.R.id.content);
        content.setText(Constant.LOAD);
        ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
        imageView.setImageBitmap(null);
        String url = Api.EXTRAS_PAGE_GET + Decoder.getHtml(item.getTitle());
        convertView.setTag(url);
        mLoader.displayView(url, convertView);
    return convertView;
    }

}

