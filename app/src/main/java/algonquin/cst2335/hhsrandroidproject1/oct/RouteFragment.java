package algonquin.cst2335.hhsrandroidproject1.oct;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import algonquin.cst2335.hhsrandroidproject1.R;

/** This class displays route trip details.
 * @author Rong Fu
 * @version 1.0
 */
public class RouteFragment extends Fragment{
    private String stringURL;
    String routeDirection_routeLabel;
    String direction;
    String routeNumber;
    ArrayList<Trip> tripArrayList = new ArrayList<>();
    StationFragment.Route chosenRoute;
    int chosenPosition;

    /**
     * Constructor
     * @param route route
     * @param position position in the list
     */
    public RouteFragment(StationFragment.Route route, int position){
        chosenRoute = route;
        chosenPosition = position;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container  If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState  If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View routeLayout = inflater.inflate(R.layout.route_detail_layout,container,false);

        TextView titleText = routeLayout.findViewById(R.id.routeHeader);
        TextView stopDescriptionText = routeLayout.findViewById(R.id.routeDetailInfo);
        String stationNumber = getArguments().getString("StationNumber");//getActivity().getIntent().getStringExtra("StationNumber");//"3017";
        String routeNo = getArguments().getString("RouteNo");//getActivity().getIntent().getStringExtra("RouteNo");//"75";

        Button routeCloseBtn= routeLayout.findViewById(R.id.route_close_btn);
        routeCloseBtn.setOnClickListener(closeClk -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        Executor newThread = Executors.newSingleThreadExecutor();
        // create a stop list in the stop recyclerView list
        newThread.execute(() -> {//this runs on another thread
            try {
                stringURL = "https://api.octranspo1.com/v2.0/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="
                        + stationNumber + " &routeNo=" + routeNo + "&format=json";
                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String text = (new BufferedReader(//To read this data in Android, you have to convert the inputStream from the server into a Java String object
                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));
                //convert string to JSON object
                JSONObject theDocument = new JSONObject(text);
                JSONObject getNextTripsForStopResult = theDocument.getJSONObject("GetNextTripsForStopResult");
                String stopNo = getNextTripsForStopResult.getString("StopNo");
                String error = getNextTripsForStopResult.getString("Error");
                String stopLabel = getNextTripsForStopResult.getString("StopLabel");
                //titleText.setText("Information of the Route: " );
                //stopDescriptionText.setText(stopLabel +error);
                JSONObject routesObj = getNextTripsForStopResult.getJSONObject("Route");
                JSONArray routeDirectionArray = routesObj.getJSONArray("RouteDirection");
                for (int i = 0; i < routeDirectionArray.length(); i++) {
                    JSONObject o = routeDirectionArray.getJSONObject(i);
                    routeNumber = o.getString("RouteNo");
                    routeDirection_routeLabel = o.getString("RouteLabel");
                    direction = o.getString("Direction");
                    JSONObject trips = o.getJSONObject("Trips");
                    JSONArray tripArray = trips.getJSONArray("Trip");
                    for (int counter = 0; counter < tripArray.length(); counter++){
                        JSONObject object = tripArray.getJSONObject(counter);
                        Trip iterator = new Trip();
                        iterator.trip_longitude = object.getString("Longitude");
                        iterator.trip_latitude = object.getString("Latitude");
                        iterator.trip_gpsSpeed = object.getString("GPSSpeed");
                        iterator.trip_destination = object.getString("TripDestination");
                        iterator.trip_startTime = object.getString("TripStartTime");
                        iterator.trip_adjustedScheduleTime = object.getString("AdjustedScheduleTime");
                        iterator.Trip_adjustmentAge = object.getString("AdjustmentAge");
                        iterator.trip_lastTripOfSchedule = object.getBoolean("LastTripOfSchedule");
                        iterator.trip_busType = object.getString("BusType");
                        tripArrayList.add(iterator);
                    }
                }
                getActivity().runOnUiThread(() -> {
                    titleText.setText("Information of the Route: " + stopNo + stopLabel + error);
                    stopDescriptionText.setText(getTripsText(tripArrayList));
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch(JSONException joe) {
                getActivity().runOnUiThread(() -> {
                    titleText.setText("Can't retrieve information of trips for Route: " + routeNo + " Stop:"+ stationNumber);
                    stopDescriptionText.setText("");
                });
            } catch (IOException ioe) {
                Log.e("Connection error: ", ioe.getMessage());
            }
        });
        return routeLayout;
    }

    /**
     * Used to generate a long String including all trips' details
     * @param tripArrayList A list of all trips
     * @return A String to present all trips' info
     */
    public String getTripsText(ArrayList<Trip> tripArrayList) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        String dest = "";
        for (Trip it : tripArrayList) {
            if(!dest.equals(it.trip_destination)) {
                sb.append("\nDestination: ");
                sb.append(it.trip_destination);
                sb.append("\n");
                dest = it.trip_destination;
            }
            sb.append("  Trip");
            sb.append(++count);
            sb.append(":\n    Longitude:");
            sb.append(it.trip_longitude);
            sb.append("\n    latitude:");
            sb.append(it.trip_latitude);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Trip Class encapsulates details of one trip
     */
    private class Trip {
        String trip_longitude ;
        String trip_latitude ;
        String trip_gpsSpeed ;
        String trip_destination ;
        String trip_startTime ;
        String trip_adjustedScheduleTime ;
        String Trip_adjustmentAge ;
        boolean trip_lastTripOfSchedule ;
        String trip_busType;

    }
}
