package com.team254.frc2015.auto.modes;

import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

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
	}

}
