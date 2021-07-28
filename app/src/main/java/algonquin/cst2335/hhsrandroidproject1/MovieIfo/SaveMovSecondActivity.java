package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.hhsrandroidproject1.R;

public class SaveMovSecondActivity extends AppCompatActivity {

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_save_second);
        Intent fromPrevious = getIntent();
        String theaterName = fromPrevious.getStringExtra("TheaterName");

        TextView topOfScreen = findViewById(R.id.textView);
        topOfScreen.setText("The Movie Theater Name : " + theaterName);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("phoneNumber", "");
        EditText editText = findViewById(R.id.editTextPhone);
        editText.setText(phoneNumber);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(clk ->{
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + editText.getText().toString()));
            startActivityForResult(call,1);
        });



    }





}
