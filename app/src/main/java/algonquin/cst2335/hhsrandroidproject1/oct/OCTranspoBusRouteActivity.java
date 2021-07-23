package algonquin.cst2335.hhsrandroidproject1.oct;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import algonquin.cst2335.hhsrandroidproject1.MainActivity;
import algonquin.cst2335.hhsrandroidproject1.R;

public class OCTranspoBusRouteActivity extends AppCompatActivity {
    RecyclerView octSearchView;
    ArrayList<Histories> stopInfoList = new ArrayList<>();
    MyHistoriesAdapter adt = new MyHistoriesAdapter();
    StationDetail myStationDetail = new StationDetail();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oct_layout);

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
        Intent fromPreOCT = getIntent();
        // save message
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String searchStop = prefs.getString("StationNumber","");
        EditText userInputText = findViewById(R.id.search_edit_text);
        userInputText.setText(searchStop);

        Button btnSearch = findViewById(R.id.search_bar);
        Button btnHelp = (Button) findViewById(R.id.help_bar);
        Button btnReturn = findViewById(R.id.button2);

        octSearchView = findViewById(R.id.myrecycler);
        octSearchView.setAdapter(adt);
        LinearLayoutManager mgr = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mgr.setStackFromEnd(true);
        mgr.setReverseLayout(true);
        octSearchView.setLayoutManager(mgr);


        btnSearch.setOnClickListener(clk1 ->{
            if(!userInputText.getText().toString().isEmpty()) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("StationNumber", userInputText.getText().toString());
                editor.apply();

                Intent nextPageStation = new Intent(OCTranspoBusRouteActivity.this, StationDetail.class);
                nextPageStation.putExtra("StationNumber", userInputText.getText().toString());
                startActivity(nextPageStation);

                String newStop = userInputText.getText().toString().trim();
                boolean newSearch = true;
                for(Histories h:stopInfoList) {
                    if(h.getStop().equals(newStop)) {
                        newSearch = false;
                        break;
                    }
                }
                if(newSearch) {
                    // create a histories message in the histories recyclerView list
                    Histories historiesMessage = new Histories(userInputText.getText().toString(), 1);
                    // contentValues like a insert
                    ContentValues newRow = new ContentValues();

                    newRow.put(OCTOpenHelper.col_stationNumber, historiesMessage.getStop());
                    newRow.put(OCTOpenHelper.col_search_button, historiesMessage.getSearchButton());
                    long newId = db.insertWithOnConflict(OCTOpenHelper.TABLE_NAME, null, newRow, SQLiteDatabase.CONFLICT_REPLACE);
                    historiesMessage.setId(newId);

                    stopInfoList.add(historiesMessage);
                    adt.notifyItemInserted(stopInfoList.size() - 1);
                }
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

    private class MyRowViews extends RecyclerView.ViewHolder{
        //The view that is passed in as a parameter represents the ConstraintLayout that is the root of the row.
        TextView historiesText;
        int position = -1;

        public MyRowViews(View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(OCTranspoBusRouteActivity.this);
                builder.setMessage("Do you want to delete this history research?" + historiesText.getText())
                        .setTitle("Question")
                        .setNegativeButton("No",(dialog, cl) ->{})
                        .setPositiveButton("Yes",(dialog, cl) ->{
                            position = getAbsoluteAdapterPosition();
                            Histories removeStopInfo = stopInfoList.get(position);
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
            historiesText = itemView.findViewById(R.id.historylist);
        }
      //  public  void setPosition(int p){position = p;}
    }
    private class MyHistoriesAdapter extends RecyclerView.Adapter <MyRowViews> {

            @Override
            public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
//                LayoutInflater inflater = getLayoutInflater();
//                View loadedRow = inflater.inflate(R.layout.histories_list,parent,false);
//                return new MyRowViews(loadedRow);
                return new MyRowViews(getLayoutInflater().inflate(R.layout.histories_list,parent,false));
            }

            @Override
            public void onBindViewHolder(MyRowViews holder, int position) {
//                MyRowViews thisRowLayout = (MyRowViews)holder;
                holder.historiesText.setText(stopInfoList.get(position).getStop());
//                thisRowLayout.historiesText.setText(stopInfoList.get(position).getStop());
//                thisRowLayout.setPosition(position);

            }

            @Override
            public int getItemCount() {
                return stopInfoList.size();
            }
        }
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