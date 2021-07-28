package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.net.URL;

/**
 * This class is used to query the image from url by using AsyncTask.
 * @author Minghui Liao
 * @version 1.0
 */
public class ImageQuery extends AsyncTask<String, Void, Bitmap> {
    /**This passes the activity context*/
    Context context;
    /**This holds the image.*/
    ImageView imageView;
    /**This checks if the image is a thumbnail.*/
    boolean isThumbnail;
    /**This is the alter dialog for showing the messages*/
    AlertDialog dialog;

    /**
     * Creates an image query with specified characteristics.
     * @param context Context object that is the activity context
     * @param imageView An ImageView that holds the image.
     * @param isThumbnail It checks if the image is a thumbnail.
     */
    public ImageQuery(Context context, ImageView imageView, boolean isThumbnail) {
        this.context = context;
        this.imageView = imageView;
        this.isThumbnail = isThumbnail;
        this.dialog = new AlertDialog.Builder(context)
                .setMessage("We are downloading the articles")
                .setTitle("Downloading")
                .setView( new ProgressBar(context)) // Add the progress bar in the alter dialog.
                .show();
    }

    /**
     * This methods is to download the images on the background thread.
     * @param urls String object that is the image url.
     * @return return the downloaded bitmap image.
     */
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

    /**
     * This methods sets the image view on the main thread.
     * @param bitmap Bitmap object that is the bitmap image.
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        dialog.dismiss();
    }
}
