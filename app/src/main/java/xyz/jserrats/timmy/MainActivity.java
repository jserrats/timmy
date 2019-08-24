package xyz.jserrats.timmy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.timmy.R;


public class MainActivity extends AppCompatActivity {

    Button button;
    TextView timmyIp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        timmyIp = findViewById(R.id.timmyIp);
        button = findViewById(R.id.test);
    }

    protected void startDriving(View view) {
        String ip= timmyIp.getText().toString();
        Intent intent = new Intent(this, DriveActivity.class);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

}
