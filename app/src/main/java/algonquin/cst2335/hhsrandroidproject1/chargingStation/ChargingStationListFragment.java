package algonquin.cst2335.hhsrandroidproject1.chargingStation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

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
import java.util.stream.Collectors;

import algonquin.cst2335.hhsrandroidproject1.MainActivity;
import algonquin.cst2335.hhsrandroidproject1.R;

/**
 * @author Hui Lyu
 * @version 1.0
 */
public class ChargingStationListFragment extends Fragment {
    /**
     * Field from the screen accepts latitude from a user
     */
    private EditText latitude;
    /**
     * Field from the screen accepts longitude from a user
     */
    private EditText longitude;
    /**
     * search button
     */
    private Button search;
    /**
     * shared preferences instance
     */
    private SharedPreferences sharedPref;
    /**
     * list of car charging stations
     */
    ArrayList<ChargingStationPOJO> stationList = new ArrayList<>();
    /**
     * SQLiteDatabase instance
     */
    SQLiteDatabase db;
    /**
     * ChargingStationAdapter instance
     */
    ChargingStationAdapter adt;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View StationLayout = inflater.inflate(R.layout.charging_station_list, container, false);

        RecyclerView stations = StationLayout.findViewById(R.id.myrecycler);
        latitude = StationLayout.findViewById(R.id.latitudeInput);
        longitude = StationLayout.findViewById(R.id.longitudeInput);
        search = StationLayout.findViewById(R.id.searchButton);

        adt = new ChargingStationAdapter(stationList);
        stations.setAdapter(adt);
        stations.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        sharedPref = getContext().getSharedPreferences("ChargingStation", Context.MODE_PRIVATE);
        latitude.setText(sharedPref.getString("Latitude", ""));
        longitude.setText(sharedPref.getString("Longitude", ""));
        search.setOnClickListener(clk -> {
            String url = "https://api.openchargemap.io/v3/poi/?key=aa49870e-45ee-4c7d-aa07-bad1b3e0e07a&output=json&countrycode=CA&latitude="
                    + latitude.getText().toString() + "&longitude=" + longitude.getText().toString() + "&maxresults=8";
            DownloadFilesTask downloadFileTask = new DownloadFilesTask();
            downloadFileTask.execute(url);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Latitude", latitude.getText().toString());
            editor.putString("Longitude", longitude.getText().toString());
            editor.commit();
            //startActivity( nextPage );

        });
        setHasOptionsMenu(true);
        Toolbar myToolbar = StationLayout.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        DrawerLayout drawer = StationLayout.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = StationLayout.findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener((item) -> {
            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });
        return StationLayout;
    }

    /**
     * This hook is called whenever an item in options menu is selected.
     *
     * @param item The menu item that was selected. This value cannot be null.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            case R.id.bus_home:
//
//                break;
            case R.id.car_charging_home:
                Intent chargingStation = new Intent(getContext(), ChargingStation.class);
                startActivity(chargingStation);
                break;
//            case R.id.movie_home:
//
//                break;
//            case R.id.soccer_home:

//                break;
            case R.id.help:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Directly input the latitude and longitude would be fine")
                        .setTitle("Instruction")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, cl) -> {
                        })
                        .create()
                        .show();
                break;
            case R.id.home:
                Intent home = new Intent(getContext(), MainActivity.class);
                startActivity(home);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater The MenuInflater object.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.charging_station_activity_actions, menu);
    }

    /**
     * Class connects to the server, reads and process the data
     */
    private class DownloadFilesTask extends AsyncTask<String, String, String> {
        /**
         * dialog to show a progression of downloading to the user
         */
        private ProgressDialog p;

        /**
         * Method shows a progression of downloading data from the server to the user
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(getContext());
            p.setTitle("Getting Station");
            p.setMessage("Please wait...");
            p.setCancelable(false);
            p.show();
        }

        /**
         * Methods connects to the server, retrieves the data about car charging stations
         *
         * @param urls link to the server
         * @return data about car charging stations
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        protected String doInBackground(String... urls) {

            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL link = new URL(urls[0]);
                urlConnection = (HttpURLConnection) link.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = (new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));

                return result;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;
        }

        /**
         * Method gets car charging stations and displays them on the screen as a list of items
         *
         * @param result data about car charging stations retrieved by doInBackground method()
         */
        protected void onPostExecute(String result) {
            p.dismiss();
            try {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject stationJSON = jsonArray.getJSONObject(i);
                    JSONObject addressJSON = stationJSON.getJSONObject("AddressInfo");

                    ChargingStationPOJO stationObject = new ChargingStationPOJO();
                    stationObject.setTitle(addressJSON.getString("Title"));
                    stationObject.setLatitude(addressJSON.getDouble("Latitude"));
                    stationObject.setLongitude(addressJSON.getDouble("Longitude"));
                    stationObject.setPhone(addressJSON.getString("ContactTelephone1"));
                    stationList.add(stationObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
