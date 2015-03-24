package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;
import com.team254.frc2015.subsystems.TopCarriage;
import com.team254.lib.util.ConstantsBase;

/**
 * Created by tombot on 3/23/15.
 */
public class ThreeToteAutoMode extends AutoMode {

    public static double CLEAR_TOTE_HEIGHT = 20.0;
    public static double CLEAR_GROUND_HEIGHT = 6;
    public static double ON_GROUND_HEIGHT = 2;
    public static double CLEAR_WHEELS_HEIGHT = 12.5;

    protected void routine() throws AutoModeEndedException {
        double heading_cache = 0;
        bottom_carriage.setFlapperOpen(true);
        top_carriage.setGrabberPosition(TopCarriage.GrabberPositions.CLOSED);
        waitTime(.1);

        // Move can
        top_carriage.setFastPositionSetpoint(20);
        waitForCarriageHeight(top_carriage, 16.5, true, 1.0);

        intake.setSpeed(Constants.kAutoIntakeSpeed);
        drive.setDistanceSetpoint(24);
        waitTime(.5);
        intake.close();
        waitForTote(1.0);
        top_carriage.squeeze();

        waitTime(.4);
        waitForDrive(2);

        // Lift tote a bit off ground
        bottom_carriage.setFlapperOpen(false);
        waitTime(.08);
        bottom_carriage.setPositionSetpoint(CLEAR_GROUND_HEIGHT, true);
        waitForCarriageHeight(bottom_carriage, CLEAR_GROUND_HEIGHT - 1, true, 1.0);



        // Turn
        drive.setTurnSetPoint(-Math.PI, Math.PI / 1.4);

        heading_cache = -Math.PI;
        waitForDrive(5.0);

        // Squeeze
        bottom_carriage.setFastPositionSetpoint(CLEAR_TOTE_HEIGHT);

        drive.reset();
        intake.open();
        waitTime(.1);
        drive.setDistanceSetpoint(25);
        waitForDrive(1.5);
        intake.close();
        waitForTote(1.0);



        // Grab 2nd tote
        bottom_carriage.setFastPositionSetpoint(2.0);
        waitForCarriage(bottom_carriage, 1.5);


        bottom_carriage.setPositionSetpoint(CLEAR_TOTE_HEIGHT, true);
        waitForCarriageHeight(bottom_carriage, CLEAR_GROUND_HEIGHT, true, 1.5);


        intake.setSpeed(0);
        drive.setTurnSetPoint(heading_cache + (Math.PI / 4.75));
        waitForDrive(2.2);
        drive.reset();
        drive.setDistanceSetpoint(33, 55);
        waitForDrive(2.0);
        drive.setTurnSetPoint(heading_cache - (Math.PI / 17.0), 1.1);
        intake.open();

        waitForDrive(.7);


        drive.reset();
        drive.setDistanceSetpoint(53);
        waitForDriveDistance(40, true, 1.5);
        intake.setSpeed(Constants.kAutoIntakeSpeed);

        waitForDrive(1.5);
        intake.close();


        drive.setTurnSetPoint(heading_cache - (Math.PI / 1.75));

        waitForDrive(2.0);

        drive.reset();

        drive.setDistanceSetpoint(-140);

        waitForDriveDistance(-105, false, 3.0);
        top_carriage.setPositionSetpoint(55, true);


        waitForDriveDistance(-115, false, 3.0);
        bottom_carriage.setFastPositionSetpoint(2.0);
        intake.open();
        bottom_carriage.setFlapperOpen(true);
        top_carriage.setPositionSetpoint(55, true);





        /*drive.setDistanceSetpoint(48, 35);
        waitForDrive(5);

        intake.close();
        drive.setTurnSetPoint(heading_cache - (Math.PI / 4.0));
        waitForDrive(2.0);
        intake.setSpeed(-Constants.kEjectCanSpeed);
        waitTime(.4);
        intake.open();
        drive.setTurnSetPoint(heading_cache);
        waitForDrive(2.0);

        // Drive to third tote
        bottom_carriage.setPositionSetpoint(CLEAR_TOTE_HEIGHT, true);
        drive.reset();
        intake.setSpeed(Constants.kAutoIntakeSpeed);
        drive.setDistanceSetpoint(63);
        waitForDrive(2.0);


        intake.close();
        drive.setTurnSetPoint(heading_cache - (Math.PI / 2.0));




*/
        /*
        drive.setTurnSetPoint(heading_cache + (Math.PI/ 9.0));
        bottom_carriage.setPositionSetpoint(CLEAR_TOTE_HEIGHT, true);
        waitForCarriageHeight(bottom_carriage, CLEAR_WHEELS_HEIGHT, true, 1.0);
        intake.setLeftRight(Constants.kEjectCanSpeed, -Constants.kEjectCanSpeed);
        waitForDrive(3.0);

        drive.reset();
        drive.setDistanceSetpoint(50);
        waitForDrive(3.0);
        drive.setTurnSetPoint(heading_cache - (Math.PI / 12.0));
        waitForDrive(3.0);

        intake.close();
        intake.setSpeed(Constants.kAutoIntakeSpeed);

        drive.reset();
        drive.setDistanceSetpoint(40);



        // Move dat tote doe
        //intake.setLeftRight(Constants.kEjectCanSpeed, -Constants.kEjectCanSpeed);
        //drive.setDistanceSetpoint(90, 35);
        waitForDrive(3.0);
        drive.setTurnSetPoint(heading_cache - (Math.PI / 1.8)); // Overturn 110*
        waitForDrive(3.0);
        */



    }
}
