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

public class SaveSecondActivity extends AppCompatActivity {

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2)
        {
            if(resultCode == RESULT_OK)
            {
                Bitmap thumbnail = data.getParcelableExtra("data");
//                ImageView profileImage = findViewById(R.id.imageView);
//                profileImage.setImageBitmap(thumbnail);
                FileOutputStream fOut = null;
                try {
                    fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                }catch (FileNotFoundException e) {
                    e.printStackTrace();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }*/

   /* @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = prefs.edit();
        EditText editText = findViewById(R.id.editTextPhone);
        editor.putString("phoneNumber", editText.getText().toString());
        editor.apply();

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_second);
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
