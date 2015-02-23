package com.team254.lib.util;

import edu.wpi.first.wpilibj.AnalogInput;

public class SharpGP2Y0A60SZ extends AnalogInput {

    public SharpGP2Y0A60SZ(int channel) {
        super(channel);
    }

    public double getDistance() {
        return 19.739 * Math.pow(this.getVoltage(), -1.519);
    }

}
