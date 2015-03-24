package com.team254.frc2015.auto.modes;

import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

/**
 * Created by tombot on 3/21/15.
 */
public class TestDriveAutoMode extends AutoMode {
    @Override
    protected void routine() throws AutoModeEndedException {
        drive.reset();
        drive.setDistanceSetpoint(8.0);
        waitForDrive(3.0);
        drive.setTurnSetPoint(-Math.PI);
        waitForDrive(4.0);
        drive.reset();
        drive.setDistanceSetpoint(80.0);
        waitForDrive(3.0);

    }
}
