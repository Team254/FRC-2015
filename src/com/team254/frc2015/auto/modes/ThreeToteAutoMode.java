package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;
import com.team254.frc2015.subsystems.TopCarriage;
import edu.wpi.first.wpilibj.Timer;

/**
 * Created by tombot on 3/23/15.
 */
public class ThreeToteAutoMode extends AutoMode {

    public static double CLEAR_TOTE_HEIGHT = 20.0;
    public static double CLEAR_TOTE_HEIGHT_LOW = 18.0;
    public static double SAFE_DRIVE_AWAY_TIME = 13.6;
    public static double CLEAR_WHEELS_HEIGHT = 12.5;
    Timer autoModeTimer = new Timer();

    protected void routine() throws AutoModeEndedException {
        autoModeTimer.reset();
        autoModeTimer.start();
        waitTime(.125); // Weird gyro init bug
        waitForGyroData(.25); // Weird gyro init bug

        bottom_carriage.setFlapperOpen(true);

        waitTime(.1);

        // Move can
        double start_height = top_carriage.getHeight();
        top_carriage.setFastPositionSetpoint(start_height + 16.0);
        waitForCarriageHeight(top_carriage, start_height + 15.0, true, 1.0);

        // Grab first tote
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

        top_carriage.setGrabberPosition(TopCarriage.GrabberPositions.CLOSED);

        // Turn 180
        drive.setTurnSetPoint(-Math.PI, 2.2);
        double heading_cache = -Math.PI;
        intake.open();
        waitForDrive(3.0);

        // Drive to second tote
        drive.reset();
        drive.setDistanceSetpoint(30);
        waitForDriveDistance(28.5, true, 1.5);
        intake.close();
        waitForTote(15.0); // die if no tote

        // Get second tote
        bottom_carriage.setFastPositionSetpoint(2.0);
        waitForCarriage(bottom_carriage, 1.5);
        bottom_carriage.setPositionSetpoint(CLEAR_TOTE_HEIGHT_LOW, true);
        waitForCarriageHeight(bottom_carriage, CLEAR_WHEELS_HEIGHT, true, 1.5);

        // Spinny thing (TM)
        intake.setLeftRight(-Constants.kSpinnyThingSpeed, Constants.kSpinnyThingSpeed);
        waitForCarriageHeight(top_carriage, 30, true, 1.5);

        // Drive through can
        drive.setDistanceSetpoint(70, 18);
        waitForDriveDistance(68, true, 5.0);

        // Drive to last tote
        intake.open();
        drive.setDistanceSetpoint(110);
        waitForDriveDistance(95, true, 2.0);
        intake.setSpeed(Constants.kAutoIntakeSpeed);
        waitForDriveDistance(108, true, 2.0);

        // grab last tote
        intake.close();
        waitForTote(10.0); // die if no tote
        waitForDrive(.5);

        // Turn towards auto zone
        double last_angle = heading_cache + (Math.PI / 2.9);
        drive.setTurnSetPoint(last_angle);
        waitForTurnAngle(last_angle - .13, true, 1.0);

        //  drive forwards to auto zone
        drive.reset();
        drive.setDistanceSetpoint(118);

        waitForDriveDistance(117.5, true, 2.0);

        double time_until_safe_drive_away = SAFE_DRIVE_AWAY_TIME - autoModeTimer.get();
        if (time_until_safe_drive_away > 0) {
            waitTime(time_until_safe_drive_away);
        }

        // Drop
        double top_carriage_height_end = top_carriage.getHeight();
        top_carriage.setFastPositionSetpoint(top_carriage_height_end + 5);

        bottom_carriage.setFastPositionSetpoint(2.0);
        waitForCarriageHeight(bottom_carriage, 3, false, 1.0);
        
        intake.open();
        intake.setSpeed(0);
        bottom_carriage.setFlapperOpen(true);

        waitForCarriageHeight(top_carriage, top_carriage_height_end + 4, true, 1.0);

        // Drive backwards
        drive.setDistanceSetpoint(75);
        waitForDrive(2.0);

        System.out.println("Auto time: " + autoModeTimer.get());

    }

    @Override
    public void prestart() {

    }
}
