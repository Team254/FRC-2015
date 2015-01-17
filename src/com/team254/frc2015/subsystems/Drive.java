package com.team254.frc2015.subsystems;

import com.team254.lib.util.Subsystem;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class Drive extends Subsystem {

	public Talon leftMotorA;
	public Talon leftMotorB;
	public Talon rightMotorA;
	public Talon rightMotorB;
	public Encoder leftEncoder;
	public Encoder rightEncoder;
	
	public Drive(String name, Talon leftDriveA, Talon leftDriveB, Talon rightDriveA, Talon rightDriveB, Encoder leftEncoder, Encoder rightEncoder) {
		super(name);
		this.leftMotorA = leftDriveA;
		this.leftMotorB = leftDriveB;
		this.rightMotorA = rightDriveA;
		this.rightMotorB = rightDriveB;
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
	}
	
	public void setLeftRight(double left, double right) {
		leftMotorA.set(left);
		leftMotorB.set(left);
		rightMotorA.set(-right);
		rightMotorB.set(-right);
	}

}
