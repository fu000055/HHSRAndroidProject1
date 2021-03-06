package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import algonquin.cst2335.hhsrandroidproject1.R;

public class MovieInfoRecord extends AppCompatActivity {
    boolean isTablet = false;
    MessageMovListFragment chatFragment;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loading layout
        setContentView(R.layout.movie_empty_layout);

        isTablet = findViewById(R.id.detailsRoom) != null;

        chatFragment = new MessageMovListFragment();
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.add(R.id.fragmentRoom,chatFragment);
        tx.commit();
    }


    public void userClickedMessage(MessageMovListFragment.ChatMessage chatMessage, int position) {

        MessageMovDetailsFragment mdFragment = new MessageMovDetailsFragment(chatMessage,position);

        if(isTablet){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsRoom,mdFragment).commit();

        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom,mdFragment).commit();
        }
    }

    public void notifyMessageDeleted(MessageMovListFragment.ChatMessage chosenMessage, int chosenPosition) {

        chatFragment.notifyMessageDeleted(chosenMessage,chosenPosition);

    }






}
