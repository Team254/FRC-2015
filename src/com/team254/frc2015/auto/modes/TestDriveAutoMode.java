package com.team254.frc2015.auto.modes;

import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;
import com.team254.frc2015.paths.ThreeBinPath;

public class TestDriveAutoMode extends AutoMode {
	
	@Override
	public void routine() throws AutoModeEndedException {
		drive.reset();
		drive.m_gyro.reset();
		drive.setPathSetpoint(new ThreeBinPath());
		
		waitForDrive(8);
		
		drive.reset();
		drive.setTurnSetPoint(Math.PI / 2.0);
		waitForDrive(2);
		
		drive.reset();
		drive.setDistanceSetpoint(80, 75);
		waitForDrive(3);
		
		drive.reset();
		drive.setTurnSetPoint(0);
		waitForDrive(2);
		
		drive.reset();
		drive.setDistanceSetpoint(-75, 70);
		
		
		/*drive.setTurnSetPoint(-Math.PI / 6.0);
		waitForDrive(2);
		drive.setDistanceSetpoint(50, 40);
		waitForDrive(3);
		drive.setTurnSetPoint(0);
		waitForDrive(3);
		
		/*waitForDrive(3);
		drive.setTurnSetPoint(-Math.PI / 6.0);
		
		
		drive.m_left_encoder.reset();
		drive.m_right_encoder.reset();
		drive.setDistanceSetpoint(10, 40);
		waitForDrive(2);
		
		drive.setTurnSetPoint(0);
		waitForDrive(2);
		drive.m_left_encoder.reset();
		drive.m_right_encoder.reset();
		drive.setDistanceSetpoint(10, 40);
		waitForDrive(2);
		*/
	}
	

}
