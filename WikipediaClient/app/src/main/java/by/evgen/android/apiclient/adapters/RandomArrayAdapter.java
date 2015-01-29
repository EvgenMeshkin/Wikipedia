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
    public View mConvertView;
    public ImageView mImageView;
    public int mFinalHeight;
    public int mFinalWidth;


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
        TextView title = (TextView) convertView.findViewById(android.R.id.text1);
        title.setText("");
        mContent = (TextView) convertView.findViewById(android.R.id.text2);
        mContent.setText("");
        mImageView = (ImageView) convertView.findViewById(android.R.id.icon);
        mImageView.setImageBitmap(null);
        convertView.setTag(Api.EXTRAS_PAGE_GET + item.getTitle().replaceAll(" ", "%20"));
        mLoader.displayView(Api.EXTRAS_PAGE_GET + item.getTitle().replaceAll(" ", "%20"), convertView);
    return convertView;
    }

}

