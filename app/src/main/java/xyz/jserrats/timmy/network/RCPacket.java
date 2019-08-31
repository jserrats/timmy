package xyz.jserrats.timmy.network;

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class RCPacket {

    private byte RF, RB, LF, LB;
    public int id;

    String stringForm;
    private static final AtomicInteger sequence = new AtomicInteger();

    public RCPacket(int RF, int RB, int LF, int LB) {
        this.RF = (byte) RF;
        this.RB = (byte) RB;
        this.LF = (byte) LF;
        this.LB = (byte) LB;
        this.id = sequence.getAndIncrement();

        stringForm = "RF: " + RF + " RB: " + RB + "\nLF: " + LF + " LB: " + LB;
        Log.d("important", "Packet generated. ID: " + this.id);

    }

    byte[] generateRawByteArray() {
        byte[] packet = new byte[4];

        packet[0] = RF;
        packet[1] = RB;
        packet[2] = LF;
        packet[3] = LB;
        return packet;
    }

    public boolean isEquivalent(RCPacket otherPacket) {
        return (RF == otherPacket.RF && RB == otherPacket.RB && LF == otherPacket.LF && LB == otherPacket.LF);
    }

    public String toString() {
        return stringForm;
    }

}