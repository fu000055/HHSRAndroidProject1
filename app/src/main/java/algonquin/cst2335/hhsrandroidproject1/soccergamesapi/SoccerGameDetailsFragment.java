package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.hhsrandroidproject1.R;

public class SoccerGameDetailsFragment extends Fragment {
    static final String SAVE_TO_FAVOURITE = "SAVE TO FAVOURITE";
    static final String REMOVE_FROM_FAVOURITE = "REMOVE FROM FAVOURITE";
    ArticleInfo articleInfo;
    int chosenPosition;
    boolean isSaved = false;

    public SoccerGameDetailsFragment(ArticleInfo articleInfo, int position, boolean isSaved) {
        this.articleInfo = articleInfo;
        chosenPosition = position;
        this.isSaved = isSaved;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.details_layout, container, false);

        TextView titleView = detailsView.findViewById(R.id.titleView);
        TextView dateView = detailsView.findViewById(R.id.dateView);
        ImageView imageView = detailsView.findViewById(R.id.imageView);
        TextView urlView = detailsView.findViewById(R.id.urlView);
        TextView descriptionView = detailsView.findViewById(R.id.descriptionView);

        titleView.setText(articleInfo.title );
        dateView.setText((CharSequence) articleInfo.date);

        // Try to get bitmap from cache
        Bitmap bitmap = ImageCache.getBitmapFromMemCache(articleInfo.imgUrl);
        if (bitmap == null) {
            // If bitmap not in cache, request the image from url
            ImageQuery query = new ImageQuery(imageView, false);
            query.execute(articleInfo.imgUrl);
        } else {
            // If bitmap in cache, put it in the imageView
            imageView.setImageBitmap(bitmap);
        }

        urlView.setText(articleInfo.articleUrl);
        descriptionView.setText(articleInfo.description);

        Button closeButton = detailsView.findViewById(R.id.closebtn);
        closeButton.setOnClickListener( closeClicked -> {
            getParentFragmentManager().beginTransaction().remove( this ).commit();
        });

        Button saveButton = detailsView.findViewById(R.id.savebtn);
        if (this.isSaved) {
            saveButton.setText(REMOVE_FROM_FAVOURITE);
        } else {
            saveButton.setText(SAVE_TO_FAVOURITE);
        }

        saveButton.setOnClickListener(saveClicked -> {
            if (this.isSaved) {
                // If the article saved in favorite list, remove it from the list
                SoccerGameActivity parentActivity = (SoccerGameActivity) getContext();
                parentActivity.notifyNewsRemoved(articleInfo, chosenPosition);
                // Change the save state
                this.isSaved = false;
                // Update the test of the button
                saveButton.setText(SAVE_TO_FAVOURITE);
            } else {
                // If the article not in favorite list, save it from the list
                SoccerGameActivity parentActivity = (SoccerGameActivity) getContext();
                parentActivity.notifyNewsSaved(articleInfo, chosenPosition);
                // Change the save state
                this.isSaved = true;
                // Update the test of the button
                saveButton.setText(REMOVE_FROM_FAVOURITE);
            }

            getParentFragmentManager().beginTransaction().remove( this ).commit();
        });

        return detailsView;
    }
}
