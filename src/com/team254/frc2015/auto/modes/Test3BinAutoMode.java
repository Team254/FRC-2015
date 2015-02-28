package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;
import com.team254.frc2015.paths.ThreeBinPath;

public class Test3BinAutoMode extends AutoMode {

    @Override
    protected void routine() throws AutoModeEndedException {

        // THIS IS STILL IN DEVELOPMENT!

        // Wait for homing of top carriage to clear ground
        top_carriage.setPositionSetpoint(Constants.kTopCarriageReZeroPositionInches, true);
        waitTime(.25);

        // Start moving forward to seat 1st tote into funnel
        drive.setDistanceSetpoint(84, 35);
        bottom_carriage.setPositionSetpoint(10, true); // There is some time to make this move

        // Wait for homing
        waitForCarriage(top_carriage, 2.0);

        // Move out of way of second RC/tote combo
        bottom_carriage.setPositionSetpoint(37.75, true);
        top_carriage.setPositionSetpoint(57, true);

        waitForDrive(2.0);

        // Here is where we would use the aux grabber to nab the 2nd RC. I will move
        // the carriages for now to show when that happens
        bottom_carriage.setPositionSetpoint(42.75, true);
        top_carriage.setPositionSetpoint(62, true);



    }

}
