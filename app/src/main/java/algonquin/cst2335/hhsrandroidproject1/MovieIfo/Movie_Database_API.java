package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import algonquin.cst2335.hhsrandroidproject1.MainActivity;
import algonquin.cst2335.hhsrandroidproject1.R;

/**
 * @Author Simon Ao
 * @version 2021-07-16
 *
 *
 * This class can link the movie information of the network
 */

public class Movie_Database_API extends AppCompatActivity {
    RecyclerView chatList;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    MyChatAdapter adt =new MyChatAdapter();




    private static String TAG = "Movie_Database_API";
    private Button  helpButton;
    private Button  newMoviesButton;
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

    TextView titleView,yearView,ratingView,runtimeView,actorsView,plotView;
    ImageView posterView;

    float oldSize=14;

    /**
     *  Get an object called a MenuInflater,initialize the toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.movie_api_activity_actions,menu);
        return true;
    }

    //Main page port
    public void startAppHomeActivity(){
        Intent appHome  = new Intent(this, MainActivity.class);
        startActivity(appHome);
    }

    public void favoriteMovieActivity(){
        Intent favoriteMovie = new Intent(this,MovieInfoRecord.class);
        startActivity(favoriteMovie);
    }

    /**
     * Clear movie information,font size smaller,font size bigger
     * Use a switch statement and check for different ids in the menu file
     */

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
          switch (item.getItemId()){
              case R.id.hide_view:
                  titleView.setVisibility(View.INVISIBLE);
                  yearView.setVisibility(View.INVISIBLE);
                  ratingView.setVisibility(View.INVISIBLE);
                  runtimeView.setVisibility(View.INVISIBLE);
                  actorsView.setVisibility(View.INVISIBLE);
                  plotView.setVisibility(View.INVISIBLE);
                  posterView.setVisibility(View.INVISIBLE);
                  movieNameText.setText("");
                  break;



              case R.id.id_favorite:

                  favoriteMovieActivity();

                 /* oldSize++;
                  titleView.setTextSize(oldSize);
                  yearView.setTextSize(oldSize);
                  ratingView.setTextSize(oldSize);
                  runtimeView.setTextSize(oldSize);
                  actorsView.setTextSize(oldSize);
                  plotView.setTextSize(oldSize);*/
                  break;

              case R.id.id_home:
                  startAppHomeActivity();


                  /*oldSize = Float.max(oldSize-1,5);
                  titleView.setTextSize(oldSize);
                  yearView.setTextSize(oldSize);
                  ratingView.setTextSize(oldSize);
                  runtimeView.setTextSize(oldSize);
                  actorsView.setTextSize(oldSize);
                  plotView.setTextSize(oldSize);*/
                  break;

              case 5:
                  String movieName = item.getTitle().toString();
                  runMoviecast(movieName);
                  break;

              case R.id.id_movie_help:
                  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                  alertDialogBuilder.setTitle("How to use application movie_DATABASE_API")
                          .setMessage("1>Type movie name\n" +
                                  "2>Click on movie to view movies\n" +
                                  "3>Click on movie to search\n" +
                                  "4>Click Add while in movie to save\n" +
                                  "5>Click on My Movies to see saved\n" +
                                  "6>Click movie name delete\n" +
                                  "to delete it from saved Movies\n" +
                                  "\n" +
                                  "Application created by Simon Ao")
                          .setPositiveButton("OK", (click, arg) ->{
                          })
                          .create().show();
                  break;



          }

        return super.onOptionsItemSelected(item);
    }


    //Convert Movie cast to a separate function called runMoviecast(String movieName)
    public void runMoviecast(String movieName){
        AlertDialog dialog = new AlertDialog.Builder(Movie_Database_API.this)
                .setTitle("Getting Movie  information")
                .setMessage("Simon Movie App: " + movieName)
                .setView(new ProgressBar(Movie_Database_API.this))
                .show();
        //still on GUI thread, can't connect to server here
        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute( ( ) ->{

            URL url = null;

            //try catch

            try {

                //connect to the server:
                String stringURL = "http://www.omdbapi.com/?apikey=6c9862c2" + "&t="
                        + URLEncoder.encode(movieName, "UTF-8");

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

                    titleView = findViewById(R.id.title);
                    titleView.setText("       *** Movie Information ***\n"+ "The movie title is : " + title);
                    titleView.setVisibility(View.VISIBLE);

                    yearView = findViewById(R.id.year);
                    yearView.setText("The movie year is : " + year);
                    yearView.setVisibility(View.VISIBLE);

                    ratingView = findViewById(R.id.rating);
                    ratingView.setText("The movie rating is : " + rating);
                    ratingView.setVisibility(View.VISIBLE);

                    runtimeView = findViewById(R.id.runtime);
                    runtimeView.setText("The movie runtime is : " + runtime);
                    runtimeView.setVisibility(View.VISIBLE);

                    actorsView = findViewById(R.id.actors);
                    actorsView.setText("The movie main actors is : " + actors);
                    actorsView.setVisibility(View.VISIBLE);

                    plotView = findViewById(R.id.plot);
                    plotView.setText("The movie plot is : " + plot);
                    plotView.setVisibility(View.VISIBLE);

                    posterView = findViewById(R.id.poster);
                    posterView.setImageBitmap(image);
                    posterView.setVisibility(View.VISIBLE);


                });

            }catch (IOException  | JSONException e){
                e.printStackTrace();

            }

        });





    }




    /**     *
     * Use to connect the activity_movie_database_api.xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_database_api);

        chatList = findViewById(R.id.myrecycler);
        chatList.setAdapter(adt);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        //Button addMovieBtn = findViewById(R.id.addMoviebutton);




        /*addMovieBtn.setOnClickListener(clk ->{
            ChatMessage thisMessage = new ChatMessage( messageTyped.getText().toString(),1);
            messages.add( thisMessage );
            messageTyped.setText("");
            adt.notifyItemInserted(messages.size()-1);
        });*/







       // tv = findViewById(R.id.movieTextView);
        movieNameBtn = findViewById(R.id.movieIfoBtn);
        movieNameText = findViewById(R.id.inputMovieName);

        //add code in onCreate() to find the toolbar

        Toolbar myToolbar = findViewById(R.id.toolbar);
        // load the toolbar call setSupportActionBar( ) and pass the toolbar as a parameter
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener((item) ->{
            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;










        });


        //welcome page show information
     /*   welBtn = findViewById(R.id.welcomImageButton);
        welBtn.setOnClickListener((click)->{
            AlertDialog dialog = new AlertDialog.Builder(Movie_Database_API.this)
                    .setTitle("Getting Movie information").setMessage("Welcome to the movie App")
                    .setView(new ProgressBar(Movie_Database_API.this))
                    .show();

        });*/


        //Verify that the user is an adult
        advBtn = findViewById(R.id.adultVericicationImageButton);
        advBtn.setOnClickListener((m) ->{
            Toast.makeText(getApplicationContext(),
                    "You need to verify your identity",
                    Toast.LENGTH_LONG).show();
        });



        /**
         * Start movie  button to query movie information
         */
        movieNameBtn.setOnClickListener( click ->{

            String movieName = movieNameText.getText().toString();
            myToolbar.getMenu().add( 0, 5, 0, movieName).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

            runMoviecast(movieName);


        });




         //Login button can go to the next page
        Button loginBtn = findViewById(R.id.loginBtn);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        //String moviesName = prefs.getString("MovieName", "");
       // EditText et = findViewById(R.id.inputMovieName);
       // et.setText(movieName);

        loginBtn.setOnClickListener((clk) -> {

            Intent nextPage = new Intent(Movie_Database_API.this, SavedMovies.class);
            startActivity(nextPage);

        });

      /*  Button detailBtn = findViewById(R.id.detailBtn);
        detailBtn.setOnClickListener((clk) -> {
            Intent nextPage = new Intent(Movie_Database_API.this, MovieInfoRecord.class);
            startActivity(nextPage);

        });*/


        //help button information
    /*    helpButton = findViewById(R.id.helpButton);
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


        });*/


        //favorites movies information button
        newMoviesButton =findViewById(R.id.addMovieButton);
        newMoviesButton.setOnClickListener(clk ->{
            ChatMessage titleMessage = new ChatMessage( titleView.getText().toString(),1);
            messages.add( titleMessage );
            titleView.setText("");

            ChatMessage yearMessage = new ChatMessage( yearView.getText().toString(),1);
            messages.add( yearMessage );
            yearView.setText("");

            ChatMessage ratingMessage = new ChatMessage( ratingView.getText().toString(),1);
            messages.add( ratingMessage );
            ratingView.setText("");

            ChatMessage runtimeMessage = new ChatMessage( runtimeView.getText().toString(),1);
            messages.add( runtimeMessage );
            runtimeView.setText("");

            ChatMessage actorMessage = new ChatMessage( actorsView.getText().toString(),1);
            messages.add( actorMessage );
            actorsView.setText("");

            ChatMessage plotMessage = new ChatMessage( plotView.getText().toString(),1);
            messages.add( plotMessage );
            plotView.setText("");








            adt.notifyItemInserted(messages.size()-1);

        });




       /*
        //favorites movies information button
        newMoviesButton =findViewById(R.id.addMovieButton);
        newMoviesButton.setOnClickListener(clk ->{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Favorite movie")
                    .setMessage( "Favorite movie Application created by Simon Ao")
                    .setPositiveButton("OK", (click, arg) ->{
                    })
                    .create().show();

        });*/

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





    private class MyRowViews extends RecyclerView.ViewHolder{
        // this should the widgets on a row, only have a TextView for message
        TextView messageText;
        int position = -1;

        public MyRowViews(View itemView) {// itemView is a constraintLayout, that has <TextView> as sub-item>
            super(itemView);
            itemView.setOnClickListener(clk -> {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Movie_Database_API.this);
                builder.setMessage("Do you want to delete the movie: " + messageText.getText())
                        .setTitle("Question")
                        .setNegativeButton("No",(dialog, cl) ->{ })
                        .setPositiveButton("Yes",(dialog, cl) ->{
                            // position = getAbsoluteAdapterPosition();
                            ChatMessage removedMessage = messages.get(position);
                            messages.remove(position);
                            adt.notifyItemRemoved(position);
                            Snackbar.make(messageText, "You deleted movie #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk2 -> {
                                        messages.add(position, removedMessage);
                                        adt.notifyItemInserted(position);
                                    })
                                    .show();

                        })
                        .create().show();
            });
            messageText = itemView.findViewById(R.id.movie_add_favorite);
        }
        public  void setPosition(int p){position = p;}
    }
    private class MyChatAdapter extends RecyclerView.Adapter{
        @Override
        public int getItemViewType(int position) {
            return messages.get(position).sendOrReceive;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();// LayoutInflater is for loading XML layouts
            int layoutID;
            layoutID = R.layout.movie_add_favorite;
            View loadedRow = inflater.inflate(layoutID, parent, false);
            return new MyRowViews(loadedRow);// will initialize the TextView

        }
        @Override // says ViewHolder, but it's acually MyRowViews object
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {// position is which row we're building

            MyRowViews thisRowLayout = (MyRowViews)holder;
            thisRowLayout.messageText.setText(messages.get(position).getMessage());
            thisRowLayout.setPosition(position);
        }
        @Override
        public int getItemCount() {
            return messages.size();
        }// how many items to show? // row layout is match_parent
    }

    private class ChatMessage{// data model for a message in a row
        String message;
        int sendOrReceive;

        public ChatMessage(String message, int sendOrReceive) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
        }
        public String getMessage() {
            return message;
        }
    }





}
