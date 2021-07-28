package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.hhsrandroidproject1.R;

/**
 * @author Minghui Liao
 * @version 1.0
 */
public class SoccerGameActivity extends AppCompatActivity {
    /**The Activity tag name.*/
    private static final String TAG = "SoccerGameActivity";
    /**This marks that if the device is a tablet*/
    boolean isTablet = false;
    /**The article list fragment*/
    SoccerGameListFragment soccerGameFragment;

//    String title[], description[], date[];
//    int images[] = {R.drawable.bus_icon,R.drawable.charging_station,R.drawable.movie_icon,R.drawable.soccer_icon };

    /**
     * Called when the activity first starts up.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soccer_game_empty_layout);

        isTablet = findViewById(R.id.detailsRoom) != null;

        soccerGameFragment = new SoccerGameListFragment();
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.add(R.id.fragmentRoom, soccerGameFragment);
        tx.commit();
        showRatingDialog();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_activity_actions, menu);
//        return true;
//    }

    /**
     * This method creates a dialog box for rating this application
     * and uses sharedPreferences to save the rating to show the next time you start the application
     */
    public void showRatingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Create layout inside AlertDialog
        LinearLayout linearLayout = new LinearLayout(this);
        final RatingBar ratingBar = new RatingBar(this);
        // Get Rating from SharedPreferences
        float rating = prefs.getFloat("Rating", 0.0f);

        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);
        ratingBar.setRating(rating);

        linearLayout.addView(ratingBar);

        builder.setTitle("Rating this app: ")
                .setView(linearLayout)
                .setNegativeButton("Cancel", (dialog, cl) -> {
                })
                .setPositiveButton("OK", (dialog, cl) -> {
                    // Set Rating into SharedPreferences
                    editor.putFloat("Rating", ratingBar.getRating());
                    editor.apply();
                    dialog.dismiss();
                })
                .create().show();

    }

    /**
     * This function will enable a phone to load a detail fragment over top of the SoccerGameListFragment
     * and enable a tablet can load the detail fragment in the FrameLayout
     * @param news Soccer news
     * @param position The positin of news
     * @param isSaved The status of a news that is saved or not
     */
    public void userClickedMessage(ArticleInfo news, int position, boolean isSaved) {

        SoccerGameDetailsFragment mdFragment = new SoccerGameDetailsFragment(news, position, isSaved);

        if (isTablet) {
            //A tablet has a second Fragment with id detailsRoom to load a second fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsRoom, mdFragment).commit();
        } else //on a phone
        {
            // The chat List is already loaded in the FrameLayout with id fragmentRoom, now load a second fragment over top of the list:
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, mdFragment).commit();
        }
    }

    /**
     * This function calls the notifyNewsSaved function from SoccerGameListFragment
     * @param chosenMessage The item you want to save
     * @param chosenPosition The item position
     */
    public void notifyNewsSaved(ArticleInfo chosenMessage, int chosenPosition) {
        soccerGameFragment.notifyNewsSaved(chosenMessage, chosenPosition);
    }

    /**
     * This function calls the notifyNewsRemoved function from SoccerGameListFragment
     * @param chosenMessage The item you want to remove
     * @param chosenPosition The item position
     */
    public void notifyNewsRemoved(ArticleInfo chosenMessage, int chosenPosition) {
        soccerGameFragment.notifyNewsRemoved(chosenMessage, chosenPosition);
    }

}
