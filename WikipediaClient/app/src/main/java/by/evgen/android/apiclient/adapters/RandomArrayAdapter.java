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
import by.evgen.android.apiclient.helper.LoaderRandomArray;
import by.evgen.android.apiclient.helper.ManagerDownload;
import by.evgen.android.apiclient.processing.ExtrasProcessor;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.utils.Log;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by evgen on 28.01.2015.
 */
public class RandomArrayAdapter extends ArrayAdapter<Category> {

    private TextView mTitle;
    private TextView mContent;
    private LoaderRandomArray mLoader;
    private Context mContext;
    public View mConvertView;
    public ImageView mImageView;


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
        Log.text(getClass(), item.getTitle());
        convertView.setTag(Api.EXTRAS_PAGE_GET + item.getTitle().replaceAll(" ", "%20"));
        mLoader.displayImage(Api.EXTRAS_PAGE_GET + item.getTitle().replaceAll(" ", "%20"), convertView);
    return convertView;
    }



}

