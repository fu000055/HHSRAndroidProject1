package algonquin.cst2335.hhsrandroidproject1.oct;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import algonquin.cst2335.hhsrandroidproject1.MainActivity;
import algonquin.cst2335.hhsrandroidproject1.R;

public class OCTranspoBusRouteActivity extends AppCompatActivity {
    RecyclerView octSearchView;
    ArrayList<Histories> stopInfo = new ArrayList<>();
    MyHistoriesAdapter adt = new MyHistoriesAdapter();
    StationDetail myStationDetail = new StationDetail();
    PopupWindow p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oct_layout);

        octSearchView = findViewById(R.id.myrecycler);
        octSearchView.setAdapter(adt);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        mgr.setStackFromEnd(true);
        mgr.setReverseLayout(true);
        octSearchView.setLayoutManager(mgr);


        Button btnSearch = findViewById(R.id.search_bar);
        //Button btnHistory = findViewById(R.id.history_bar);
        Button btnHelp = (Button) findViewById(R.id.help_bar);
        Button btnReturn = findViewById(R.id.button2);

        // save message
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String searchStop = prefs.getString("StationNumber","");
        EditText userInputText = findViewById(R.id.search_edit_text);
        userInputText.setText(searchStop);
        btnSearch.setOnClickListener(clk1 ->{
            if(!userInputText.getText().toString().isEmpty()) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("StationNumber", userInputText.getText().toString());
                editor.apply();

                Intent nextPageStation = new Intent(OCTranspoBusRouteActivity.this, StationDetail.class);
                nextPageStation.putExtra("StationNumber", userInputText.getText().toString());
                startActivity(nextPageStation);

                // create a histories message in the histories recyclerView list
                Histories historiesMessage = new Histories(userInputText.getText().toString());
                stopInfo.add(historiesMessage);
                adt.notifyItemInserted(stopInfo.size() - 1);
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
                            Histories removeStopInfo = stopInfo.get(position);
                            stopInfo.remove(position);
                            adt.notifyItemRemoved(position);
                            //Snackbar for cancel the previous operation
                            Snackbar.make(historiesText, "You deleted search history #" +position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk2 ->{
                                        stopInfo.add(position,removeStopInfo);
                                        adt.notifyItemInserted(position);
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
                holder.historiesText.setText(stopInfo.get(position).getStop());
//                thisRowLayout.historiesText.setText(stopInfo.get(position).getStop());
//                thisRowLayout.setPosition(position);

            }

            @Override
            public int getItemCount() {
                return stopInfo.size();
            }
        }
    private class Histories {
        String stop;

        public Histories(String stop) {
            this.stop = stop;
        }

        public String getStop() {
            return stop;
        }

    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        super.startActivityFromFragment(fragment, intent, requestCode);
    }
}