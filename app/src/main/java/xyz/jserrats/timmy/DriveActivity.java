package xyz.jserrats.timmy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.timmy.R;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

import xyz.jserrats.timmy.network.RCPacket;
import xyz.jserrats.timmy.network.RCSocket;

public class DriveActivity extends AppCompatActivity {

    TextView controllerText;
    TextView connectionText;

    TimmyUpdater updater;
    String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        this.controllerText = findViewById(R.id.controllerFeedback);
        this.connectionText = findViewById(R.id.connectionFeedback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            ip = extras.getString("ip");
            if (ip == null || ip.isEmpty()) {
                ip = "10.0.1.5";
            }
        } else {
            ip = "10.0.1.5";
        }
        try {
            this.updater = new TimmyUpdater();
            this.updater.execute(new RCSocket(ip));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.updater.cancel(true);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        float r = event.getY();
        float l = event.getAxisValue(MotionEvent.AXIS_RZ);


        int RF, RB, LF, LB;

        if (r < 0) { // going forward
            RF = (int) (r * -255);
            RB = 0;
        } else if (r > 0) { //going backward
            RF = 0;
            RB = (int) (r * 255);
        } else { // still
            RF = 0x00;
            RB = 0x00;
        }

        if (l < 0) { // going forward
            LF = (int) (l * -255);
            LB = 0;
        } else if (l > 0) { //going backward
            LF = 0;
            LB = (int) (l * 255);
        } else { // still
            LF = 0x00;
            LB = 0x00;
        }

        this.updater.addInstruction(new RCPacket(RF, RB, LF, LB));

        controllerText.setText("RF: " + RF + " RB: " + RB +
                "\nLF: " + LF + " LB: " + LB);

        return super.onGenericMotionEvent(event);
    }


    public class TimmyUpdater extends AsyncTask<RCSocket, Boolean, Boolean> {

        private ArrayBlockingQueue<RCPacket> queue;
        private RCPacket lastRCpacket;

        @Override
        protected void onPreExecute() {
            this.queue = new ArrayBlockingQueue<>(1000);
            this.lastRCpacket = new RCPacket(0, 0, 0, 0);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(RCSocket... rcSocket) {
            RCSocket socket;

            socket = rcSocket[0];
            boolean available = false;

            try {
                available = socket.ip.isReachable(2000);
                publishProgress(available);
                if (available) {
                    while (true) {
                        RCPacket rcPacket = this.queue.take();
                        Log.d("important", "Packet retrieved from queue. ID: " + rcPacket.id);
                        socket.send_info(rcPacket);
                    }
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            return available;

        }

        @Override
        protected void onProgressUpdate(Boolean... connected) {
            super.onProgressUpdate(connected);
            if (!connected[0]) {
                connectionText.setText("Could not connect to Timmy :(");
            } else {
                connectionText.setText("Connected Succesfully to " + ip);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                connectionText.setText("Could not connect to Timmy :(");
            }
        }


        void addInstruction(RCPacket rcPacket) {
            if (!rcPacket.isEquivalent(lastRCpacket)) {
                this.queue.add(rcPacket);
                this.lastRCpacket = rcPacket;
                Log.d("important", "Packet added to queue. ID: " + rcPacket.id);
            } else
                Log.d("important", "Packet too similar to previous to add to queue. ID: " + rcPacket.id);
        }
    }

}
