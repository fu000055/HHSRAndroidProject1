package algonquin.cst2335.hhsrandroidproject1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import algonquin.cst2335.hhsrandroidproject1.soccergamesapi.SoccerGameActivity;

public class MainActivity extends AppCompatActivity {
    Toolbar main_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_menu = findViewById(R.id.main_menu);
        setSupportActionBar(main_menu);

        ImageView soccer_game = findViewById(R.id.soccer_icon);
        soccer_game.setOnClickListener( v -> {
            Intent soccerGame = new Intent(MainActivity.this, SoccerGameActivity.class);
            startActivity(soccerGame);
        });
    }

    public void startBusActivity() {
        Intent soccerGame = new Intent(this, SoccerGameActivity.class);
        startActivity(soccerGame);
    }

    public void startSoccerNewsActivity() {
        Intent soccerGame = new Intent(this, SoccerGameActivity.class);
        startActivity(soccerGame);
    }

//    public void startCarCharingActivity() {
//        Intent carCharging = new Intent(this, CarChargingStation.class);
//        startActivity(carCharging);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.soccer_games:
                startSoccerNewsActivity();
                break;
//            case R.id.electric_car_charging:
//                startCarChargingActivity();
//                break;
//
//            case R.id.octranspo:
//                startBusActivity();
//                break;
//
//            case R.id.movie:
//                startMovieActivity();
//                break;
        }


        return true;
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