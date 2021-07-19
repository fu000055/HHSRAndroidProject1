package algonquin.cst2335.hhsrandroidproject1.chargingStation;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import algonquin.cst2335.hhsrandroidproject1.R;


public class ChargingStation extends AppCompatActivity {
    boolean isTablet = false;
    ChargingStationListFragment stationFragment;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);
        sharedPref = getSharedPreferences("ChargingStation", MODE_PRIVATE);
        isTablet = findViewById(R.id.detailsRoom) != null;

        stationFragment = new ChargingStationListFragment();
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.add(R.id.fragmentRoom, stationFragment);
        tx.commit();

    }

    public void userClickedStation(ChargingStationPOJO station, int position) {

        ChargingStationDetailsFragment detailsFragment = new ChargingStationDetailsFragment(station, position);
        //   ChargingStationFavouriteStation favouriteStation = new ChargingStationFavouriteStation();
    //    favouriteStation.setPositionClicked(position);
        if(isTablet)
        {
            //A tablet has a second Fragment with id detailsRoom to load a second fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsRoom, detailsFragment).commit();
        }else //on a phone
        {
            // The chat List is already loaded in the FrameLayout with id fragmentRoom, now load a second fragment over top of the list:
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, detailsFragment).commit();
        }
    }


    public void notifyStationDeleted(ChargingStationPOJO chosenStation, int chosenPosition) {
        stationFragment.notifyStationDeleted(chosenStation, chosenPosition);
    }
}
