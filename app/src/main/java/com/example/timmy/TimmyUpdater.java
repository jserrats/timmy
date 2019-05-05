package com.example.timmy;

import android.os.AsyncTask;
import android.support.v4.content.res.TypedArrayUtils;

import java.io.IOException;
import java.util.Arrays;

public class TimmyUpdater extends AsyncTask<RCSocket, Void, Void> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(RCSocket... rcSocket) {
        try {
            //byte[] test = new byte[]{(byte) 'a', (byte) 'a', (byte) 'a', (byte) 'b'};
            // byte[] test = new byte[]{(byte) 0xFF, 0, (byte) 0xFF, 0};
            byte[] packet = new byte[100];
            Arrays.fill(packet, (byte) 0);

            packet[0] = (byte)0xFF;
            packet[1] = (byte)0x00;
            packet[2] = (byte)0xFF;
            packet[3] = (byte)0x00;

            rcSocket[0].send_info(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
