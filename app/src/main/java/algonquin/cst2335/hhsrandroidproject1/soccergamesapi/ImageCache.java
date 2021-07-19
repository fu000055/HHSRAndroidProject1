package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCache {
    static private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    static private final int cacheSize = maxMemory / 4;
    static private LruCache<String, Bitmap> imgCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };

    static private LruCache<String, Bitmap> thumbnailCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };

    static public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            imgCache.put(key, bitmap);
        }
    }

    static public Bitmap getBitmapFromMemCache(String key) {
        return imgCache.get(key);
    }

    static public void addthumbnailToMemoryCache(String key, Bitmap bitmap) {
        if (getthumbnailFromMemCache(key) == null) {
            thumbnailCache.put(key, bitmap);
        }
    }

    static public Bitmap getthumbnailFromMemCache(String key) {
        return thumbnailCache.get(key);
    }

}
