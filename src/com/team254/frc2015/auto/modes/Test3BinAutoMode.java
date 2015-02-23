package com.team254.frc2015.auto.modes;

import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;
import com.team254.frc2015.paths.ThreeBinPath;

public class Test3BinAutoMode extends AutoMode {

    @Override
    protected void routine() throws AutoModeEndedException {
        System.out.println("Lift 1");

        // Open intake, run motors
        System.out.println("Run intake");
        intake.open();

        // Start driving path
        drive.reset();
        drive.m_gyro.reset();
        System.out.println("Start drive");
        drive.setPathSetpoint(new ThreeBinPath());

        // Lift 1st bin
        waitForPathSegment(20, .3);
        bottom_carriage.setPositionSetpoint(17.75, true);
        intake.setSpeed(1.0);

        // Grab 2nd bin
        waitForPathSegment(170, 5);
        intake.close();

        top_carriage.setPositionSetpoint(40, true);

        // Open intake to clear
        waitForPathSegment(220, 5);
        intake.open();
        intake.setSpeed(0);
        bottom_carriage.setPositionSetpoint(0, false);


        waitForPathSegment(320, 5);
        top_carriage.setPositionSetpoint(45, true);
        bottom_carriage.setPositionSetpoint(17.75, true);
        intake.setSpeed(0);

        waitTime(1.0);

        drive.setTurnSetPoint(Math.PI / 4.0);
        waitForDrive(1.5);

        drive.reset();
        drive.setDistanceSetpoint(20, 40);
        waitForDrive(3);

        // // Grab 3rd bin
        // waitForPathSegment(485, 5);
        // intake.close();

        // waitForPathSegment(535, 5);
        // intake.setSpeed(1.0);

        // // Open intake to clear
        // waitForPathSegment(595, 5);
        // intake.open();
        // intake.setSpeed(0);

        // // Raise 3rd bin
        // bottom_carriage.setPositionSetpoint(0, false);
        // top_carriage.setPositionSetpoint(50, true);

        // // Wait for done with path
        // waitForDrive(5);
        // bottom_carriage.setPositionSetpoint(7, false);

        // // Close on last can
        // intake.setSpeed(0);
        // intake.close();

        // drive.reset();
        // drive.setTurnSetPoint(Math.PI / 2.0);
        // waitForDrive(1.5);

        // drive.reset();
        // drive.setDistanceSetpoint(108, 75);
        // waitForDrive(3);

        // drive.reset();
        // drive.setTurnSetPoint(0);
        // waitForDrive(1.0);

        // bottom_carriage.setPositionSetpoint(0, false);
        // waitForCarriage(bottom_carriage, .5);

        // intake.open();
        // drive.reset();
        // drive.setDistanceSetpoint(-75, 70);
    }

}
