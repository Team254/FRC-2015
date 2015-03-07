package com.team254.frc2015.auto.modes;

import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

public class TestDriveAutoMode extends AutoMode {
    static int i = 0;
    static double angles[] = { Math.PI / 2.0, Math.PI / 4.0, Math.PI / 6.0 };
    @Override
    public void routine() throws AutoModeEndedException {
        drive.setDistanceSetpoint(60);
        waitForDrive(3);
        drive.setTurnSetPoint(Math.PI / 4.0);
        waitForDrive(3);
        drive.reset();
        drive.setDistanceSetpoint(40);
        waitForDrive(2);
        drive.setTurnSetPoint(0);
        waitForDrive(2.0);
        drive.reset();
        drive.setDistanceSetpoint(35);
        waitForDrive(2.0);
        drive.setTurnSetPoint(-Math.PI / 4.0);
        waitForDrive(2.0);
        drive.reset();
        drive.setDistanceSetpoint(35);
        waitForDrive(2);
        drive.setTurnSetPoint(0);
        waitForDrive(3);
        drive.reset();
        drive.setDistanceSetpoint(60);

    }
}
