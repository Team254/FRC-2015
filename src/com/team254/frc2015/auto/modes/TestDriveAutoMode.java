package com.team254.frc2015.auto.modes;

import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;

public class TestDriveAutoMode extends AutoMode {
	
	@Override
	public void routine() throws AutoModeEndedException {
		//drive.setDistanceSetpoint(50, 35);
		//waitTime(6);
		drive.setTurnSetPoint(Math.PI);
		waitTime(6);
	}

}
