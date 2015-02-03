package by.evgen.android.apiclient.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.db.StorageDBHelper;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Decoder;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by evgen on 03.02.2015.
 */
public class StorageCursorAdapter extends SimpleCursorAdapter {

    private Cursor mDataCursor;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private String mPrevDate = null;

    public StorageCursorAdapter(Context context, int layout, Cursor dataCursor, String[] from,
                       int[] to, int flags) {
        super(context, layout, dataCursor, from, to, flags);
        if (dataCursor != null) {
            mImageLoader = ImageLoader.get(context);
            mDataCursor = dataCursor;
            mInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (mDataCursor != null) {
            mDataCursor.moveToPosition(position);
            int title = mDataCursor.getColumnIndex(StorageDBHelper.WIKI_NAME);
            String task_title = mDataCursor.getString(title);
            holder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.adapter_item, null);
                holder.name = (TextView) convertView.findViewById(android.R.id.text1);//Task Title
                holder.img = (ImageView) convertView.findViewById(android.R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String urlImage = Api.IMAGEVIEW_GET + Decoder.getHtml(task_title);
            holder.name.setText(task_title);
            mImageLoader.displayImage(urlImage, holder.img);
            return convertView;
        } else {
            return null;
        }

    }

    static class ViewHolder {
        TextView name;
        ImageView img;
    }

}
