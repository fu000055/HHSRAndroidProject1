package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import algonquin.cst2335.hhsrandroidproject1.R;

public class SoccerGameAdapter extends RecyclerView.Adapter<SoccerGameAdapter.SoccerGameViewHolder> {
    String data1[], data2[], data3[];
    int images[];
    Context context;

    public SoccerGameAdapter(Context ct, String title[], String date[], String description[], int img[]) {
        context = ct;
        data1 = title;
        data2 = date;
        data3 = description;
        images = img;
    }

    @Override
    public SoccerGameAdapter.SoccerGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.soccer_game_item, parent, false);
        return new SoccerGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SoccerGameViewHolder holder, int position) {
        holder.soccer_game_title.setText(data1[position]);
        holder.soccer_game_date.setText(data2[position]);
        holder.soccer_game_description.setText(data3[position]);
        holder.soccer_game_image.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class SoccerGameViewHolder extends RecyclerView.ViewHolder {
        TextView soccer_game_title, soccer_game_date, soccer_game_description;
        ImageView soccer_game_image;

        public SoccerGameViewHolder(View itemView) {
            super(itemView);
            soccer_game_title = itemView.findViewById(R.id.soccer_game_title);
            soccer_game_date = itemView.findViewById(R.id.soccer_game_date);
            soccer_game_description = itemView.findViewById(R.id.soccer_game_description);
            soccer_game_image = itemView.findViewById(R.id.soccer_game_images);
        }
    }
}
