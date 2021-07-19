package algonquin.cst2335.hhsrandroidproject1.MovieIfo;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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


        EditText movieNameText;
        TextView tv;
        Button movieNameBtn;

        tv = findViewById(R.id.movieTextView);
        movieNameBtn = findViewById(R.id.movieIfoButton);
        movieNameText = findViewById(R.id.inputMovieName);

        /**
         * Start movie  button to query movie information
         */
        movieNameBtn.setOnClickListener( click ->{

            AlertDialog dialog = new AlertDialog.Builder(Movie_Database_API.this)
                    .setTitle("Getting Movie network information").setMessage("Simon perfecting information")
                    .setView(new ProgressBar(Movie_Database_API.this))
                    .show();

            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute( ( ) ->{

                URL url = null;

                //try catch

               /* try {

                    //connect to the server:
                    String stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                            + URLEncoder.encode(movieNameText.getText().toString(), "UTF-8")
                            + "&appid=7e943c97096a9784391a981c4d878b22&units=metric&mode=xml";

                    //on other cpu:
                    url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    //USE XML:
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    // ignore namespaces:
                    factory.setNamespaceAware(false);
                    //create the object
                    XmlPullParser xpp = factory.newPullParser();
                    //read from in, like Scanner
                    xpp.setInput( in  , "UTF-8");






                }catch (IOException | XmlPullParserException e){
                    e.printStackTrace();

                }
*/





            });


        });








        Button loginBtn = findViewById(R.id.longinBtn);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String movieName = prefs.getString("MovieName", "");
        EditText et = findViewById(R.id.inputMovieName);
        et.setText(movieName);


        /*String actorName = prefs.getString("ActorName","");
        EditText et2 = findViewById(R.id.editActorName);
        et2.setText(actorName);

        String memberNumber = prefs.getString("MemberNumber","");
        EditText mer = findViewById(R.id.editMemNumber);
        mer.setText(memberNumber);*/

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
