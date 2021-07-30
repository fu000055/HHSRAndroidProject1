package algonquin.cst2335.hhsrandroidproject1.chargingStation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.hhsrandroidproject1.R;

/**
 * @author Hui Lyu
 * @version 1.0
 */
public class ChargingStationAdapter extends RecyclerView.Adapter<ChargingStationAdapter.MyRowViews> {
    /**
     * list to hold car charging stations objects
     */
    private ArrayList<ChargingStationPOJO> stationList;

    /**
     * flag of a full or partial information of a car charging station displayed in an item view
     */
    private Boolean fullInfo = false;

    /**
     * Constructor for an adapter
     *
     * @param stationList list of car charging stations objects
     * @param fullInfo    marker of a full details for an item view displayed on screen
     */
    public ChargingStationAdapter(ArrayList<ChargingStationPOJO> stationList, Boolean fullInfo) {
        this.stationList = stationList;
        this.fullInfo = fullInfo;
    }

    /**
     * Constructor for an adapter
     *
     * @param stationList list of car charging stations objects
     */
    public ChargingStationAdapter(ArrayList<ChargingStationPOJO> stationList) {
        this.stationList = stationList;
    }

    @Override
    public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View loadedRow = inflater.inflate(R.layout.charging_station_item, parent, false);
        return new MyRowViews(loadedRow);
    }

    /**
     * bind the item rows in the holder
     *
     * @param holder   a recycleView holder
     * @param position the position of the row in the holder
     */
    @Override
    public void onBindViewHolder(MyRowViews holder, int position) {
        String title = stationList.get(position).getTitle();
        if (fullInfo) {
            holder.rowDetails.setText(String.format("Station: %s\n Latitude: %.2f\n Longitude: %.2f\n Phone: %s",
                    title, stationList.get(position).getLatitude(),
                    stationList.get(position).getLongitude(), stationList.get(position).getPhone()));
        } else {
            holder.rowDetails.setText(title);
        }
        holder.setPosition(position);
    }

    /**
     * Methods counts how many car charging stations are in a list
     *
     * @return number of stations in a list
     */
    @Override
    public int getItemCount() {
        return stationList.size();
    }

    /**
     * inner class rowviews
     */
    public class MyRowViews extends RecyclerView.ViewHolder {
        /**
         * details info of car charging station in a rowview
         */
        TextView rowDetails;

        /**
         * the position of the row in the holder
         */
        int position = -1;

        /**
         * find the widget and do some actions
         *
         * @param itemView
         */
        public MyRowViews(View itemView) {
            super(itemView);
            rowDetails = itemView.findViewById(R.id.row_title);
            itemView.setOnClickListener(clk -> {
                Boolean flag = fullInfo;
                if (fullInfo) {
                    int position = getAbsoluteAdapterPosition();
                    ChargingStationFavouriteStation favActivity = (ChargingStationFavouriteStation) itemView.getContext();
                    favActivity.userClickedOneOfFavStation(position);
                } else {
                    ChargingStation parentActivity = (ChargingStation) itemView.getContext();
                    int position = getAbsoluteAdapterPosition();
                    parentActivity.userClickedStation(stationList.get(position), position);

                }
            });
        }

        /**
         * methods set the position of the charging station in the holder
         *
         * @param p position of the charging station in the list
         */
        public void setPosition(int p) {
            position = p;
        }
    }
}