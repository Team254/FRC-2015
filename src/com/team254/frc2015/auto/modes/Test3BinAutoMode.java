package com.team254.frc2015.auto.modes;

import com.team254.frc2015.Constants;
import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;
import com.team254.lib.util.DriveSignal;

public class Test3BinAutoMode extends AutoMode {

	@Override
	protected void routine() throws AutoModeEndedException {
		bottom_carriage.setPositionSetpoint(14.75, false);
		waitTime(1.0);
		drive.setDistanceSetpoint(81, 48);
		waitForDrive(3.0);
		bottom_carriage.setPositionSetpoint(0, false);
		waitTime(1.0);
		bottom_carriage.setPositionSetpoint(14.75, false);
		waitTime(1.0);
		drive.setDistanceSetpoint(81*2.0, 48);
		waitForDrive(3.0);
		bottom_carriage.setPositionSetpoint(0, false);
		waitTime(1.0);
		bottom_carriage.setPositionSetpoint(14.75, false);
		waitTime(1.0);
        drive.setTurnSetPoint(Math.PI / 2, Constants.kTurnMaxSpeedRadsPerSec);
        waitForDrive(3.0);
        drive.m_left_encoder.reset();
        drive.m_right_encoder.reset();
        drive.m_gyro.reset();
        drive.setOpenLoop(new DriveSignal(0, 0));
        drive.setDistanceSetpoint(50);
        waitForDrive(3.0);
        bottom_carriage.setPositionSetpoint(0, false);
        waitTime(1.0);
        drive.setDistanceSetpoint(30);
        waitForDrive(3.0);
	}

}
