package by.evgen.android.apiclient.helper;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.CoreApplication;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.os.assist.LIFOLinkedBlockingDeque;
import by.evgen.android.apiclient.processing.ExtrasProcessor;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.source.CachedHttpDataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by evgen on 29.01.2015.
 */
public class LoaderRandomArray {

    private Map<View, String> mViews = new ConcurrentHashMap<View, String>();
    private ExecutorService mExecutorService;
    public static final String KEY = "LoaderRandomArray";
    private Context mContext;

    public LoaderRandomArray(Context context){
        mContext = context;
        mExecutorService = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS,
                new LIFOLinkedBlockingDeque<Runnable>());
    }

    public static LoaderRandomArray get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    public void displayView(final String title, final View convertView){
        mViews.put(convertView, title);
        queueView(title, convertView);
      }

    private void queueView(String url, View convertView){
        MemoryValue p = new MemoryValue(url, convertView, mContext);
        mExecutorService.submit(new DataLoader(p));
    }

    private class MemoryValue {

        public final String url;
        public final View convertView;
        public final HttpDataSource dataSource;
        public final HttpDataSource dataUrl;
        public final Processor processUrl;

        public MemoryValue(String url, View convertView, Context context){
            this.url = url;
            this.convertView = convertView;
            this.dataSource = new CachedHttpDataSource(context);
            dataUrl = new HttpDataSource();
            processUrl = new ExtrasProcessor();
        }
    }

    private class DataLoader implements Runnable {

        private final MemoryValue mMemoryValue;
        private Handler mHandler = new Handler();
        private DataLoader(MemoryValue mMemoryValue){
            this.mMemoryValue = mMemoryValue;
        }

        @Override
        public void run() {
            try{
                InputStream dataUrl = mMemoryValue.dataUrl.getResult(mMemoryValue.url);
                Object procesUrl = mMemoryValue.processUrl.process(dataUrl);
                List<Category> data = (List<Category>)procesUrl;
                Category category = data.get(0);
                if (viewReused(mMemoryValue)) {
                    return;
                }
                ViewDisplayer bd = new ViewDisplayer(category, mMemoryValue);
                mHandler.post(bd);
            }catch(Throwable th){
                return;
            }
        }
    }

    synchronized boolean viewReused(MemoryValue memoryValue){
        String tag = mViews.get(memoryValue.convertView);
        if (tag.equals(null) || !tag.equals(memoryValue.url))
            return true;
        return false;
    }

    class ViewDisplayer implements Runnable{

        private Category category;
        private MemoryValue memoryValue;

        public ViewDisplayer(Category category, MemoryValue memoryValue){
            this.category = category;
            this.memoryValue = memoryValue;
        }

        public void run(){
            if (viewReused(memoryValue)) {
                return;
            }
            TextView title = (TextView) memoryValue.convertView.findViewById(android.R.id.text1);
            TextView content = (TextView) memoryValue.convertView.findViewById(android.R.id.text2);
            ImageView imageView = (ImageView) memoryValue.convertView.findViewById(android.R.id.icon);
            if (category != null) {
                title.setText(category.getTitle());
                content.setText(category.getExtract());
                final String urlImage = Api.IMAGEVIEW_GET + category.getTitle().replaceAll(" ", "%20");
                imageView.setImageBitmap(null);
                imageView.setTag(urlImage);
                ImageLoader imageLoader = ImageLoader.get(mContext);
                imageLoader.displayImage(urlImage, imageView);
            }
        }
    }


}
