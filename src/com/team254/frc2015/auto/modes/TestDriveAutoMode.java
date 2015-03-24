package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;
import com.team254.frc2015.subsystems.TopCarriage;

/**
 * Created by tombot on 3/21/15.
 */
public class TestDriveAutoMode extends AutoMode {
    @Override
    protected void routine() throws AutoModeEndedException {
        top_carriage.setGrabberPosition(TopCarriage.GrabberPositions.CLOSED);
        top_carriage.setPositionSetpoint(30, true);
        waitTime(5.0);
        drive.reset();
        intake.close();
        intake.setSpeed(Constants.kManualIntakeSpeed);
        drive.setTurnSetPoint(-Math.PI);
        waitForDrive(4.0);
        drive.reset();


    }
}
