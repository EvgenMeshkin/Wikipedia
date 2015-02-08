package by.evgen.android.apiclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.utils.Decoder;
import by.evgen.android.apiclient.utils.Log;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by User on 13.01.2015.
 */
public class RecyclerWikiAdapter extends RecyclerView.Adapter<RecyclerWikiAdapter.ViewHolder> {

    private List<Category> mRecords;
    private ImageLoader mImageLoader;

    public RecyclerWikiAdapter(Context context, List<Category> records) {
        mImageLoader = ImageLoader.get(context);
        mRecords = records;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d(this.getClass(), "Start recycler");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Log.d(this.getClass(), "Bind recycler");
        Category item = mRecords.get(i);
        viewHolder.name.setText(item.getTitle());
        viewHolder.content.setText(item.getDist() + " m.");
        final String urlImage = Api.IMAGEVIEW_GET + Decoder.getHtml(item.getTitle());
        viewHolder.icon.setImageBitmap(null);
        viewHolder.icon.setTag(urlImage);
        mImageLoader.displayImage(urlImage, viewHolder.icon);
    }

   @Override
    public int getItemCount() {
        return mRecords.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView content;
        private ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(android.R.id.title);
            content = (TextView) itemView.findViewById(android.R.id.content);
            icon = (ImageView) itemView.findViewById(android.R.id.icon);
        }
    }

}
