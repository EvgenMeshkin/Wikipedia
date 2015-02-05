package by.evgen.android.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

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

import by.evgen.android.apiclient.WikiApplication;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.os.assist.LIFOLinkedBlockingDeque;
import by.evgen.android.apiclient.processing.BitmapProcessor;
import by.evgen.android.apiclient.processing.ImageUrlProcessor;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.source.CachedHttpDataSource;
import by.evgen.android.apiclient.source.HttpDataSource;

/**
 * Created by evgen on 30.01.2015.
 */
public class AbstractLoader {

    private static final String TAG = "ImageLoader";
    private MemoryCache memoryCache = new MemoryCache();
    private Map<ImageView, String> mImageViews = new ConcurrentHashMap<ImageView, String>();
    private ExecutorService mExecutorService;
    public static final String KEY = "ImageLoader";
    private Context mContext;
    private AtomicBoolean isPause = new AtomicBoolean(false);
    final int stub_id = by.evgen.android.apiclient.R.drawable.stub;
    private Set<ImageView> mImagesViews = new HashSet<ImageView>();
    private final Object mLock = new Object();

    public AbstractLoader(Context context){
        mContext = context;
        mExecutorService = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS,
                new LIFOLinkedBlockingDeque<Runnable>());
    }

    public static AbstractLoader get(Context context) {
        return WikiApplication.get(context, KEY);
    }

    public void displayImage(final String url,final ImageView imageView){
        mImageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            Log.i(TAG, "FromTheCache");
            imageView.setImageBitmap(bitmap);
        }   else {
            Log.i(TAG, "Not FromTheCache");
            queueImage(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    public void pause() {
        isPause.set(true);
    }

    public void resume() {
        isPause.set(false);
        synchronized (mLock) {
            for (ImageView imageView : mImagesViews) {
                Object tag = imageView.getTag();
                if (tag != null) {
                    displayImage((String) tag, imageView);
                }
            }
            mImagesViews.clear();
        }
    }

    private void queueImage(String url, ImageView imageView){
        MemoryValue p = new MemoryValue(url, imageView, mContext);
        mExecutorService.submit(new ImagesLoader(p));
    }

    private class MemoryValue {

        public final String url;
        public final ImageView imageView;
        public final HttpDataSource dataSource;
        public final Processor processor;
        public final HttpDataSource dataUrl;
        public final Processor processUrl;

        public MemoryValue(String url, ImageView imageView, Context context){
            this.url = url;
            this.imageView = imageView;
            this.dataSource = new CachedHttpDataSource(context);
            this.processor = new BitmapProcessor();
            dataUrl = new HttpDataSource();
            processUrl = new ImageUrlProcessor();
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
                String str = data.get(0).getUrlImage();
                String pos = str.substring(str.indexOf("px")-2, str.indexOf("px")+2);
                String url = str.replaceAll(pos,"300px");
                InputStream dataSource = mMemoryValue.dataSource.getResult(url);
                Object processingResult = mMemoryValue.processor.process(dataSource);
                Bitmap bmp = (Bitmap) processingResult;
                bmp = CircleMaskedBitmap.getCircleMaskedBitmapUsingShader(bmp, 100);
                if (bmp != null) {
                    memoryCache.put(mMemoryValue.url, bmp);
                }
                if (imageViewReused(mMemoryValue)) {
                    return;
                }
                BitmapDisplayer bd = new BitmapDisplayer(bmp, mMemoryValue);
                mHandler.post(bd);
            }catch(Throwable th){
                return;
            }
        }
    }

    synchronized boolean imageViewReused(MemoryValue memoryValue){
        String tag = mImageViews.get(memoryValue.imageView);
        Log.i(TAG, "Check " + tag);
        if (tag.isEmpty() || !tag.equals(memoryValue.url))
            return true;
        return false;
    }

    class BitmapDisplayer implements Runnable{

        private Bitmap bitmap;
        private MemoryValue memoryValue;

        public BitmapDisplayer(Bitmap bitmap, MemoryValue memoryValue){
            this.bitmap = bitmap;
            this.memoryValue = memoryValue;
        }

        public void run(){
            if (imageViewReused(memoryValue)) {
                return;
            }
            memoryValue.imageView.setImageBitmap(bitmap);
        }
    }

}
