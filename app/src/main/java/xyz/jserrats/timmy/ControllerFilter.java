package xyz.jserrats.timmy;

import xyz.jserrats.timmy.network.RCPacket;

public class ControllerFilter {

    public RCPacket newRead(float r, float l) {

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

        return new RCPacket(RF, RB, LF, LB);
    }
}
