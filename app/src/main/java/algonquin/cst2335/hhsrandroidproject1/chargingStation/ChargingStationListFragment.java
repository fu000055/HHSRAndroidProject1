package algonquin.cst2335.hhsrandroidproject1.chargingStation;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

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
            for(int i = 0; i<=10;i++){
                ChargingStationPOJO pojo = new ChargingStationPOJO("a",2.22, 3.33,"123456");
                pojo.setTitle("i" + i);
                stationList.add(pojo);
            }

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
}
