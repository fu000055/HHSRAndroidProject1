package algonquin.cst2335.hhsrandroidproject1.MovieIfo;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import algonquin.cst2335.hhsrandroidproject1.R;

/**
 * This class can link the movie information of the network
 */

public class Movie_Database_API extends AppCompatActivity {
    private static String TAG = "Movie_Database_API";
    private Button  helpButton;
    private Button  favoritesButton;
    private ImageButton welBtn;
    private ImageButton advBtn;


    private  String stringURL;
    Bitmap image;
    String title;
    String year;
    String rating;
    String runtime;
    String actors;
    String plot;
    String poster;

    EditText movieNameText;
    TextView tv;
    Button movieNameBtn;

    EditText actorNameText;



    /**     *
     * Use to connect the activity_movie_database_api.XML
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_database_api);

        //welcome page show information
        welBtn = findViewById(R.id.welcomImageButton);
        welBtn.setOnClickListener((click)->{
            AlertDialog dialog = new AlertDialog.Builder(Movie_Database_API.this)
                    .setTitle("Getting Movie information").setMessage("Welcome to the movie App")
                    .setView(new ProgressBar(Movie_Database_API.this))
                    .show();

        });


        //Verify that the user is an adult
        advBtn = findViewById(R.id.adultVericicationImageButton);
        advBtn.setOnClickListener((m) ->{
            Toast.makeText(getApplicationContext(),
                    "You need to verify your identity",
                    Toast.LENGTH_LONG).show();
        });




        tv = findViewById(R.id.movieTextView);
        movieNameBtn = findViewById(R.id.movieIfoBtn);
        movieNameText = findViewById(R.id.inputMovieName);

        /**
         * Start movie  button to query movie information
         */
        movieNameBtn.setOnClickListener( click ->{

            AlertDialog dialog = new AlertDialog.Builder(Movie_Database_API.this)
                    .setTitle("Getting Movie  information")
                    .setMessage("Simon Movie App: " + movieNameText)
                    .setView(new ProgressBar(Movie_Database_API.this))
                    .show();

            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute( ( ) ->{

                URL url = null;

                //try catch

                try {

                    //connect to the server:
                    String stringURL = "http://www.omdbapi.com/?apikey=6c9862c2" + "&t="
                            + URLEncoder.encode(movieNameText.getText().toString(), "UTF-8");

                    //on other cpu:
                    url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());


                    //convert string to JSON object

                    //this converts to a String
                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    JSONObject movieDocument = new JSONObject( text );

                    title = movieDocument.getString("Title");
                    year = movieDocument.getString("Year");
                    rating = movieDocument.getString("Rated");
                    runtime = movieDocument.getString("Runtime");
                    actors = movieDocument.getString("Actors");
                    plot = movieDocument.getString("Plot");
                    poster = movieDocument.getString("Poster");

                    URL imageUrl = new URL(poster);
                    HttpURLConnection imageConnect = (HttpURLConnection) imageUrl.openConnection();
                    imageConnect.connect();
                    int responseCode = imageConnect.getResponseCode();
                    if(responseCode == 200){
                        image = BitmapFactory.decodeStream(imageConnect.getInputStream());

                    }

                    runOnUiThread(() -> {
                        TextView tv ;
                        tv = findViewById(R.id.title);
                        tv.setText("       *** Movie Information ***\n"+ "The movie title is : " + title);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.year);
                        tv.setText("The movie year is : " + year);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.rating);
                        tv.setText("The movie rating is : " + rating);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.runtime);
                        tv.setText("The movie runtime is : " + runtime);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.actors);
                        tv.setText("The movie main actors is : " + actors);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.plot);
                        tv.setText("The movie plot is : " + plot);
                        tv.setVisibility(View.VISIBLE);

                        ImageView iv = findViewById(R.id.poster);
                        iv.setImageBitmap(image);
                        iv.setVisibility(View.VISIBLE);

                    });

                }catch (IOException  | JSONException e){
                    e.printStackTrace();

                }

            });

        });

         //Login button can go to the next page
        Button loginBtn = findViewById(R.id.longinBtn);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String movieName = prefs.getString("MovieName", "");
        EditText et = findViewById(R.id.inputMovieName);
        et.setText(movieName);




        loginBtn.setOnClickListener((clk) -> {

            Intent nextPage = new Intent(Movie_Database_API.this, SavedMovies.class);
            startActivity(nextPage);

        });

        Button detailBtn = findViewById(R.id.detailBtn);
        detailBtn.setOnClickListener((clk) -> {


            Intent nextPage = new Intent(Movie_Database_API.this, MovieInfoRecord.class);
            startActivity(nextPage);

        });


        //help button information
        helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(clk ->{

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("How to use application movie_DATABASE_API")
                    .setMessage("1>Type movie name\n" +
                            "2>Click on movie to view movies\n" +
                            "3>Click on movie to search\n" +
                            "4>Click Save while in movie to save\n" +
                            "5>Click on My Movies to see saved\n" +
                            "6>Click Delete button inside Movie \n" +
                            "to delete it from saved Movies\n" +
                            "\n" +
                            "Application created by Simon Ao")
                    .setPositiveButton("OK", (click, arg) ->{
                    })
                    .create().show();


        });

        //favorites movies information button
        favoritesButton =findViewById(R.id.favoritesButton);
        favoritesButton.setOnClickListener(clk ->{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Favorite movie")
                    .setMessage( "Favorite movie Application created by Simon Ao")
                    .setPositiveButton("OK", (click, arg) ->{
                    })
                    .create().show();

        });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2)
        {
            if(resultCode == RESULT_OK)
            {
                Bitmap thumbnail = data.getParcelableExtra("data");
//                ImageView profileImage = findViewById(R.id.imageView);
//                profileImage.setImageBitmap(thumbnail);
                FileOutputStream fOut = null;
                try {
                    fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                }catch (FileNotFoundException e) {
                    e.printStackTrace();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }



    }




}
