package by.evgen.android.apiclient.source;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import by.evgen.android.apiclient.WikiApplication;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 04.12.2014.
 */
public class CachedHttpDataSource extends HttpDataSource {

    public static final String KEY = "CachedHttpDataSource";
    private Map<String, File> mLruCache= Collections.synchronizedMap(new LinkedHashMap<String, File>());
    private Context mContext;
    private File mCacheFile;
    private long mSize = 0;

    public CachedHttpDataSource(Context context) {
        mContext = context;
    }

    public static CachedHttpDataSource get(Context context) {
        return WikiApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        mCacheFile = fileCache(p);
        return new FileInputStream(mCacheFile);
        }


    public File fileCache(String p) throws Exception  {
        File cacheDir = mContext.getCacheDir();
        File file = new File(cacheDir, "__cache");
        file.mkdirs();
        String[] list = file.list();
        Log.d(getClass(), "directory cache:  " + Arrays.toString(list));
        String path = file.getPath() + File.separator + generateFileName(p);
        mCacheFile = new File(path);
        if (mCacheFile.exists() && !mLruCache.containsKey(path)) {
            mLruCache.put(path, mCacheFile);
            mSize += mCacheFile.length();
            checkSize();
        }
        if (mLruCache.containsKey(path)) {
            Log.d(getClass(), "load from file");
            mCacheFile = mLruCache.get(path);
            return mCacheFile;
        } else {
            Log.d(getClass(), "Do not load load from file");
            mCacheFile = new File(path);
            InputStream inputStream = super.getResult(p);
            try {
                copy(inputStream, mCacheFile);
            } catch (Exception e) {
                mCacheFile.delete();
                throw e;
            }
            mLruCache.put(path, mCacheFile);
            mSize += mCacheFile.length();
            checkSize();
            Log.d(getClass(), "copy stream success get from file");
            return mCacheFile;
        }
    }

        private void checkSize() {
        Log.d(getClass(), "cache size = " + mSize + " length = " + mLruCache.size());
            long limit = 30000;
            if(mSize > limit){
            Iterator<Map.Entry<String, File>> iter = mLruCache.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry<String, File> entry = iter.next();
                mSize -= entry.getValue().length();
                entry.getValue().delete();
                Log.d(getClass(), "delete file:   " + entry.getKey());
                iter.remove();
                if (mSize <= limit)
                    break;
            }
            Log.d(getClass(), "Clean cache. New size " + mLruCache.size());
        }
    }


    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static long copy(InputStream input, File file) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            return copy(input, fileOutputStream);
        } finally {
            close(fileOutputStream);
        }
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    public static long copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private String generateFileName(String p) {
        return md5(p);
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Constant.EMPTY;
    }


}