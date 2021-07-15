package algonquin.cst2335.hhsrandroidproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import algonquin.cst2335.hhsrandroidproject1.chargingStation.ChargingStation;
import algonquin.cst2335.hhsrandroidproject1.chargingStation.ChargingStationListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView chargingStation = findViewById(R.id.charging_station);
        chargingStation.setOnClickListener(clk ->{
                Intent goToCharingStation = new Intent(this, ChargingStation.class);
                startActivity(goToCharingStation);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}