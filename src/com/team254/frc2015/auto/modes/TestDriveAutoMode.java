package com.team254.frc2015.auto.modes;

import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;
import com.team254.frc2015.paths.ThreeBinPath;

/**
 * Created by tombot on 3/21/15.
 */
public class TestDriveAutoMode extends AutoMode {
    @Override
    protected void routine() throws AutoModeEndedException {
        waitTime(.5);
        drive.setPathSetpoint(new ThreeBinPath());

    }

    @Override
    public void prestart() {

    }
}
