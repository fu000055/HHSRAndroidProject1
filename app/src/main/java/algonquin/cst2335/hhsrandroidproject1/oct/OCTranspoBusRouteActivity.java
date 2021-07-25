package algonquin.cst2335.hhsrandroidproject1.oct;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import algonquin.cst2335.hhsrandroidproject1.MainActivity;
import algonquin.cst2335.hhsrandroidproject1.R;

public class OCTranspoBusRouteActivity extends AppCompatActivity {
    StationDetail myStationDetail = new StationDetail();
    ArrayList<String> favoriteList = new ArrayList<>();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oct_layout);


        Intent fromPreOCT = getIntent();
        // save message
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String searchStop = prefs.getString("StationNumber","");
        EditText userInputText = findViewById(R.id.search_edit_text);
        userInputText.setText(searchStop);

        Button btnSearch = findViewById(R.id.search_bar);
        Button btnHelp =  findViewById(R.id.help_bar);
        Button btnReturn = findViewById(R.id.returnButtonOCt);
        Button btnFavourites = findViewById(R.id.favouritesButton);




        btnSearch.setOnClickListener(clk1 ->{
            if(!userInputText.getText().toString().isEmpty()) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("StationNumber", userInputText.getText().toString());
                editor.apply();

                String newStationNo = userInputText.getText().toString().trim();
                OCTOpenHelper opener = new OCTOpenHelper(this);
                db = opener.getWritableDatabase();
                //The SQLiteDatabase class has a function called rawQuery, where you can write your query in SQL
                //The rawQuery function returns something a Cursor object
                Cursor results = db.rawQuery("Select * from " + OCTOpenHelper.TABLE_NAME + " Where " + OCTOpenHelper.col_stationNumber+ "='" + newStationNo + "';", null);
                boolean newStation = true;
                if (results.moveToNext()) {
                    newStation = false;
                }

                if(newStation) {
                    // contentValues like a insert
                    ContentValues newRow = new ContentValues();

                    newRow.put(OCTOpenHelper.col_stationNumber, newStationNo);
                    newRow.put(OCTOpenHelper.col_search_button, 1);
                    db.insertWithOnConflict(OCTOpenHelper.TABLE_NAME, null, newRow, SQLiteDatabase.CONFLICT_REPLACE);
                }

                Intent nextPageStation = new Intent(OCTranspoBusRouteActivity.this, StationDetail.class);
                nextPageStation.putExtra("StationNumber", userInputText.getText().toString());
                startActivity(nextPageStation);

                // progress bar
                AlertDialog dialog = new AlertDialog.Builder(OCTranspoBusRouteActivity.this)
                        .setTitle("Getting Bus Stop Information")
                        .setMessage("Loading " + userInputText.getText().toString() +"station information...")
                        .setView(new ProgressBar(OCTranspoBusRouteActivity.this))
                        .show();
            }
            else {// remind user input using Toast
                Toast.makeText(getApplicationContext(),
                        "You should enter a stop number",
                        Toast.LENGTH_LONG).show();
            }
        });

        btnReturn.setOnClickListener(returnclk ->{
            Intent previousPage = new Intent(OCTranspoBusRouteActivity.this, MainActivity.class);
            startActivity(previousPage);
        });

        btnFavourites.setOnClickListener(favouritesClk -> {
            Intent nextPageFavourites = new Intent(OCTranspoBusRouteActivity.this, Favourites.class);
            //nextPageFavourites.putExtra("StationNumber", userInputText.getText().toString());
            startActivity(nextPageFavourites);

        });

        btnHelp.setOnClickListener(clk3 ->{
            // AlertDialog for popup dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(OCTranspoBusRouteActivity.this);
            builder.setMessage("Enter the bus station code, then click 'Search button', you will find all the line information that the bus station contains. " +
                    "Click 'Return' button to return to previous page. Click on the list of histories. You can quickly enter the familiar station." )
                    .setTitle("Help")
                    .setPositiveButton("Cancel",(dialog, cl) ->{

                    }).create().show();
        });

    }


}