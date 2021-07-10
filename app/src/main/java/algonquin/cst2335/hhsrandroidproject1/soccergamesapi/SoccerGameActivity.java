package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import algonquin.cst2335.hhsrandroidproject1.R;

public class SoccerGameActivity  extends AppCompatActivity {
    RecyclerView soccer_game_recylerView;

    String title[], description[], date[];
    int images[] = {R.drawable.bus_icon,R.drawable.charging_station,R.drawable.movie_icon,R.drawable.soccer_icon };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soccer_games_list);

        soccer_game_recylerView = findViewById(R.id.Soccer_Game_RecylerView);
        title = getResources().getStringArray(R.array.soccer_game_news);
        date = getResources().getStringArray(R.array.soccer_game_date);
        description = getResources().getStringArray(R.array.soccer_game_description);

        SoccerGameAdapter soccerGameAdapter = new SoccerGameAdapter(this,title,date,description,images);
        soccer_game_recylerView.setAdapter(soccerGameAdapter);
        soccer_game_recylerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
