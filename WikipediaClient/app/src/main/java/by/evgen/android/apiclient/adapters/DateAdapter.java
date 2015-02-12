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
import by.evgen.android.apiclient.db.HistoryDBHelper;
import by.evgen.android.apiclient.utils.Decoder;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by evgen on 11.01.2015.
 */
public class DateAdapter extends SimpleCursorAdapter {

    private Cursor mDataCursor;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private String mPrevDate = null;

    public DateAdapter(Context context, int layout, Cursor dataCursor, String[] from,
                       int[] to, int flags) {
        super(context, layout, dataCursor, from, to, flags);
        if (dataCursor != null) {
            mImageLoader = ImageLoader.get(context);
            mDataCursor = dataCursor;
            mInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (mDataCursor != null) {
            mDataCursor.moveToPosition(position);
            if (position > 0) {
                mDataCursor.moveToPrevious();
                mPrevDate = (new java.sql.Date(mDataCursor.getLong(mDataCursor.getColumnIndex(HistoryDBHelper.WIKI_DATE)))).toString();
                mDataCursor.moveToNext();
            }
            int title = mDataCursor.getColumnIndex(HistoryDBHelper.WIKI_NAME);
            String task_title = mDataCursor.getString(title);
            int title_date = mDataCursor.getColumnIndex(HistoryDBHelper.WIKI_DATE);
            Long task_day = mDataCursor.getLong(title_date);
            String dt = (new java.sql.Date(task_day)).toString();
            holder = new ViewHolder();
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            }
            if (!dt.equals(mPrevDate)) {
                convertView = mInflater.inflate(by.evgen.android.apiclient.R.layout.view_separator, null);
                holder.sec_hr = (TextView) convertView.findViewById(android.R.id.content);
                holder.sec_hr.setVisibility(View.VISIBLE);
            } else {
                convertView = mInflater.inflate(by.evgen.android.apiclient.R.layout.adapter_item, null);
                holder.sec_hr = (TextView) convertView.findViewById(android.R.id.content);
                holder.sec_hr.setVisibility(View.GONE);
            }
            holder.name = (TextView) convertView.findViewById(android.R.id.title);
            holder.img = (ImageView) convertView.findViewById(android.R.id.icon);
            convertView.setTag(holder);
            holder.sec_hr.setText(dt);
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
        TextView sec_hr;
        ImageView img;
    }

}