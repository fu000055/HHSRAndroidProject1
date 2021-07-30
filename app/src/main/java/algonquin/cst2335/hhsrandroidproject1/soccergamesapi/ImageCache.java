package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * This class is used to cache the image and thumbnail.
 * @author Minghui Liao
 * @version 1.0
 */
public class ImageCache {
    /**The available memory for this memory cache. */
    private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    /**Use 1/4th of the available memory for this memory cache. */
    private static final int cacheSize = maxMemory / 4;

    /**
     * This is used to cache for images.
     */
    private static LruCache<String, Bitmap> imgCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };

    /**
     * This is used to cache for thumbnails.
     */
    private static LruCache<String, Bitmap> thumbnailCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };

    /**
     * This method is used to add the bitmap image to memory cache.
     * @param key String object that is the image url as the key.
     * @param bitmap Bitmap object that is the bitmap image.
     */
    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            imgCache.put(key, bitmap);
        }
    }


    /**
     * This method is used to get the bitmap image from memory cache.
     * @param key String object that is the image url as the key.
     * @return return the image by using the key.
     */
    public static Bitmap getBitmapFromMemCache(String key) {
        return imgCache.get(key);
    }

    /**
     * This method is used to add the thumbnail image to the memory cache.
     * @param key String object that is the image url as the key.
     * @param bitmap return the image by using the key.
     */
    public static void addthumbnailToMemoryCache(String key, Bitmap bitmap) {
        if (getthumbnailFromMemCache(key) == null) {
            thumbnailCache.put(key, bitmap);
        }
    }

    /**
     * This method is used to get the thumbnail image from memory cache.
     * @param key String object that is the image url as the key.
     * @return return the image by using the key.
     */
    public static Bitmap getthumbnailFromMemCache(String key) {
        return thumbnailCache.get(key);
    }

}
