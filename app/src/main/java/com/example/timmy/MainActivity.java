package com.example.timmy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {

        Log.d("Right Trigger Value", event.getAxisValue(MotionEvent.AXIS_RTRIGGER) + "");
        Log.d("Left Trigger Value", event.getAxisValue(MotionEvent.AXIS_LTRIGGER) + "");

        Log.d("Left Stick X", event.getX() + "");
        Log.d("Left Stick Y", event.getY() + "");

        Log.d("Right Stick Y", event.getAxisValue(MotionEvent.AXIS_RZ) + "");
        Log.d("Right Stick X", event.getAxisValue(MotionEvent.AXIS_Z) + "");

        return super.onGenericMotionEvent(event);
    }
}
