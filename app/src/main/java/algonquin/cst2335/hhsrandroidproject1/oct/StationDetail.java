package algonquin.cst2335.hhsrandroidproject1.oct;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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

public class StationDetail extends AppCompatActivity {
    private String stringURL;
    RecyclerView stopListView;
    ArrayList<Route> routes = new ArrayList<>();
    StopAdapter adt = new StopAdapter();
    String routeNo;
    Toolbar mytoolbar;
    public void runFavourite() {
        Intent favoritesPage = new Intent(this, Favourites.class);
        startActivity(favoritesPage);
    }
    public void runSearch() {
        Intent searchPage = new Intent(this, OCTranspoBusRouteActivity.class);
        startActivity(searchPage);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.oct_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.id_favorites_oct:
                runFavourite();
                break;
            case R.id.id_search_oct:
                runSearch();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oct_station_detail_layout);
        mytoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ImageView star = findViewById(R.id.imageView);
        //Generate Open and Close strings
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,mytoolbar,R.string.oct_open,R.string.oct_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.popup_menu_oct);
        navigationView.setNavigationItemSelectedListener((item) -> {
            onOptionsItemSelected(item);//call the function for the other Toolbar
            drawer.closeDrawer(GravityCompat.START);
            return false;

        });

        Intent fromOct = getIntent();
        String stationNumber = fromOct.getStringExtra("StationNumber");
        TextView titleText = findViewById(R.id.header);
        TextView stopDescriptionText = findViewById(R.id.staticInfo);

        stopListView = findViewById(R.id.stationInformationRecycler);
        stopListView.setAdapter(adt);
        stopListView.setLayoutManager(new LinearLayoutManager(this));
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
                    routes.add(new Route(o.getString("RouteNo"),o.getString("RouteHeading"),o.getString("Direction"),o.getString("DirectionID")));
//                    stopListView.setVisibility(View.VISIBLE);
//                    adt.notifyDataSetChanged();
                }
                 runOnUiThread(() -> {
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

        Button returnBtn = findViewById(R.id.returnButtonStation);
        returnBtn.setOnClickListener(returnclk2 -> {
            Intent previousPage = new Intent(StationDetail.this, OCTranspoBusRouteActivity.class);
            startActivity(previousPage);
        });
    }
// loaded as a recycler view
    private class StopAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = getLayoutInflater();
//            View loadedRow = inflater.inflate(R.layout.station_detail_list_layout,parent,false);
//            return new RouteRowViews(loadedRow);
            return new RouteRowViews(
                    getLayoutInflater().inflate(R.layout.oct_station_detail_list_layout,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RouteRowViews thisRowLayout = (RouteRowViews) holder;
            thisRowLayout.routeNoText.setText(routes.get(position).getRouteNo()+"heading:"+routes.get(position).getRouteHeading()+routes.get(position).getDirection());// set the content loading from server
        }

        @Override
        public int getItemCount() {
            return routes.size();
        }
    }
    public class RouteRowViews extends RecyclerView.ViewHolder{
        // this should the widgets on a row, only have a TextView for message
        TextView routeNoText;
        int position = -1;
        public RouteRowViews( View itemView) {
            super(itemView);
            routeNoText = itemView.findViewById(R.id.stop_list);
            itemView.setOnClickListener(click -> {

            });
        }
      //  public void setPosition(int p) {position = p;}
    }

    private class Route{
        String routeNo;
        String routeHeading;
        String direction;
        String directionId;

        public Route(String routeNo, String routeHeading, String direction, String directionId) {
            this.routeNo = routeNo;
            this.routeHeading = routeHeading;
            this.direction = direction;
            this.directionId = directionId;
        }

        public String getRouteNo(){
            return routeNo;
        }
        public String getRouteHeading(){
            return routeHeading;
        }
        public String getDirection(){
            return direction;
        }
        public String getDirectionId(){
            return directionId;
        }

    }
}

/*
public class StationDetail extends AppCompatActivity {
    private String stringURL;
    RecyclerView stopListView;
    ArrayList<RoutesList> routes = new ArrayList<>();
    StopAdapter adt = new StopAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_detail_layout);

        Intent fromOct = getIntent();
        String stationNumber = fromOct.getStringExtra("StationNumber");
        TextView titleText = findViewById(R.id.header);
        TextView StopDescription = findViewById(R.id.staticInfo);
        stopListView = findViewById(R.id.stationInformationRecycler);
        stopListView.setAdapter(adt);
        stopListView.setLayoutManager(new LinearLayoutManager(this));
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
                String stopDescription = getRouteSummaryForStopResult.getString("StopDescription");
                titleText.setText("Information of the Station: ");
                StopDescription.setText(stopDescription +"(stop number" +stopNo+")");
                JSONObject routes = getRouteSummaryForStopResult.getJSONObject("Routes");
                JSONArray routeArray = routes.getJSONArray("Route");
                for (int i = 0; i < routeArray.length(); i++) {
                    JSONObject o = routeArray.getJSONObject(i);
                    o.getString("RouteNo");
                    o.getString("RouteHeading");
                    o.getString("Direction");
                    o.getInt("DirectionID");
                }
                stopListView.setVisibility(View.VISIBLE);

            } catch (IOException | JSONException ioe) {
                Log.e("Connection error:", ioe.getMessage());
            }
        });
        Button returnBtn = findViewById(R.id.returnButton);
        returnBtn.setOnClickListener(returnclk2 -> {
            Intent previousPage = new Intent(StationDetail.this, OCTranspoBusRouteActivity.class);
            startActivity(previousPage);
        });
    }
// loaded as a recycler view
    private class StopAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = getLayoutInflater();
//            View loadedRow = inflater.inflate(R.layout.station_detail_list_layout,parent,false);
//            return new RouteRowViews(loadedRow);
            return new RouteRowViews(
                    getLayoutInflater().inflate(R.layout.station_detail_list_layout,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RouteRowViews thisRowLayout = (RouteRowViews) holder;
            thisRowLayout.listText.setText(routes.get(position).getInfoURL(""));// set the content loading from server
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
    public class RouteRowViews extends RecyclerView.ViewHolder{
        // this should the widgets on a row, only have a TextView for message
        TextView listText;
        public RouteRowViews( View itemView) {
            super(itemView);
            listText = itemView.findViewById(R.id.stop_list);
            itemView.setOnClickListener(click -> {

            });
        }
    }

    private class RoutesList{
        String infoURL;

        public String getInfoURL(String infoURL){
            return infoURL;
        }

    }
}
* */




/*
public class StationDetail extends AppCompatActivity {
    private String stringURL;
    RecyclerView  stopListView;
    ArrayList<StopInfoMessage> stopInfo = new ArrayList<>();
    StopAdapter adt = new StopAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_detail_layout);

        Intent fromOct = getIntent();
        String stationNumber = fromOct.getStringExtra("StationNumber");
        TextView titleText = findViewById(R.id.header);
        titleText.setText("Information of the Station " + stationNumber);
        stopListView = findViewById(R.id.stationInformationRecycler);
        stopListView.setAdapter(adt);
        stopListView.setHasFixedSize(true);
        LinearLayoutManager mgrS = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        stopListView.setLayoutManager(mgrS);
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
                String stopDescription = getRouteSummaryForStopResult.getString("StopDescription");

                JSONObject routes = getRouteSummaryForStopResult.getJSONObject("Routes");
                JSONArray routeArray = routes.getJSONArray("Route");
                for(int i=0; i<routeArray.length();i++){
                    JSONObject o = routeArray.getJSONObject(i);
                    o.getString("RouteNo");
                    o.getString("RouteHeading");
                    o.getString("Direction");
                    o.getInt("DirectionID");
                }
                stopListView.setVisibility(View.VISIBLE);

            } catch (IOException | JSONException ioe) {
                Log.e("Connection error:", ioe.getMessage());
            }
        });
        Button returnBtn = findViewById(R.id.returnButton);
        returnBtn.setOnClickListener(returnclk2 -> {
            Intent previousPage = new Intent(StationDetail.this, OCTranspoBusRouteActivity.class);
            startActivity(previousPage);
        });


    }


    private class StopRowViews extends RecyclerView.ViewHolder{
        //The view that is passed in as a parameter represents the ConstraintLayout that is the root of the row.
        TextView stopList;
        int position = -1;
        public StopRowViews(View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk -> {
                    });
            stopList = itemView.findViewById(R.id.stop_list);
        }
        //  public  void setPosition(int p){position = p;}
    }
    private class StopAdapter extends RecyclerView.Adapter <StopRowViews> {

        @Override
        public StopRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
//                LayoutInflater inflater = getLayoutInflater();
//                View view = inflater.inflate(R.layout.station_detail_list_layout,parent,false);
//                return new StopRowViews(view);
            return new StopRowViews(getLayoutInflater().inflate(R.layout.station_detail_list_layout,parent,false));
        }

        @Override
        public void onBindViewHolder(StopRowViews holder, int position) {
            holder.stopList.setText(stopInfo.get(position).getImageUrl());
        }

        @Override
        public int getItemCount() {
            return stopInfo.size();
        }
    }


    private class StopInfoMessage {
        String imageUrl;
        long id;

        public void setId(long l) {
            id = l;
        }


        public StopInfoMessage(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public StopInfoMessage(String imageUrl, long id) {
            this.imageUrl = imageUrl;
            setId(id);
        }

        public String getImageUrl(){
            return imageUrl;
        }
    }
}
 */