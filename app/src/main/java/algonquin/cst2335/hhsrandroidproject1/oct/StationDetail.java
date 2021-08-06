package algonquin.cst2335.hhsrandroidproject1.oct;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import algonquin.cst2335.hhsrandroidproject1.R;
/**
 * The StationDetail class use to load the bus station detail information that user searched.
 * @author Rong Fu
 * @version 1.0
 */
public class StationDetail extends AppCompatActivity {
    /** The station detail fragment*/
    StationFragment stationDetail;
    /** This marks that the device is a tablet*/
    boolean isTablet = false;

    Bundle bundle = new Bundle();

    /**
     * This function uses to call the activity first stats up.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_oct_layout);

        isTablet = findViewById(R.id.oct_secondary) !=null;

        stationDetail = new StationFragment();
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.add(R.id.oct_fragmentRoom, stationDetail);
        tx.commit();

    }

    /**
     * This function controls the different layout of the detail fragment when user operates the different device.
     * @param route The route passes through this station.
     * @param positon The position of the route item.
     */
    public void userClickedMessage(StationFragment.Route route, int positon){
        RouteFragment mdFragment = new RouteFragment(route, positon);
        bundle.putString("StationNumber", route.getStationNumber());
        bundle.putString("RouteNo", route.getRouteNo());
        mdFragment.setArguments(bundle);
        if(isTablet) {
            // A tablet has a second FrameLayout with id detailsRoom to load a second fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.oct_secondary, mdFragment).commit();
        }
        else//on a phone
        {
            // The chat list is already loaded in the FrameLayout with id fragmentRoom, now load a second fragment over top of the list
            getSupportFragmentManager().beginTransaction().add(R.id.oct_fragmentRoom, mdFragment).commit();
        }


    }
//    public void notifyMessageDeleted(MessageListFragment.ChatMessage chosenMessage, int chosenPosition) {
//        chatFragment.notifyMessageDeleted(chosenMessage,chosenPosition);
//    }
}

