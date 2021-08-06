package algonquin.cst2335.hhsrandroidproject1.oct;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import algonquin.cst2335.hhsrandroidproject1.R;

import java.util.ArrayList;

import algonquin.cst2335.hhsrandroidproject1.MainActivity;
import algonquin.cst2335.hhsrandroidproject1.R;

/** This class holds the histories that user searched.
 * @author Rong Fu
 * @version 1.0
 */
public class Favourites extends AppCompatActivity {
    RecyclerView favouritesView;
    ArrayList<Histories> stopInfoList = new ArrayList<>();
    MyHistoriesAdapter adt = new MyHistoriesAdapter();
    StationDetail myStationDetail = new StationDetail();
    SQLiteDatabase db;
    Toolbar mytoolbar;

    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items.
     * @return calling super from the parent class.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.oct_favourites_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This function is called whenever an item in options menu is selected.
     * @param item The menu item that was selected. This value cannot be null.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
//            case R.id.id_favorites_oct:
//                Intent favoritesPage = new Intent(this, Favourites.class);
//                startActivity(favoritesPage);;
//                break;
            case R.id.id_search_oct:
                Intent searchPage = new Intent(this, OCTranspoBusRouteActivity.class);
                startActivity(searchPage);
                break;

            case R.id.car_charging_menu:
                Intent carCharging = new Intent(this, ChargingStation.class);
                startActivity(carCharging);
                break;
            case R.id.movie_menu:
                Intent movie = new Intent(this, Movie_Database_API.class);
                startActivity(movie);
                break;
            case R.id.id_soccer_games:
                Intent soccerGame = new Intent(this, SoccerGameActivity.class);
                startActivity(soccerGame);
                break;
            case R.id.id_main_from_oct:
                Intent soccerGame = new Intent(this,MainActivity.class);
                startActivity(soccerGame);
                break;
            case R.id.id_help_oct:
                AlertDialog.Builder builder = new AlertDialog.Builder(Favourites.this);
                builder.setMessage(R.string.oct_help_alert )
                        .setTitle(R.string.oct_help_alert_title)
                        .setPositiveButton(R.string.oct_help_alert_cancel,(dialog, cl) ->{

                        }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function uses to call the activity first stats up.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.favourites_oct_layout);
            mytoolbar = findViewById(R.id.toolbar);
            setSupportActionBar(mytoolbar);

            DrawerLayout drawer = findViewById(R.id.favourite_drawer_layout);
            ImageView star = findViewById(R.id.imageView);
            //Generate Open and Close strings
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,mytoolbar,R.string.oct_open,R.string.oct_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = findViewById(R.id.popup_menu_oct_f);
            navigationView.setNavigationItemSelectedListener((item) -> {
                onOptionsItemSelected(item);//call the function for the other Toolbar
                drawer.closeDrawer(GravityCompat.START);
                return false;

            });

            Intent fromOct = getIntent();
            String stationNumber = fromOct.getStringExtra("StationNumber");

            OCTOpenHelper opener = new OCTOpenHelper(this);
            db = opener.getWritableDatabase();
            //The SQLiteDatabase class has a function called rawQuery, where you can write your query in SQL
            //The rawQuery function returns something a Cursor object
            Cursor results = db.rawQuery("Select * from " + OCTOpenHelper.TABLE_NAME + ";", null);
            int _idCol = results.getColumnIndex("_id");
            int stationCol = results.getColumnIndex(OCTOpenHelper.col_stationNumber);
            int searchCol = results.getColumnIndex(OCTOpenHelper.col_stationNumber);
            results.moveToNext();

            while (results.moveToNext()) {
                long id = results.getInt(_idCol);
                String stationInfo = results.getString(stationCol);
                int searchBtn = results.getInt(searchCol);
                stopInfoList.add(new Histories(stationInfo, searchBtn, id));
            }
            // save message

            favouritesView = findViewById(R.id.myrecycler);
            favouritesView.setAdapter(adt);
            LinearLayoutManager mgr = new LinearLayoutManager(this);
            mgr.setStackFromEnd(true);
            mgr.setReverseLayout(true);
            favouritesView.setLayoutManager(mgr);

            Button btnFReturn = findViewById(R.id.oct_favouritesRerun);

            btnFReturn.setOnClickListener(fr_clk ->{
                Intent previousPage = new Intent(Favourites.this, OCTranspoBusRouteActivity.class);
                startActivity(previousPage);
            });

        }
    /**
     * This class represents a row that inherits from RecyclerView.ViewHolder.
     */
        private class MyRowViews extends RecyclerView.ViewHolder{
            //The view that is passed in as a parameter represents the ConstraintLayout that is the root of the row.
            TextView historiesText;
            ImageView deleteImg;
            int position = -1;

            public MyRowViews(View itemView) {
                super(itemView);
                itemView.setOnClickListener(clk -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Favourites.this);
                    builder.setMessage("Do you want to delete this history research?" + historiesText.getText())
                            .setTitle("Question")
                            .setNegativeButton("No",(dialog, cl) ->{})
                            .setPositiveButton("Yes",(dialog, cl) ->{
                                position = getAbsoluteAdapterPosition();
                                Favourites.Histories removeStopInfo = stopInfoList.get(position);
                                stopInfoList.remove(position);
                                adt.notifyItemRemoved(position);
                                //Snackbar for cancel the previous operation
                                db.delete(OCTOpenHelper.TABLE_NAME,"_id=?",new String[]{Long.toString(removeStopInfo.getId())});
                                Snackbar.make(historiesText, "You deleted search history #" +position, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", clk2 ->{
                                            stopInfoList.add(position,removeStopInfo);
                                            adt.notifyItemInserted(position);
                                            db.execSQL("Insert into " + OCTOpenHelper.TABLE_NAME + " values('" + removeStopInfo.getId() +
                                                    "','" + removeStopInfo.getStop() +
                                                    "','" + removeStopInfo.getSearchButton() +"');");
                                        }).show();
                            }).create().show();
                });
                historiesText = itemView.findViewById(R.id.favouritesList);

                deleteImg = itemView.findViewById(R.id.deleteImg);
            }
            //  public  void setPosition(int p){position = p;}
        }

    /**
     * Adapters provide a binding from an app-specific data set to views that are displayed within a RecyclerView.
     */
        private class MyHistoriesAdapter extends RecyclerView.Adapter <MyRowViews> {

        /**
         * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
         * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
         * @param viewType The view type of the new View.
         * @return
         */
            @Override
            public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
//                LayoutInflater inflater = getLayoutInflater();
//                View loadedRow = inflater.inflate(R.layout.histories_list,parent,false);
//                return new MyRowViews(loadedRow);
                return new MyRowViews(getLayoutInflater().inflate(R.layout.faourites_oct_list,parent,false));
            }
        /**
         * Called by RecyclerView to display the data at the specified position.
         * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
            @Override
            public void onBindViewHolder(MyRowViews holder, int position) {
//                MyRowViews thisRowLayout = (MyRowViews)holder;
                holder.historiesText.setText(stopInfoList.get(position).getStop());

//                thisRowLayout.historiesText.setText(stopInfoList.get(position).getStop());
//                thisRowLayout.setPosition(position);
            }
        /**
         * This method is to get the amount of articles.
         * @return Return the amount of articles.
         */
            @Override
            public int getItemCount() {
                return stopInfoList.size();
            }
        }

    /**
     * This class is the Favourites(Histories) information.
     */
        private class Histories {
            String stop;
            int searchButton;
            long id;
            public void setId(long l){id = l;}
            public long getId(){ return id;}

            public Histories(String stop, int searchButton) {
                this.stop = stop;
                this.searchButton = searchButton;
            }
            public Histories(String stop, int searchButton, long id) {
                this.stop = stop;
                this.searchButton = searchButton;
                setId(id);
            }
            public String getStop() {
                return stop;
            }
            public int getSearchButton(){
                return searchButton;
            }

        }

        @Override
        public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
            super.startActivityFromFragment(fragment, intent, requestCode);
        }
    }

