package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * This fragment is used for viewing both the delete and save Movie views
 */
public class MovieFragment extends Fragment  {

    /**
     * Create class variables
     */
    private Bundle dataFromActivitySimon;
    private AppCompatActivity parentActivitySimon;
    private String MovieName;
    private String ActorName;
    private int year;
    private String genre;
    private String buttonText;
    private String dis;
    private Integer movieID;
    public SQLiteDatabase databaMoSave;
    private ArrayList<String>movieData;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}


