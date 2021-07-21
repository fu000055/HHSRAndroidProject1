package algonquin.cst2335.hhsrandroidproject1.chargingStation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import algonquin.cst2335.hhsrandroidproject1.R;

public class ChargingStationListFragment extends Fragment {

    private EditText latitude;

    private EditText longitude;

    private Button search;

    private SharedPreferences sharedPref;

    ArrayList<ChargingStationPOJO> stationList = new ArrayList<>();

    SQLiteDatabase db;

    ChargingStationAdapter adt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View StationLayout = inflater.inflate(R.layout.charging_station_list, container, false);

        RecyclerView stations = StationLayout.findViewById(R.id.myrecycler);
        latitude = StationLayout.findViewById(R.id.latitudeInput);
        longitude = StationLayout.findViewById(R.id.longitudeInput);
        search = StationLayout.findViewById(R.id.searchButton);
        Button help = StationLayout.findViewById(R.id.help);

        adt = new ChargingStationAdapter(stationList);
        stations.setAdapter(adt);
        stations.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        sharedPref = getContext().getSharedPreferences("ChargingStation", Context.MODE_PRIVATE);
        latitude.setText(sharedPref.getString("Latitude", ""));
        longitude.setText(sharedPref.getString("Longitude",""));
        search.setOnClickListener(clk->{
           // longitude.onEditorAction(EditorInfo.IME_ACTION_DONE);
            //Intent nextPage = new Intent(this, ChargingStationDetails.class);
//            for(int i = 0; i<=10;i++){
//                ChargingStationPOJO pojo = new ChargingStationPOJO("a",2.22, 3.33,"123456");
//                pojo.setTitle("i" + i);
//                stationList.add(pojo);
//            }
            String url = "https://api.openchargemap.io/v3/poi/?key=aa49870e-45ee-4c7d-aa07-bad1b3e0e07a&output=json&countrycode=CA&latitude="
                    + latitude.getText().toString() + "&longitude=" + longitude.getText().toString() + "&maxresults=2";
            DownloadFilesTask downloadFileTask = new DownloadFilesTask();
            downloadFileTask.execute(url);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Latitude", latitude.getText().toString());
            editor.putString("Longitude", longitude.getText().toString());
            editor.commit();
            //startActivity( nextPage );

        });

        help.setOnClickListener(helpClk->{
            AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

            builder.setMessage("Directly input the latitude and longitude would be fine")
                    .setTitle("Instruction")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, cl) -> { })
                    .create().show();

        });

        return StationLayout;
    }



    public void notifyStationDeleted(ChargingStationPOJO chosenStation, int chosenPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setMessage("are you sure you want to delete this station: " + chosenStation.getTitle())
                .setTitle("Danger!")
                .setNegativeButton("Cancel", (dialog, cl) -> { })
                .setPositiveButton("Delete", (dialog, cl) -> {

                    //position = getAbsoluteAdapterPosition();

                    ChargingStationPOJO removedStation = stationList.get(chosenPosition);
                    stationList.remove(chosenPosition);
                    adt.notifyItemRemoved(chosenPosition);

                    db.delete(ChargingOpenHelper.TABLE_NAME, "_id=?", new String[] { Long.toString(removedStation.getId()) });


                    Snackbar.make(search, "You deleted message #" + chosenPosition, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk -> {

                                stationList.add(chosenPosition,  removedStation);
                                adt.notifyItemInserted(chosenPosition);
                                db.execSQL("Insert into " + ChargingOpenHelper.TABLE_NAME + " values('" + removedStation.getId() +
                                        "','" + removedStation.getTitle() +
                                        "','" + removedStation.getLatitude() +
                                        "','" + removedStation.getLongitude() +
                                        "','" + removedStation.getPhone() + "');");
                            })
                            .show();
                })
                .create().show();
    }

    /**
     * Class connects to the server, reads and process the data
     */
    private class DownloadFilesTask extends AsyncTask<String, String, String> {
        /**
         * dialog to show a progression of downloading to the user
         */
        private ProgressDialog p;
        //AlertDialog dialog;
        /**
         * Method shows a progression of downloading data from the server to the user
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            AlertDialog dialog = new AlertDialog.Builder(getContext())
//                    .setTitle("Getting Station")
//                    .setMessage("Please waiting")
//                    .setView(new ProgressBar(getContext()))
//                    .show();
            p = new ProgressDialog(getContext());
            p.setTitle("Getting Station");
            p.setMessage("Please wait...");
            p.setCancelable(false);
            p.show();
        }
        /**
         * Methods connects to the server, retrieves the data about car charging stations
         * @param urls link to the server
         * @return data about car charging stations
         */
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL link = new URL(urls[0]);
                urlConnection = (HttpURLConnection) link.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    result += (char) data;
                    data = reader.read();
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
//            try {

//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                InputStreamReader reader = new InputStreamReader(in);

//                int data = reader.read();
//                while (data != -1) {
//                    result += (char) data;
//                    data = reader.read();
//                }
            //    return result;
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;
        }
        /**
         * Method gets car charging stations and displays them on the screen as a list of items
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
                for(int i = 0; i < jsonArray.length(); i++){
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
           // ChargingStationAdapter adapter = new ChargingStationAdapter(getApplicationContext(), stationList, false);
           // theList.setAdapter(adapter);
        }

    }



}
