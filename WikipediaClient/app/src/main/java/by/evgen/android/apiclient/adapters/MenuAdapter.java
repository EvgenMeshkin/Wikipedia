package by.evgen.android.apiclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.utils.EnumMenuItems;

/**
 * Created by User on 02.02.2015.
 */
public class MenuAdapter extends BaseAdapter {

    private Context mContext;
    private EnumMenuItems[] mItems;

    public MenuAdapter (Context context, EnumMenuItems[] items){
          mContext = context;
          mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
       return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.drawer_list_item, null);
        }
        TextView title = (TextView) convertView.findViewById(android.R.id.content);
        title.setText(mItems[position].getTitle());
        ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
        imageView.setImageResource(mItems[position].getIcon());
        TranslateAnimation anim = new TranslateAnimation(-200, 0, 0, 0);
        anim.setDuration(100 + position * 200);
        convertView.startAnimation(anim);
        convertView.setTag(position);
        return convertView;
    }
}
