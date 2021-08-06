package algonquin.cst2335.hhsrandroidproject1.oct;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import algonquin.cst2335.hhsrandroidproject1.MainActivity;
import algonquin.cst2335.hhsrandroidproject1.R;

/** This class displays all routes passing a specified station.
 * @author Rong Fu
 * @version 1.0
 */
public class StationFragment extends Fragment {
    private String stringURL;
    RecyclerView stopListView;
    ArrayList<Route> routes = new ArrayList<>();
    StopAdapter adt = new StopAdapter();
    String routeNo;
    //Toolbar mytoolbar;

    /**
     * Called to create a fragment
     * @param savedInstanceState the state from previous saved
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    /**
     * Initialize the contents of options menu for fragment
     * @param menu The options menu in which you place your items.
     * @param inflater The MenuInflater object
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.oct_station_page_menu, menu);
//        return onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in options menu is selected.
     * @param item The menu item that was selected. This value cannot be null.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
//            case R.id.car_charging_menu:
//                Intent carCharging = new Intent(getActivity(), ChargingStation.class);
//                startActivity(carCharging);
//                break;
//            case R.id.movie_menu:
//                Intent movie = new Intent(getActivity(), Movie_Database_API.class);
//                startActivity(movie);
//                break;
//            case R.id.id_soccer_games:
//                Intent soccerGame = new Intent(getActivity(), SoccerGameActivity.class);
//                startActivity(soccerGame);
//                break;
            case R.id.id_favorites_oct:
                Intent favoritesPage = new Intent(getActivity(), Favourites.class);
                startActivity(favoritesPage);;
                break;
            case R.id.id_search_oct:
                Intent searchPage = new Intent(getActivity(), OCTranspoBusRouteActivity.class);
                startActivity(searchPage);
                break;
            case R.id.id_main_from_oct:
                Intent homePage = new Intent(getActivity(), MainActivity.class);
                startActivity(homePage);
                break;
            case R.id.id_help_oct:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.oct_help_alert )
                        .setTitle(R.string.oct_help_alert_title)
                        .setPositiveButton(R.string.oct_help_alert_cancel,(dialog, cl) ->{

                        }).create().show();

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container  If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState  If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View stationLayout = inflater.inflate(R.layout.oct_station_detail_layout, container, false);

        Toolbar mytoolbar = stationLayout.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mytoolbar);

        DrawerLayout drawer = stationLayout.findViewById(R.id.station_drawer_layout);
        //ImageView star = stationLayout.findViewById(R.id.imageView);
        //Generate Open and Close strings
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),drawer,mytoolbar,R.string.oct_open,R.string.oct_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = stationLayout.findViewById(R.id.popup_menu_oct_s);
        navigationView.setNavigationItemSelectedListener((item) -> {
            onOptionsItemSelected(item);//call the function for the other Toolbar
            drawer.closeDrawer(GravityCompat.START);
            return false;

        });

        Intent fromOct = getActivity().getIntent();
        String stationNumber = fromOct.getStringExtra("StationNumber");
        TextView titleText = stationLayout.findViewById(R.id.header);
        TextView stopDescriptionText = stationLayout.findViewById(R.id.staticInfo);

        stopListView = stationLayout.findViewById(R.id.stationInformationRecycler);
        stopListView.setAdapter(adt);
        stopListView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mgrS.setStackFromEnd(true);
//        mgrS.setReverseLayout(true);
        Executor newThread = Executors.newSingleThreadExecutor();
        // create a stop list in the stop recyclerView list
        newThread.execute(() -> {//this runs on another thread
            try {
                stringURL = "https://api.octranspo1.com/v2.0/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="
                        + stationNumber + "&format=json";
                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String text = (new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));
                JSONObject theDocument = new JSONObject(text);
                JSONObject getRouteSummaryForStopResult = theDocument.getJSONObject("GetRouteSummaryForStopResult");
                String stopNo = getRouteSummaryForStopResult.getString("StopNo");
                String error = getRouteSummaryForStopResult.getString("Error");
                String description = getRouteSummaryForStopResult.getString("StopDescription");
                titleText.setText("Information of the Station: ");
                stopDescriptionText.setText(description +"(stop number" +stopNo+")"+error);
                JSONObject routesObj = getRouteSummaryForStopResult.getJSONObject("Routes");
                JSONArray routeArray = routesObj.getJSONArray("Route");
                for (int i = 0; i < routeArray.length(); i++) {
                    JSONObject o = routeArray.getJSONObject(i);
                    routeNo = o.getString("RouteNo");
                    //routes.add(new Route(o.getString("RouteNo")));
                    //adt.notifyItemInserted(i);
//                    o.getString("RouteNo");
//                    o.getString("RouteHeading");
//                    o.getString("Direction");
//                    o.getInt("DirectionID");
                    routes.add(new Route(stationNumber, o.getString("RouteNo"),o.getString("RouteHeading"),o.getString("Direction"),o.getString("DirectionID")));
//                    stopListView.setVisibility(View.VISIBLE);
//                    adt.notifyDataSetChanged();
                }
                getActivity().runOnUiThread(() -> {
                    titleText.setText("Information of the Station: ");
                    stopDescriptionText.setText(description +"(stop number" +stopNo+")");
//                     routes.add(new Route("RouteNo"));
                    stopListView.setVisibility(View.VISIBLE);
                    adt.notifyDataSetChanged();
                });

//                Intent nextPageStop = new Intent(StationDetail.this, RouteFragment.class);
//                nextPageStop.putExtra("StationNumber",stationNumber);
//                nextPageStop.putExtra("StopNumber",routeNo);
//                startActivity(nextPageStop);

            } catch (IOException | JSONException ioe) {
                Log.e("Connection error:", ioe.getMessage());
            }

        });

        Button returnBtn = stationLayout.findViewById(R.id.returnButtonStation);
        returnBtn.setOnClickListener(returnclk2 -> {
            Intent previousPage = new Intent(getContext(), OCTranspoBusRouteActivity.class);
            startActivity(previousPage);
        });
        return stationLayout;
    }

    /**
     * Provide a binding from an app-specific data set to views that are displayed
     */
    private class StopAdapter extends RecyclerView.Adapter{

        /**
         * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent
         * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         */
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = getLayoutInflater();
//            View loadedRow = inflater.inflate(R.layout.station_detail_list_layout,parent,false);
//            return new RouteRowViews(loadedRow);
            return new RouteRowViews(
                    getLayoutInflater().inflate(R.layout.oct_station_detail_list_layout,parent,false));
        }

        /**
         * Called by RecyclerView to display the data at the specified position
         * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RouteRowViews thisRowLayout = (RouteRowViews) holder;
            thisRowLayout.routeNoText.setText(routes.get(position).getRouteNo()+"heading:"+routes.get(position).getRouteHeading()+routes.get(position).getDirection());// set the content loading from server
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount() {
            return routes.size();
        }
    }

    /**
     * A class that inherits a ViewHolder, describes an item view and metadata about its place within the RecyclerView.
     */
    public class RouteRowViews extends RecyclerView.ViewHolder{
        // this should the widgets on a row, only have a TextView for message
        TextView routeNoText;
        int position = -1;
        public RouteRowViews( View itemView) {
            super(itemView);
            routeNoText = itemView.findViewById(R.id.stop_list);
            itemView.setOnClickListener(click -> {
                StationDetail parentActivity = (StationDetail)getContext();
//                Intent nextPageRoute = new Intent(getContext(), RouteFragment.class);
//                Intent fromOct = getActivity().getIntent();
              //  String stationNumber = fromOct.getStringExtra("StationNumber");
              //  nextPageRoute.putExtra("StationNumber", stationNumber);
                position = getAbsoluteAdapterPosition();
                //nextPageRoute.putExtra("RouteNo", routes.get(position).routeNo);
                //startActivity(nextPageRoute);
                parentActivity.userClickedMessage(routes.get(position),position);
            });

        }
        //  public void setPosition(int p) {position = p;}
    }

    /**
     * A class that encapsulates route detail information
     */
    class Route{
        private String stationNumber;
        private String routeNo;
        private String routeHeading;
        private String direction;
        private String directionId;

        /**
         * Constructor, stores detail information of a route
         * @param stationNumber Station number
         * @param routeNo   Route number
         * @param routeHeading  Route heading
         * @param direction Route direction
         * @param directionId   Route direction id
         */
        public Route(String stationNumber, String routeNo, String routeHeading, String direction, String directionId) {
            this.stationNumber = stationNumber;
            this.routeNo = routeNo;
            this.routeHeading = routeHeading;
            this.direction = direction;
            this.directionId = directionId;
        }

        /**
         * Standard getter
         * @return Station number
         */
        public String getStationNumber(){
            return stationNumber;
        }

        /**
         * Standard getter
         * @return Route number
         */
        public String getRouteNo(){
            return routeNo;
        }

        /**
         * Standard getter
         * @return Route heading
         */
        public String getRouteHeading(){
            return routeHeading;
        }

        /**
         *  Standard getter
         * @return Route direction
         */
        public String getDirection(){
            return direction;
        }

        /**
         *  Standard getter
         * @return Route direction Id
         */
        public String getDirectionId(){
            return directionId;
        }

    }
}
