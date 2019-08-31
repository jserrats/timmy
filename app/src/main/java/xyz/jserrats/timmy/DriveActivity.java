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

    ControllerFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        this.controllerText = findViewById(R.id.controllerFeedback);
        this.connectionText = findViewById(R.id.connectionFeedback);

        filter = new ControllerFilter();
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

        RCPacket packet = filter.newRead(r, l);
        controllerText.setText(packet.toString());
        this.updater.addInstruction(packet);
        return super.onGenericMotionEvent(event);
    }


    public class TimmyUpdater extends AsyncTask<RCSocket, Boolean, Void> {

        private ArrayBlockingQueue<RCPacket> queue;
        private RCPacket lastRCpacket;

        @Override
        protected void onPreExecute() {
            this.queue = new ArrayBlockingQueue<>(1000);
            this.lastRCpacket = new RCPacket(0, 0, 0, 0);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(RCSocket... rcSocket) {
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
            return null;
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
