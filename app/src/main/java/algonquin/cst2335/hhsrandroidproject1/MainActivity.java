package algonquin.cst2335.hhsrandroidproject1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import algonquin.cst2335.hhsrandroidproject1.chargingStation.ChargingStation;
import algonquin.cst2335.hhsrandroidproject1.chargingStation.ChargingStationListFragment;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import algonquin.cst2335.hhsrandroidproject1.MovieIfo.Movie_Database_API;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import algonquin.cst2335.hhsrandroidproject1.soccergamesapi.SoccerGameActivity;
import algonquin.cst2335.hhsrandroidproject1.oct.OCTranspoBusRouteActivity;


public class MainActivity extends AppCompatActivity {
   private static String TAG = "MainActivity";
   Toolbar main_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView chargingStation = findViewById(R.id.charging_station);
        chargingStation.setOnClickListener(clkStation -> {
            Intent goToCharingStation = new Intent(this, ChargingStation.class);
            startActivity(goToCharingStation);
        });

        //SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        ImageView otcImage = findViewById(R.id.bus_icon);
        otcImage.setOnClickListener(clk -> {

            Intent nextPageOCT = new Intent(MainActivity.this, OCTranspoBusRouteActivity.class);
            startActivity(nextPageOCT);


        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG,"In onStart() - The application is now visible on screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG,"In onResume() - The application is now responding to user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG,"In onPause() - The application no longer responds to user input");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG,"In onStop() - The application is no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG,"In onDestroy() - Any memory used by the application is freed");
    }
}