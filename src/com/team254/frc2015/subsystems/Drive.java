package com.team254.frc2015.subsystems;

import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Subsystem;

import edu.wpi.first.wpilibj.Encoder;

public class Drive extends Subsystem {
	public CheesySpeedController leftMotor;
	public CheesySpeedController rightMotor;
	public Encoder leftEncoder;
	public Encoder rightEncoder;
	
	public Drive(String name, CheesySpeedController leftDrive, CheesySpeedController rightDrive, Encoder leftEncoder, Encoder rightEncoder) {
		super(name);
		this.leftMotor = leftDrive;
		this.rightMotor = rightDrive;
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
	}
	
	public void setLeftRight(double left, double right) {
		leftMotor.set(left);
		rightMotor.set(-right);
	}

}
