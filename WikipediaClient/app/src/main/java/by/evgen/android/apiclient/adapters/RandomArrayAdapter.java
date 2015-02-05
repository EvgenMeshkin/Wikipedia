package by.evgen.android.apiclient.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.helper.LoaderRandomArray;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Decoder;
import by.evgen.android.apiclient.utils.Log;
import by.evgen.android.imageloader.CircleMaskedBitmap;

/**
 * Created by evgen on 28.01.2015.
 */
public class RandomArrayAdapter extends ArrayAdapter<Category> {

    private TextView mTitle;
    public TextView mContent;
    private LoaderRandomArray mLoader;
    private Context mContext;
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
        mTitle = (TextView) convertView.findViewById(android.R.id.text1);
        mTitle.setText(Constant.EMPTY);
        mContent = (TextView) convertView.findViewById(android.R.id.text2);
        mContent.setText(Constant.LOAD);
        mImageView = (ImageView) convertView.findViewById(android.R.id.icon);
        mImageView.setImageBitmap(null);
        convertView.setTag(Api.EXTRAS_PAGE_GET + Decoder.getHtml(item.getTitle()));
        mLoader.displayView(Api.EXTRAS_PAGE_GET + Decoder.getHtml(item.getTitle()), convertView);
    return convertView;
    }

}

