package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.hhsrandroidproject1.R;

/**
 * @author Minghui Liao
 * @version 1.0
 */
public class SoccerGameDetailsFragment extends Fragment {
    /**String object that is set the button text as "SAVE TO FAVOURITE"*/
    static String SAVE_TO_FAVOURITE = "SAVE TO FAVOURITE";
    /**String object that is set the button text as "REMOVE TO FAVOURITE"*/
    static String REMOVE_FROM_FAVOURITE = "REMOVE FROM FAVOURITE";
    /**The article information*/
    ArticleInfo articleInfo;
    /**The chosen position of the article.*/
    int chosenPosition;
    /**This marks if the article is saved.*/
    boolean isSaved = false;

    /**
     * This creates the detail fragment of the api.
     * @param articleInfo The article.
     * @param position The position of the article that was chosen by the user.
     * @param isSaved This marks if the article is saved.
     */
    public SoccerGameDetailsFragment(ArticleInfo articleInfo, int position, boolean isSaved) {
        this.articleInfo = articleInfo;
        chosenPosition = position;
        this.isSaved = isSaved;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container  If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.soccer_game_details_layout, container, false);

        SAVE_TO_FAVOURITE = (String) getString(R.string.soccer_news_save_to_favourite);
        REMOVE_FROM_FAVOURITE = (String) getString(R.string.soccer_news_remove_from_favourite);

        TextView titleView = detailsView.findViewById(R.id.titleView);
        TextView dateView = detailsView.findViewById(R.id.dateView);
        ImageView imageView = detailsView.findViewById(R.id.imageView);
        TextView urlView = detailsView.findViewById(R.id.urlView);
        TextView descriptionView = detailsView.findViewById(R.id.descriptionView);
//        WebView webView = detailsView.findViewById(R.id.webview);

        titleView.setText(articleInfo.title );
        dateView.setText((CharSequence) articleInfo.date);
        urlView.setText(articleInfo.articleUrl);
        descriptionView.setText(articleInfo.description);
        Button loadBtn = (Button)detailsView.findViewById(R.id.loadbtn);
        loadBtn.setOnClickListener(click -> {
//            Intent intent = new Intent(getContext(), SoccerGameWebActivity.class);
//            intent.putExtra("url", articleInfo.articleUrl);
//            startActivity(intent);
            WebView webView = (WebView)detailsView.findViewById(R.id.webview);
            webView.loadUrl(articleInfo.articleUrl);
        });

        // Try to get bitmap from cache
        Bitmap bitmap = ImageCache.getBitmapFromMemCache(articleInfo.imgUrl);
        if (bitmap == null) {
            // If bitmap not in cache, request the image from url
            ImageQuery query = new ImageQuery(getContext(), imageView, false);
            query.execute(articleInfo.imgUrl);
        } else {
            // If bitmap in cache, put it in the imageView
            imageView.setImageBitmap(bitmap);
        }


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
