package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

/**
 * Auto mode to test in-place turning
 */
public class TestTurnAutoMode extends AutoMode {
    @Override
    protected void routine() throws AutoModeEndedException {
        drive.setTurnSetPoint(Math.PI / 2.0, Constants.kTurnMaxSpeedRadsPerSec);
        waitTime(10);
    }
}
