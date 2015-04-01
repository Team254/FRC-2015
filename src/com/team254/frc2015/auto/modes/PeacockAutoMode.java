package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

/**
 * Created by tombot on 4/1/15.
 */
public class PeacockAutoMode extends AutoMode {
    @Override
    protected void routine() throws AutoModeEndedException {
        intake.open();
        peacock.setDown(true);
        waitTime(Constants.kPeacockDriveDelayTime);
        drive.setDistanceSetpoint(105);
        waitForDriveDistance(85, true, 5.0);
        peacock.setDown(false);
        waitForDrive(6);
        if (!top_carriage.isInitialized()) {
            top_carriage.setPositionSetpoint(top_carriage.getHeight() + 15, true);
        }
        peacock.setDown(false);
    }
}
