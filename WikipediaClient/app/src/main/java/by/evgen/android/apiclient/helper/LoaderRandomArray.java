package by.evgen.android.apiclient.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.CoreApplication;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.os.assist.LIFOLinkedBlockingDeque;
import by.evgen.android.apiclient.processing.BitmapProcessor;
import by.evgen.android.apiclient.processing.ExtrasProcessor;
import by.evgen.android.apiclient.processing.ImageUrlProcessor;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.source.CachedHttpDataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.imageloader.CircleMaskedBitmap;
import by.evgen.android.imageloader.ImageLoader;
import by.evgen.android.imageloader.MemoryCache;

/**
 * Created by evgen on 29.01.2015.
 */
public class LoaderRandomArray {
    private static final String TAG = "ImageLoader";
    private MemoryCache memoryCache = new MemoryCache();
    private Map<View, String> mViews = new ConcurrentHashMap<View, String>();
    private ExecutorService mExecutorService;
    public static final String KEY = "LoaderRandomArray";
    private Context mContext;
    private AtomicBoolean isPause = new AtomicBoolean(false);
    final int stub_id = by.evgen.android.apiclient.R.drawable.stub;
    private Set<ImageView> mImagesViews = new HashSet<ImageView>();
    private final Object mLock = new Object();

    public LoaderRandomArray(Context context){
        mContext = context;
        mExecutorService = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS,
                new LIFOLinkedBlockingDeque<Runnable>());
    }

    public static LoaderRandomArray get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    public void displayImage(final String title,final View convertView){
        mViews.put(convertView, title);
        queueImage(title, convertView);
      }

    private void queueImage(String url, View convertView){
        MemoryValue p = new MemoryValue(url, convertView, mContext);
        mExecutorService.submit(new ImagesLoader(p));
    }

    private class MemoryValue {

        public final String url;
        public final View convertView;
        public final HttpDataSource dataSource;
        public final Processor processor;
        public final HttpDataSource dataUrl;
        public final Processor processUrl;

        public MemoryValue(String url, View convertView, Context context){
            this.url = url;
            this.convertView = convertView;
            this.dataSource = new CachedHttpDataSource(context);
            this.processor = new BitmapProcessor();
            dataUrl = new HttpDataSource();
            processUrl = new ExtrasProcessor();
        }
    }

    private class ImagesLoader implements Runnable {

        private final MemoryValue mMemoryValue;
        private Handler mHandler = new Handler();
        private ImagesLoader(MemoryValue mMemoryValue){
            this.mMemoryValue = mMemoryValue;
        }

        @Override
        public void run() {
            try{
                InputStream dataUrl = mMemoryValue.dataUrl.getResult(mMemoryValue.url);
                Object procesUrl = mMemoryValue.processUrl.process(dataUrl);
                List<Category> data = (List<Category>)procesUrl;
                Category category = data.get(0);
//
//                String str = data.get(0).getUrlImage();
//                str = str.substring(str.indexOf("px")-2, str.indexOf("px")+2);
//                String url = data.get(0).getUrlImage().replaceAll(str,"100px");
//                InputStream dataSource = mMemoryValue.dataSource.getResult(url);
//                Object processingResult = mMemoryValue.processor.process(dataSource);
//                Bitmap bmp = (Bitmap) processingResult;
//                bmp = CircleMaskedBitmap.getCircleMaskedBitmapUsingShader(bmp, 100);
//                if (bmp != null) {
//                    memoryCache.put(mMemoryValue.url, bmp);
//                }
                if (imageViewReused(mMemoryValue)) {
                    return;
                }
                BitmapDisplayer bd = new BitmapDisplayer(category, mMemoryValue);
                mHandler.post(bd);
            }catch(Throwable th){
                return;
            }
        }
    }

    synchronized boolean imageViewReused(MemoryValue memoryValue){
        String tag = mViews.get(memoryValue.convertView);
        Log.i(TAG, "Check " + tag);
        if (tag.equals(null) || !tag.equals(memoryValue.url))
            return true;
        return false;
    }

    class BitmapDisplayer implements Runnable{

        private Category category;
        private MemoryValue memoryValue;

        public BitmapDisplayer(Category category, MemoryValue memoryValue){
            this.category = category;
            this.memoryValue = memoryValue;
        }

        public void run(){
            if (imageViewReused(memoryValue)) {
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
