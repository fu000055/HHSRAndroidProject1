package algonquin.cst2335.hhsrandroidproject1.oct;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import algonquin.cst2335.hhsrandroidproject1.MainActivity;
import algonquin.cst2335.hhsrandroidproject1.R;

public class StationDetail extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_layout);

        Intent fromOct = getIntent();
        String stationNumber = fromOct.getStringExtra("StationNumber");
        TextView titleText = findViewById(R.id.header);
        titleText.setText("Information of the Station " + stationNumber);


        TextView stationDetailInfo = findViewById(R.id.stationInformation);

        Button returnBtn = findViewById(R.id.returnButton);
        returnBtn.setOnClickListener(returnclk2 ->{
            Intent previousPage = new Intent(StationDetail.this, OCTranspoBusRouteActivity.class);
            startActivity(previousPage);
        });


    }
}
//
//    @Override
//    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
//

//
////        Intent fromPrevious = getIntent();
////        String routeNumber = fromPrevious.getStringExtra("RouteNumber");
////
////        titleText.setText("The stop you are looking for is:" + routeNumber);
//        returnBtn.setOnClickListener(returnClick -> {
//            getParentFragmentManager().beginTransaction().remove(this).commit();
//        });
//
//
//        return stationView;
//    }