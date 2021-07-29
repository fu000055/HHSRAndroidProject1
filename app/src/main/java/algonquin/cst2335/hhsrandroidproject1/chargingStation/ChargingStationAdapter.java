package algonquin.cst2335.hhsrandroidproject1.chargingStation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.hhsrandroidproject1.R;

public class ChargingStationAdapter extends RecyclerView.Adapter<ChargingStationAdapter.MyRowViews> {

    private ArrayList<ChargingStationPOJO> stationList;

    public ChargingStationAdapter(ArrayList<ChargingStationPOJO> stationList, Boolean fullInfo) {
        this.stationList = stationList;
        this.fullInfo = fullInfo;
    }

    private Boolean fullInfo = false;


    public ChargingStationAdapter(ArrayList<ChargingStationPOJO> stationList) {
        this.stationList = stationList;
    }


    @Override
    public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View loadedRow = inflater.inflate(R.layout.charging_station_item, parent, false);
        return new MyRowViews(loadedRow);
    }

    @Override
    public void onBindViewHolder(MyRowViews holder, int position) {
        String title = stationList.get(position).getTitle();
        if(fullInfo){
            holder.rowDetails.setText(String.format("Station: %s\n Latitude: %.2f\n Longitude: %.2f\n Phone: %s",
                    title, stationList.get(position).getLatitude(),
                    stationList.get(position).getLongitude(), stationList.get(position).getPhone()));
        }else {
            holder.rowDetails.setText(title);
        }
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    public class MyRowViews extends RecyclerView.ViewHolder {

        TextView rowDetails;

        int position = -1;

        public MyRowViews(View itemView) {
            super(itemView);
            rowDetails = itemView.findViewById(R.id.row_title);
            itemView.setOnClickListener(clk ->{
                Boolean flag = fullInfo;
                if(fullInfo){
                //    itemView.requestFocus();
                    int position = getAbsoluteAdapterPosition();
                    ChargingStationFavouriteStation favActivity = (ChargingStationFavouriteStation)itemView.getContext();
                    favActivity.userClickedOneOfFavStation(position);
                   // parentActivity.userClickedOneOfFavStation(stationList.get(position), position);
                }else {
                    ChargingStation parentActivity = (ChargingStation)itemView.getContext();
                    int position = getAbsoluteAdapterPosition();
                    parentActivity.userClickedStation(stationList.get(position), position);
                    //rowDetails.setText();
                }


            });
            //rowDetails = itemView.findViewById(R.id.row_title);
        }
        public void setPosition(int p){
            position = p;
        }
    }
}