package algonquin.cst2335.hhsrandroidproject1.oct;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import algonquin.cst2335.hhsrandroidproject1.R;

public class RouteDetail extends AppCompatActivity{
    RouteFragment routeFragment;
    boolean isTablet = false;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_oct_layout);


        routeFragment = new RouteFragment();
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.add(R.id.oct_fragmentRoom, routeFragment);
        tx.commit();

    }
//public void userClickedMessage(RouteFragment.Trip  )

}
//    String requestProcessingTime;
//    String tripDetail;
//    String trip_longitude;
//    String trip_latitude;
//    String trip_gpsSpeed;
//    String trip_destination;
//    String trip_startTime;
//    String trip_adjustedScheduleTime;
//    String Trip_adjustmentAge;
//    String stopNo;
//    String stopLabel;
//    String error;
//    String routeDirection_routeNo;
//    boolean trip_lastTripOfSchedule;
//    String trip_busType;