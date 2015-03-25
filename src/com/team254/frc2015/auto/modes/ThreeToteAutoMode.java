package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;
import com.team254.frc2015.subsystems.TopCarriage;
import com.team254.lib.util.ConstantsBase;
import edu.wpi.first.wpilibj.Timer;

/**
 * Created by tombot on 3/23/15.
 */
public class ThreeToteAutoMode extends AutoMode {

    public static double CLEAR_TOTE_HEIGHT = 20.0;
    public static double CLEAR_GROUND_HEIGHT = 7;
    public static double ON_GROUND_HEIGHT = 2;
    public static double CLEAR_WHEELS_HEIGHT = 12.5;
    Timer t = new Timer();

    protected void routine() throws AutoModeEndedException {
        t.reset();
        t.start();
        waitTime(.1);
        System.out.println(HardwareAdaptor.kGyroThread.getAngle());

        double heading_cache = 0;
        bottom_carriage.setFlapperOpen(true);
        top_carriage.setGrabberPosition(TopCarriage.GrabberPositions.CLOSED);
        waitTime(.1);

        // Move can
        top_carriage.setFastPositionSetpoint(20);
        waitForCarriageHeight(top_carriage, 16.5, true, 1.0);

        intake.setSpeed(Constants.kAutoIntakeSpeed);
        drive.setDistanceSetpoint(24);
        waitForDriveDistance(10, true, 1.0);
        intake.close();
        waitForTote(1.0);
        // Turn on squeeze
        top_carriage.squeeze();
        waitForDrive(2);
        // Lift tote a bit off ground
        bottom_carriage.setFlapperOpen(false);
        waitTime(.1);
        bottom_carriage.setPositionSetpoint(CLEAR_TOTE_HEIGHT, true);
        waitForCarriageHeight(bottom_carriage, CLEAR_TOTE_HEIGHT - 1, true, 1.0);

        // Turn 180
        drive.setTurnSetPoint(-Math.PI, 2.2);
        heading_cache = -Math.PI;
        intake.open();

        waitForDrive(5.0);

        drive.reset();

        drive.setDistanceSetpoint(25);
        waitForDriveDistance(22.5, true, 1.5);
        intake.close();
        waitForTote(1.0);
        waitForDrive(.5);

        // Start turn
        drive.setTurnSetPoint(heading_cache - .3);

        // Grab 2nd tote

        bottom_carriage.setFastPositionSetpoint(2.0);
        waitForCarriage(bottom_carriage, 1.5);
        bottom_carriage.setPositionSetpoint(CLEAR_TOTE_HEIGHT, true);
        waitForCarriageHeight(bottom_carriage, CLEAR_WHEELS_HEIGHT, true, 1.5);
        intake.setSpeed(-Constants.kAutoIntakeSpeed);
        waitForDrive(1.0);

        drive.reset();
        drive.setDistanceSetpoint(33, 42);

        waitForDrive(3.0);

        drive.setTurnSetPoint(heading_cache + 0.175);
        intake.open();
        waitForDrive(1.0);


        drive.reset();
        drive.setDistanceSetpoint(54);

        // Reverse wheels to intake
        waitForDriveDistance(22, true, 1.5);
        intake.setSpeed(Constants.kAutoIntakeSpeed);

        waitForDriveDistance(53, true, 1.5);
        intake.close();
        waitForDrive(3);


        // Turn towards auto zone
        double last_angle = heading_cache + (Math.PI/2.7);
        drive.setTurnSetPoint(last_angle);
        waitForTote(1.0);
        bottom_carriage.setFastPositionSetpoint(2.0);
        waitForCarriage(bottom_carriage, 1.5);
        bottom_carriage.setPositionSetpoint(CLEAR_GROUND_HEIGHT, true);
        waitForCarriageHeight(bottom_carriage, CLEAR_GROUND_HEIGHT - 1, true, 1.5);
        waitForTurnAngle(last_angle - .13, true, 1.0);

        //  drive forwards to auto zone
        drive.reset();
        drive.setDistanceSetpoint(105);

        waitForDrive(2.0);

        // Drop
        double top_carriage_height_end = top_carriage.getHeight();
        top_carriage.setFastPositionSetpoint(top_carriage_height_end + 15);
        intake.open();
        bottom_carriage.setFastPositionSetpoint(2.0);

        waitForCarriageHeight(bottom_carriage, 2.5, false, 1.0);
        bottom_carriage.setFlapperOpen(true);

        waitForCarriageHeight(top_carriage, top_carriage_height_end + 4, true, 1.0);

        // Drive backwards
        drive.setDistanceSetpoint(70);
        waitForDrive(2.0);

        System.out.println("Auto time: " + t.get());

    }
}
