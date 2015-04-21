package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

/**
 * Created by tombot on 4/1/15.
 */
public class PeacockAutoMode extends AutoMode {

    public PeacockAutoMode() {
        m_update_rate = 1.0 / 200.0;
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        waitTime(.1);
        drive.reset();
        drive.setDistanceSetpoint(98);
        waitTime(10);
        motorPeacock.disableControlLoop();
    }

    @Override
    public void prestart() {
        motorPeacock.setPowerTimeSetpoint(1.0, .1, .1, .7); // 100% for 100ms, ramp down to .1 over 70ms
        intake.open();
    }
}
