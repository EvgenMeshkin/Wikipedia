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
import by.evgen.android.apiclient.utils.Decoder;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by User on 21.01.2015.
 */
public class FavouritesArrayAdapter extends ArrayAdapter<String> {

    private ImageLoader mImageLoader;
    private Context mContext;

    public FavouritesArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mImageLoader = ImageLoader.get(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_item, null);
        }
        String title = getItem(position);
        TextView name = (TextView) convertView.findViewById(android.R.id.text1);
        name.setText(title);
        final String urlImage = Api.IMAGEVIEW_GET + Decoder.getHtml(title);
        convertView.setTag(title);
        final ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
        imageView.setImageBitmap(null);
        imageView.setTag(urlImage);
        mImageLoader.displayImage(urlImage, imageView);
        return convertView;
    }

}

