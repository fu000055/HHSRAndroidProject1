package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import algonquin.cst2335.hhsrandroidproject1.R;

public class MessageMovListFragment extends Fragment {
    ArrayList<ChatMessage> messages = new ArrayList<>();
    RecyclerView chatlist;
    MyChatAdapter adt = new MyChatAdapter();

    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
    String currentDateandTime = sdf.format(new Date());

    SQLiteDatabase db;
    Button send;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View chatLayout = inflater.inflate(R.layout.movie_info_record,container,false);

        send = chatLayout.findViewById(R.id.addMoviebutton);

        chatlist = chatLayout.findViewById(R.id.myrecycler);
        EditText messageTyped =chatLayout.findViewById(R.id.messageEdit);
        Button sentBtn = chatLayout.findViewById(R.id.addMoviebutton);
        Button receiveBtn = chatLayout.findViewById(R.id.addActorbutton);


        MovieOpenHelper opener = new MovieOpenHelper(getContext());

        /**
         * Open the database
         */
        db = opener.getWritableDatabase();

        Cursor results = db.rawQuery("Select * from " + MovieOpenHelper.TABLE_NAME + ";", null);
        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(MovieOpenHelper.col_message);
        int sendCol = results.getColumnIndex(MovieOpenHelper.col_sent_receive);
        int timeCol = results.getColumnIndex(MovieOpenHelper.col_time_sent);

        while(results.moveToNext()) {
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            String time = results.getString(timeCol);
            int sendOrReceieve = results.getInt(sendCol);
            messages.add(new ChatMessage(message, sendOrReceieve, time, id));
        }
        chatlist.setAdapter(adt);
        chatlist.setLayoutManager(new LinearLayoutManager(getContext()));

        sentBtn.setOnClickListener(clk->{
            ChatMessage cm = new ChatMessage(messageTyped.getText().toString(),1,currentDateandTime);
            //insert into the database
            ContentValues newRow = new ContentValues();
            newRow.put(MovieOpenHelper.col_message, cm.getMessage());
            newRow.put(MovieOpenHelper.col_sent_receive,cm.getSentOrReceive());
            newRow.put(MovieOpenHelper.col_time_sent,cm.getTimeSent());

            //now insert
            long newId = db.insert(MovieOpenHelper.TABLE_NAME, MovieOpenHelper.col_message, newRow);
            cm.setId(newId);

            messages.add(cm);
            messageTyped.setText("");
            adt.notifyItemInserted(messages.size()-1);
        });

        receiveBtn.setOnClickListener(clk->{
            ChatMessage cm = new ChatMessage(messageTyped.getText().toString(),2,currentDateandTime);
            ContentValues newRow = new ContentValues();
            newRow.put(MovieOpenHelper.col_message, cm.getMessage());
            newRow.put(MovieOpenHelper.col_sent_receive,cm.getSentOrReceive());
            newRow.put(MovieOpenHelper.col_time_sent,cm.getTimeSent());
            //now insert
            long newId = db.insert(MovieOpenHelper.TABLE_NAME, MovieOpenHelper.col_message, newRow);
            cm.setId(newId);

            messages.add(cm);
            messageTyped.setText("");
            adt.notifyItemInserted(messages.size()-1);
        });
        return chatLayout;
    }

    public void notifyMessageDeleted(ChatMessage chosenMessage, int chosenPosition) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("Do you want to delete the message: " + chosenMessage.getMessage())

                .setTitle("Qustions:")

                .setPositiveButton("Yes",(dialog,cl)->{
                    //position = getAbsoluteAdapterPosition();
                    ChatMessage removedMessage = messages.get(chosenPosition);
                    messages.remove(chosenPosition);
                    adt.notifyItemRemoved(chosenPosition);

                    db.delete(MovieOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(removedMessage.getId())});

                    Snackbar.make(send,"You deleted message #" + chosenPosition, Snackbar.LENGTH_SHORT)
                            .setAction("Undo",clk -> {

                                messages.add(chosenPosition,removedMessage);
                                adt.notifyItemInserted(chosenPosition);

                                db.execSQL("Insert into " + MovieOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
                                        "','" + removedMessage.getMessage() +
                                        "','" + removedMessage.getSentOrReceive() +
                                        "','" + removedMessage.getTimeSent() + "');");

                            })
                            .show();
                })

                .setNegativeButton("No",(dialog,cl)->{

                })
                .create().show();


    }

    private class MyRowViews extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;
        int position = -1;
        //This row,have a TestView for message
        public MyRowViews(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(click->{


                MovieInfoRecord parentActivity = (MovieInfoRecord)getContext();
               // int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage(messages.get(position),position);


            });

        }

        public void setPosition(int p){
            position = p;
        }
    }

    private class MyChatAdapter extends RecyclerView.Adapter{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();

            int layoutID;
            if(viewType == 1){
                layoutID = R.layout.movie_sent_message_layout;
            }else{
                layoutID = R.layout.movie_receive_message_layout;
            }
            View loadedRow = inflater.inflate(layoutID, parent, false);
            //initialize the TextView
            return  new MyRowViews(loadedRow);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyRowViews thisRowLayout = (MyRowViews) holder;
            thisRowLayout.messageText.setText(messages.get(position).getMessage());
            thisRowLayout.timeText.setText(messages.get(position).getTimeSent());
            thisRowLayout.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }
        @Override
        public int getItemViewType(int position) {
            return messages.get(position).sentOrReceive;
        }

    }

    class ChatMessage{

        String message;
        int sentOrReceive;
        String timeSent;
        long id;

        public ChatMessage(String message, int sentOrReceive, String timeSent) {
            this.message = message;
            this.sentOrReceive = sentOrReceive;
            this.timeSent = timeSent;
        }

        public ChatMessage(String message, int sentOrReceive, String timeSent, long id){
            this.message = message;
            this.sentOrReceive = sentOrReceive;
            this.timeSent = timeSent;
            setId(id);
        }

        public String getMessage() {
            return message;
        }

        public int getSentOrReceive() {
            return sentOrReceive;
        }

        public String getTimeSent() {
            return timeSent;
        }

        public void setId(Long l){
            id = l;
        }

        public long getId(){
            return id;
        }

    }
}
