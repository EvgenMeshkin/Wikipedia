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
                         int[] to) {
        super(context, layout, dataCursor, from, to);
        mImageLoader = ImageLoader.get(context);
        mDataCursor = dataCursor;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        mDataCursor.moveToPosition(position);
        if (position > 0 ) {
            mDataCursor.moveToPrevious();
            mPrevDate = (new java.sql.Date(mDataCursor.getLong(mDataCursor.getColumnIndex("wikidate")))).toString();
            mDataCursor.moveToNext();
        }
        int title = mDataCursor.getColumnIndex("name");
        String task_title = mDataCursor.getString(title);
        int title_date = mDataCursor.getColumnIndex("wikidate");
        Long task_day = mDataCursor.getLong(title_date);
        String dt = (new java.sql.Date(task_day)).toString();
        holder = new ViewHolder();
        if (convertView == null) {
            if (!dt.equals(mPrevDate)) {
                convertView = mInflater.inflate(by.evgen.android.apiclient.R.layout.view_separator, null);
                holder.sec_hr = (TextView) convertView.findViewById(android.R.id.text2);
                holder.sec_hr.setVisibility(View.VISIBLE);
            } else {
                convertView = mInflater.inflate(by.evgen.android.apiclient.R.layout.adapter_item, null);
                holder.sec_hr = (TextView) convertView.findViewById(android.R.id.text2);
                holder.sec_hr.setVisibility(View.GONE);
            }
            holder.name = (TextView) convertView.findViewById(android.R.id.text1);//Task Title
            holder.img = (ImageView) convertView.findViewById(android.R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.sec_hr.setText(dt);
        final String urlImage = Api.IMAGEVIEW_GET + task_title.replaceAll(" ", "%20");
        holder.name.setText(task_title);
        mImageLoader.displayImage(urlImage, holder.img);
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView sec_hr;
        ImageView img;
    }

}