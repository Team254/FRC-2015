package com.team254.frc2015.auto.modes;

import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

/**
 * Created by tombot on 3/21/15.
 */
public class TestBangBangAutoMode extends AutoMode {
    @Override
    protected void routine() throws AutoModeEndedException {
        bottom_carriage.setPositionSetpoint(35., true);
        top_carriage.setPositionSetpoint(60., true);
        waitTime(5.0);
        bottom_carriage.setFastPositionSetpoint(15);
        waitForCarriage(bottom_carriage, 2.0);
        bottom_carriage.setFastPositionSetpoint(35);
        waitForCarriage(bottom_carriage, 2.0);
        bottom_carriage.setFastPositionSetpoint(15);
        waitForCarriage(bottom_carriage, 2.0);
        bottom_carriage.setFastPositionSetpoint(35);


    }
}
