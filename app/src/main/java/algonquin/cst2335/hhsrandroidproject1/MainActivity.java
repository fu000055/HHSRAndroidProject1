package algonquin.cst2335.hhsrandroidproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import algonquin.cst2335.hhsrandroidproject1.MovieIfo.Movie_Database_API;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView simon = findViewById(R.id.movie_icon);
        simon.setOnClickListener(V ->{
            Intent goToMovie = new Intent(MainActivity.this, Movie_Database_API.class);
            startActivity(goToMovie);
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