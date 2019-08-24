package xyz.jserrats.timmy.network;

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class RCPacket {

    private byte RF, RB, LF, LB;
    public int id;
    private static final AtomicInteger sequence = new AtomicInteger();

    public RCPacket(int RF, int RB, int LF, int LB) {
        this.RF = (byte) RF;
        this.RB = (byte) RB;
        this.LF = (byte) LF;
        this.LB = (byte) LB;
        this.id = sequence.getAndIncrement();

        Log.d("important", "Packet generated. ID: " + this.id);

    }

    public byte[] generateRawByteArray() {
        byte[] packet = new byte[4];

        packet[0] = RF;
        packet[1] = RB;
        packet[2] = LF;
        packet[3] = LB;
        return packet;
    }

    public boolean isEquivalent(RCPacket otherPacket) {

        int diff = 5;

        return (Math.abs(otherPacket.RF - this.RF) > diff ||
                Math.abs(otherPacket.RB - this.RB) > diff ||
                Math.abs(otherPacket.LF - this.LF) > diff ||
                Math.abs(otherPacket.LB - this.LB) > diff);

    }
}