package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.hhsrandroidproject1.R;

public class MessageDetailsFragment extends Fragment {
    MessageListFragment.ChatMessage chosenMessage;
    int chosenPosition;

    public MessageDetailsFragment(MessageListFragment.ChatMessage message,int position ){
        chosenMessage = message;
        chosenPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.movie_detail_layout,container,false);

        TextView messageView = detailsView.findViewById(R.id.messageView);
        TextView sendView = detailsView.findViewById(R.id.sendView);
        TextView timeView = detailsView.findViewById(R.id.timeView);
        TextView idView = detailsView.findViewById(R.id.databaseIdView);

        messageView.setText("Message is: " + chosenMessage.getMessage());
        sendView.setText("Send or Receive? " + chosenMessage.getMessage());
        timeView.setText("Time send: " + chosenMessage.getTimeSent());
        idView.setText("Database id is: " + chosenMessage.getId());

        Button closeButton = detailsView.findViewById(R.id.cloButton);
        closeButton.setOnClickListener(closeClicked ->{

        });

        Button deleteButton = detailsView.findViewById(R.id.delButton);
        deleteButton.setOnClickListener(deleteClicked ->{

            MovieInfoRecord parentActivity = (MovieInfoRecord) getContext();
            parentActivity.notifyMessageDeleted(chosenMessage,chosenPosition);

            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        return detailsView;
    }
}
