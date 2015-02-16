package com.team254.lib.util;

import edu.wpi.first.wpilibj.Solenoid;

public class CheesySolenoid extends Solenoid {

    public CheesySolenoid(int channel) {
        super((channel > 7 ? 1 : 0), (channel > 7 ? channel - 8 : channel));
    }

}
