package algonquin.cst2335.hhsrandroidproject1.chargingStation;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import algonquin.cst2335.hhsrandroidproject1.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/**
 * @author Hui Lyu
 * @version 1.0
 */
public class ChargingStationFavouriteStation extends AppCompatActivity {

    /**
     * list with user's favourite car charging stations
     */
    ArrayList<ChargingStationPOJO> favStations = new ArrayList<ChargingStationPOJO>();

    /**
     * position of a station in a list which user clicks
     */
    private int positionClicked;
    /**
     * adapter for the RecyclerView
     */
    ChargingStationAdapter adapter;

    /**
     * car charging station objects
     */
    ChargingStationPOJO station;

    /**
     * Constructor to create ChargingStationFavouriteStations
     *
     * @param station         car charging station objects
     * @param positionClicked which row get clicked
     */
    public ChargingStationFavouriteStation(ChargingStationPOJO station, int positionClicked) {
        this.positionClicked = positionClicked;
        this.station = station;
    }

    /**
     * no-argument constructor for ChargingStationFavouriteStation
     */
    public ChargingStationFavouriteStation() {
    }

    /**
     * Method loads a list of favourite stations on the screen, allows to delete stations from the list
     *
     * @param savedInstanceState reference to a Bundle object that is passed into the onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites_station);
        RecyclerView stations = findViewById(R.id.favouriteRecycler);
        ChargingOpenHelper dbOpener = new ChargingOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        String[] columns = {ChargingOpenHelper.COL_ID, ChargingOpenHelper.COL_TITLE, ChargingOpenHelper.COL_LATITUDE,
                ChargingOpenHelper.COL_LONGITUDE, ChargingOpenHelper.COL_PHONE};
        Cursor results = db.query(false, ChargingOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        int titleColumnIndex = results.getColumnIndex(ChargingOpenHelper.COL_TITLE);
        int latitudeColIndex = results.getColumnIndex(ChargingOpenHelper.COL_LATITUDE);
        int idColIndex = results.getColumnIndex(ChargingOpenHelper.COL_ID);
        int longitudeColIndex = results.getColumnIndex(ChargingOpenHelper.COL_LONGITUDE);
        int phoneColIndex = results.getColumnIndex(ChargingOpenHelper.COL_PHONE);
        while (results.moveToNext()) {
            String title = results.getString(titleColumnIndex);
            double latitude = results.getDouble(latitudeColIndex);
            double longitude = results.getDouble(longitudeColIndex);
            long id = results.getLong(idColIndex);
            String phone = results.getString(phoneColIndex);
            favStations.add(new ChargingStationPOJO(id, title, latitude, longitude, phone));
            adapter = new ChargingStationAdapter(favStations, true);
            stations.setAdapter(adapter);
            stations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        Button delete = (Button) findViewById(R.id.deleteFavourite);

        delete.setOnClickListener(clk -> {
            ChargingStationPOJO stationToDelete = favStations.get(positionClicked);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.setTitle("Alert!")
                    .setMessage("Do you want to delete " + stationToDelete.getTitle() + " ?")
                    .setPositiveButton(delete.getText(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            favStations.remove(positionClicked);
                            //adapter.notifyDataSetChanged();
                            adapter.notifyItemRemoved(positionClicked);
                            int numDeleted = db.delete(ChargingOpenHelper.TABLE_NAME,
                                    ChargingOpenHelper.COL_ID + "=?", new String[]{Long.toString(stationToDelete.getId())});
                            Log.i("ViewContact", "Deleted " + numDeleted + " rows");
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", (d, w) -> {  /* nothing */})
                    .create();
            dialog.show();
        });
    }

    /**
     * get the position of the clicked charging station
     *
     * @return the position
     */
    public int getPositionClicked() {
        return positionClicked;
    }

    /**
     * methods get the user clicked charging station in the favorite list
     *
     * @param position get the position from the adapter
     */
    public void userClickedOneOfFavStation(int position) {
        positionClicked = position;
    }

}
