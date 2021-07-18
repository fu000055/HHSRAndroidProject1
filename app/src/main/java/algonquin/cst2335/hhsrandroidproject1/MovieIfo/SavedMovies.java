package algonquin.cst2335.hhsrandroidproject1.MovieIfo;



/**
 * This activity is used to view all saved movies
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.hhsrandroidproject1.R;


public class SavedMovies extends AppCompatActivity {

    /**
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_saved);
        Button loginBtn = findViewById(R.id.searchBtn);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String movieName = prefs.getString("LoginName", "");
        EditText et = findViewById(R.id.inputEmail);
        et.setText(movieName);
        loginBtn.setOnClickListener(clk -> {

            AlertDialog dialog = new AlertDialog.Builder(SavedMovies.this)
                    .setTitle("Get information about nearby movie theaters")
                    .setMessage("Display cinema location information and hotline")
                    .setView(new ProgressBar(SavedMovies.this))
                    .show();



            SharedPreferences.Editor  editor = prefs.edit();
            editor.putString("LoginName", et.getText().toString());
           // editor.putInt("Age", 35);
           // editor.apply();

            Intent nextPage = new Intent(SavedMovies.this,SaveSecondActivity.class);
            nextPage.putExtra("TheaterName", et.getText().toString());
            startActivity(nextPage);
        });


    }



}
