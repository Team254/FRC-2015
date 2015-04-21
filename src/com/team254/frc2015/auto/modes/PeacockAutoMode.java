package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

public class PeacockAutoMode extends AutoMode {

    public PeacockAutoMode() {
        m_update_rate = 1.0 / 200.0;
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        waitTime(.07); // 6ms before drive
        drive.reset();
        drive.setFinishLineSetpoint(50, 0);
        waitForDrive(5);
        drive.setDistanceSetpoint(95);
        waitTime(10);
        motorPeacock.disableControlLoop();
    }

    @Override
    public void prestart() {
        motorPeacock.setPowerTimeSetpoint(1.0, .15, .1, .1); // 100% for 150ms, ramp down to .1 over 10ms
        intake.open();
    }
}