package algonquin.cst2335.hhsrandroidproject1.chargingStation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import algonquin.cst2335.hhsrandroidproject1.R;

/**
 * @author Hui Lyu
 * @version 1.0
 */
public class ChargingStation extends AppCompatActivity {

    /**
     * a flag that the device is a tablet or a phone
     */
    boolean isTablet = false;
    /**
     * car charging station fragment
     */
    ChargingStationListFragment stationFragment;
    /**
     * shared preferences instance
     */
    private SharedPreferences sharedPref;

    /**
     * Method loads layout, reacts to user's action
     *
     * @param savedInstanceState reference to a Bundle object that is passed into the onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charging_station_empty_layout);
        sharedPref = getSharedPreferences("ChargingStation", MODE_PRIVATE);
        isTablet = findViewById(R.id.detailsRoom) != null;

        stationFragment = new ChargingStationListFragment();
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.add(R.id.fragmentRoom, stationFragment);
        tx.commit();

    }

    /**
     * method to go the fragment for showing the detail of the car charging information
     *
     * @param station  car charging station objects
     * @param position which station the user clicked
     */
    public void userClickedStation(ChargingStationPOJO station, int position) {

        ChargingStationDetailsFragment detailsFragment = new ChargingStationDetailsFragment(station, position);

        if (isTablet) {
            //A tablet has a second Fragment with id detailsRoom to load a second fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsRoom, detailsFragment).commit();
        } else //on a phone
        {
            // The chat List is already loaded in the FrameLayout with id fragmentRoom, now load a second fragment over top of the list:
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, detailsFragment).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
