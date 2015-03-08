package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

/**
 * Created by tombot on 2/28/15.
 */
public class ThreeBinNoCanAutoMode extends AutoMode {

    @Override
    protected void routine() throws AutoModeEndedException {
        intake.open();
        bottom_carriage.setFlapperOpen(false);

        // Get first can out of way, drive slowly to 2nd tote
        top_carriage.setPositionSetpoint(Constants.kTopCarriageMaxPositionInches, true);
        drive.setDistanceSetpoint(70, 30);
        waitTime(1.0);
        bottom_carriage.setPositionSetpoint(18, true);
        waitForDrive(5.0);

        // Intake 2nd tote
        intake.close();
        intake.setSpeed(1.0);
        waitTime(.5);
        intake.setSpeed(0);

        // Index 2nd tote
        bottom_carriage.setPositionSetpoint(2, false);
        waitForCarriage(bottom_carriage, 2.0);
        intake.open();
        bottom_carriage.setPositionSetpoint(24, true);

        // Knock can
        intake.setSpeed(.6);
        drive.setDistanceSetpoint(115, 40);
        waitTime(1.1);

        // Close intake on can
        intake.close();
        intake.setSpeed(0);
        waitForDrive(2.0);

        // Turn with can intake and open
        drive.setTurnSetPoint(Math.PI/3.0);
        waitTime(.5);
        intake.open();
        waitForDrive(.25);

        // Turn back to center line
        drive.setTurnSetPoint(0);
        waitForDrive(1.8);

        // Drive to last tote
        drive.reset();
        drive.setDistanceSetpoint(40);
        bottom_carriage.setPositionSetpoint(18, true);
        intake.open();

        // Grab 3rd tote
        waitForDrive(2.0);
        intake.setSpeed(1.0);
        intake.close();

        // Turn to face auto zone
        drive.setTurnSetPoint(Math.PI / 3.4);
        waitForDrive(1.2);

        // Drive to auto zone
        drive.reset();
        drive.setDistanceSetpoint(145);
        intake.close();
        intake.setSpeed(0.0);

        // Index 3rd tote while driving
        bottom_carriage.setPositionSetpoint(2, true);
        bottom_carriage.setFlapperOpen(true);
        waitForDrive(3.0);

        // Turn back to horizontal
        drive.setTurnSetPoint(0);
        waitForDrive(1.4);

        // Let go of stack and drive away
        drive.reset();
        drive.setDistanceSetpoint(-60);
        intake.open();


        waitForDrive(3.0);
        bottom_carriage.setFlapperOpen(false);


    }
}
