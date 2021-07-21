package algonquin.cst2335.hhsrandroidproject1.chargingStation;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.Locale;

import algonquin.cst2335.hhsrandroidproject1.R;

public class ChargingStationDetailsFragment extends Fragment {

    ChargingStationPOJO chosenStation;

    int chosenPosition;

    SQLiteDatabase db;

    public ChargingStationDetailsFragment(ChargingStationPOJO chosenStation, int chosenPosition) {
        this.chosenStation = chosenStation;
        this.chosenPosition = chosenPosition;
    }

    public ChargingStationDetailsFragment(int contentLayoutId, ChargingStationPOJO chosenStation, int chosenPosition) {
        super(contentLayoutId);
        this.chosenStation = chosenStation;
        this.chosenPosition = chosenPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View detailsView = inflater.inflate(R.layout.charging_station_details, container, false);

        TextView locationTitle = detailsView.findViewById(R.id.textTitle);
        TextView latitude = detailsView.findViewById(R.id.textLatitude);
        TextView longitude = detailsView.findViewById(R.id.textLongitude);
        TextView phone = detailsView.findViewById(R.id.phone);

        locationTitle.setText("Location Title is: " + chosenStation.getTitle());
        latitude.setText("Latitude is: " + chosenStation.getLatitude() );
        longitude.setText("Longitude is: " + chosenStation.getLongitude());
        phone.setText("Contact Number is:" + chosenStation.getPhone());

        ChargingOpenHelper opener = new ChargingOpenHelper(getContext());
        db = opener.getWritableDatabase();
        ImageView closeView = detailsView.findViewById(R.id.close);
        closeView.setOnClickListener( closeClicked -> {
            getParentFragmentManager().beginTransaction().remove( this ).commit();
        });

//        Button deleteButton = detailsView.findViewById(R.id.delete);
//        deleteButton.setOnClickListener(deleteClicked -> {
//            ChargingStation parentActivity = (ChargingStation )getContext();
//            parentActivity.notifyMessageDeleted(chosenStation, chosenPosition);
//
//            getParentFragmentManager().beginTransaction().remove( this ).commit();
//        });

        ImageView favourite = detailsView.findViewById(R.id.favourite);
        favourite.setOnClickListener(favouriteClicked -> {
            Context context = getContext();
            CharSequence text = "You have added "+ chosenStation.getTitle()+" to your favourite list"  ;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text,duration);
            toast.show();
            ChargingStationPOJO thisStation = new ChargingStationPOJO(chosenStation.getTitle(), chosenStation.getLatitude(), chosenStation.getLongitude(), chosenStation.getPhone());

            ContentValues newRow = new ContentValues();
            newRow.put(ChargingOpenHelper.COL_TITLE, thisStation.getTitle());
            newRow.put(ChargingOpenHelper.COL_LATITUDE, thisStation.getLatitude());
            newRow.put(ChargingOpenHelper.COL_LONGITUDE, thisStation.getLongitude());
            newRow.put(ChargingOpenHelper.COL_PHONE, thisStation.getPhone());
            long newId = db.insert(ChargingOpenHelper.TABLE_NAME, ChargingOpenHelper.COL_ID, newRow);
            thisStation.setId(newId);
        });

        Button seeFavouriteStationsBtn = detailsView.findViewById(R.id.favouriteStationBtn);
        seeFavouriteStationsBtn.setOnClickListener(clk -> {
            Intent intent = new Intent(getActivity(), ChargingStationFavouriteStation.class);
            startActivity(intent);
        });

        Button loadMap = detailsView.findViewById(R.id.loadMap);
        loadMap.setOnClickListener(loadMapClk -> {
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", chosenStation.getLatitude(), chosenStation.getLongitude());
            Uri intentUri = Uri.parse(uri);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        return detailsView;
    }
}
