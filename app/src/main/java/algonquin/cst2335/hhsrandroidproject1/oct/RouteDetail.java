//package algonquin.cst2335.hhsrandroidproject1.oct;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//import java.util.stream.Collectors;
//
//import algonquin.cst2335.hhsrandroidproject1.R;
//
//public class RouteDetail extends AppCompatActivity{
//    private String stringURL;
//    String stopNo;
//    String stopLabel;
//    String error;
//    String routeDirection_routeNo;
//    String routeDirection_routeLabel;
//    String direction;
//    String requestProcessingTime;
//    String tripDetail;
//    String trip_longitude;
//    String trip_latitude;
//    String trip_gpsSpeed;
//    String trip_destination;
//    String trip_startTime;
//    String trip_adjustedScheduleTime;
//    String Trip_adjustmentAge;
//    boolean trip_lastTripOfSchedule;
//    String trip_busType;
//    String routeNumber;
//    ArrayList<Trip> tripArrayList = new ArrayList<>();
//    @Override
//    protected void onCreate( Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.route_detail_layout);
//
//        TextView titleText = findViewById(R.id.route_header);
//        TextView stopDescriptionText = findViewById(R.id.routeDetailInfo);
//        String stationNumber = "3017";
//        String routeNo = "75";
//
//        Executor newThread = Executors.newSingleThreadExecutor();
//        // create a stop list in the stop recyclerView list
//        newThread.execute(() -> {//this runs on another thread
//            try {
//                stringURL = "https://api.octranspo1.com/v2.0/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="
//                        + stationNumber + " &routeNo=" + routeNo + "&format=json";
//                URL url = null;
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                String text = (new BufferedReader(//To read this data in Android, you have to convert the inputStream from the server into a Java String object
//                        new InputStreamReader(in, StandardCharsets.UTF_8)))
//                        .lines()
//                        .collect(Collectors.joining("\n"));
//                //convert string to JSON object
//                JSONObject theDocument = new JSONObject(text);
//                JSONObject getNextTripsForStopResult = theDocument.getJSONObject("GetNextTripsForStopResult");
//                String stopNo = getNextTripsForStopResult.getString("StopNo");
//                String error = getNextTripsForStopResult.getString("Error");
//                String stopLabel = getNextTripsForStopResult.getString("StopLabel");
//                //titleText.setText("Information of the Route: " );
//                //stopDescriptionText.setText(stopLabel +error);
//                JSONObject routesObj = getNextTripsForStopResult.getJSONObject("Route");
//                JSONArray routeDirectionArray = routesObj.getJSONArray("RouteDirection");
//                for (int i = 0; i < routeDirectionArray.length(); i++) {
//                    JSONObject o = routeDirectionArray.getJSONObject(i);
//                    routeNumber = o.getString("RouteNo");
//                    routeDirection_routeLabel = o.getString("RouteLabel");
//                    direction = o.getString("Direction");
//                    JSONObject trips = routesObj.getJSONObject("Trips");
//                    JSONArray tripArray = trips.getJSONArray("RouteDirection");
//                    for (int counter = 0; counter < tripArray.length(); counter++){
//                        JSONObject object = tripArray.getJSONObject(i);
//                        Trip iterator = new Trip();
//                        iterator.trip_longitude = object.getString("Longitude");
//                        iterator.trip_latitude = object.getString("Latitude");
//                        iterator.trip_gpsSpeed = object.getString("GPSSpeed");
//                        iterator.trip_destination = object.getString("TripDestination");
//                        iterator.trip_startTime = object.getString("TripStartTime");
//                        iterator.trip_adjustedScheduleTime = object.getString("AdjustedScheduleTime");
//                        iterator.Trip_adjustmentAge = object.getString("AdjustmentAge");
//                        iterator.trip_lastTripOfSchedule = object.getBoolean("LastTripOfSchedule");
//                        iterator.trip_busType = object.getString("BusType");
//                        tripArrayList.add(iterator);
//                    }
//                }
//                runOnUiThread(() -> {
//                    titleText.setText("Information of the Route: " + stopNo + stopLabel + error);
//                    stopDescriptionText.setText();
////                     routes.add(new Route("RouteNo"));
//
//                });
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException | JSONException ioe) {
//                Log.e("Connection error: ", ioe.getMessage());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//       // return routeLayout;
//    }
//
//    public String getTripsText(ArrayList<Trip> tripArrayList) {
//        StringBuilder sb = new StringBuilder();
//        for (Trip it : tripArrayList) {
//            sb.append("Longitude:");
//            sb.append(it.trip_longitude);
//            sb.append("\n");
//            sb.append("latitude:");
//            sb.append(it.trip_latitude);
//        }
//        return
//    }
//    private class Trip {
//        String trip_longitude ;
//        String trip_latitude ;
//        String trip_gpsSpeed ;
//        String trip_destination ;
//        String trip_startTime ;
//        String trip_adjustedScheduleTime ;
//        String Trip_adjustmentAge ;
//        boolean trip_lastTripOfSchedule ;
//        String trip_busType;
//
//    }
//}