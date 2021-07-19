package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class ImageQuery extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    boolean isThumbnail;

    public ImageQuery(ImageView imageView, boolean isThumbnail) {
        this.imageView = imageView;
        this.isThumbnail = isThumbnail;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap bitmap = null;
        Bitmap thumbnail = null;
        try {
            URL url = new URL(urls[0]);
            // Read bitmap from url
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            // Resize bitmap to thumbnail
            thumbnail = Bitmap.createScaledBitmap(bitmap, 149, 101, true);
        } catch(IOException e) {
            System.out.println(e);
        } finally {
            if (thumbnail != null) {
                // Save bitmap and thumbnail into cache
                ImageCache.addBitmapToMemoryCache(urls[0], bitmap);
                ImageCache.addthumbnailToMemoryCache(urls[0], thumbnail);
            }
            if (this.isThumbnail) {
                return thumbnail;
            } else {
                return bitmap;
            }
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
