package com.example.timmy;

import java.io.IOException;
import java.net.*;

import android.util.Log;

public class RCSocket {

    private DatagramSocket socket;
    private InetAddress ip;
    private final int port = 1234;


    public RCSocket() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        ip = InetAddress.getByName("10.0.1.15");
        //ip = InetAddress.getByName("10.0.0.2");
    }

    public void send_info(byte[] info) throws IOException {
        socket.send(new DatagramPacket(info, 100, ip, port));
        Log.d("important", "info sent");
    }


    /*
     * Each update sent contains 4 bytes.
     * RY forward
     * RY backward
     * LY forward
     * LY backward
     *
     * */
}
