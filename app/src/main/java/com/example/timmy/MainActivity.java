package com.example.timmy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    TextView textView;
    RCSocket rcSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.display);
        try {
            rcSocket = new RCSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new TimmyUpdater().execute(rcSocket);

    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        String info = "LY: " + Float.toString(event.getY()) + "\nRY: " + Float.toString(event.getAxisValue(MotionEvent.AXIS_RZ));
        textView.setText(info);
        new TimmyUpdater().execute(rcSocket);
        return super.onGenericMotionEvent(event);
    }

   // private void updateUI()
/*

    private byte[] filterInput(float input) {
        if (input == 0){

        }
    }*/
}
