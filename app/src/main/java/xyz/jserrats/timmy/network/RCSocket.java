package xyz.jserrats.timmy.network;

import java.io.IOException;
import java.net.*;

import android.util.Log;

public class RCSocket {

    private DatagramSocket socket;
    public InetAddress ip;
    private final int port = 1234;


    public RCSocket(String hostname) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        ip = InetAddress.getByName(hostname);
    }

    public void send_info(RCPacket rcPacket) throws IOException {
        byte[] rawBytes = rcPacket.generateRawByteArray();
        socket.send(new DatagramPacket(rawBytes, rawBytes.length, ip, port));
        Log.d("important", "Packet sent. ID: " + rcPacket.id);
    }
}
