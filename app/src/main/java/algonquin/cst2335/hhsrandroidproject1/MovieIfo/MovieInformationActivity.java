package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import algonquin.cst2335.hhsrandroidproject1.R;

public class MovieInformationActivity extends AppCompatActivity {
    /**
     * Variable for Movie listview adapter
     */
    public MovieListAdapter movieAdapter = new MovieListAdapter();
    /**
     * Initialize both Arraylist for movie and movies
     */
    public ArrayList<Movie> moviesArrayList = new ArrayList<>();
    public ArrayList<String> movieArrayList = new ArrayList<>();

    /**
     * Variable saves position of item clicked for later use
     */
    public int boat;

    /**
     * init all units inside layout
     */
    private Button search ;
    private Button helpButton;
    private Button savedAlbumsButtom;
    private EditText actorSearch;
    private ProgressBar loadingBar;
    public ListView moviesListView;
    public TextView tv;

    /**
     *Sets final names for the bundle to pass to fragment
     *
     */
    public Bundle dataToPassSimon;
    public static final String MOVIES_NAME = "MSNAME";
    public static final String MSID = "MSID";
    public static final String ACTOR_NAME = "ARNAME";
    public static final String YEAR = "YEAR";
    public static final String GENRE = "GENRE";
    public static final String DESCRIPTION = "DIS";
    public static final String MOD = "MOD";
    public static final String MOVIES = "MOVIES";

    /**
     * Initialize the shared preference variables
     */
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);
    }


    /**
     *This is an adapter to inflate the ListView with another row, it is used for
     * adding the searched movies as seperate entities among the listview
     */
    private class MovieListAdapter extends BaseAdapter {

        @Override // number of items in the list
        public int getCount() {
            return  moviesArrayList.size();
        }
        @Override // what object goes at row i
        public Object getItem(int i) {
            return moviesArrayList.get(i);
        }
        @Override //database id of item at row i
        public long getItemId(int i) {
            return i;
        }

        @Override //controls which widgets are on the row
        public View getView(int i, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.movielayout, parent, false);
            TextView textview = newView.findViewById(R.id.movieTitle);
            Movie thisAlbum = (Movie) getItem(i);
            textview.setText(thisAlbum.getMovieName());
            return newView;
        }
    }


}