package com.team254.frc2015.subsystems;

import com.team254.lib.util.Subsystem;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class Drive extends Subsystem {

	public Victor leftMotorA;
	public Victor leftMotorB;
	public Victor rightMotorA;
	public Victor rightMotorB;
	public Encoder leftEncoder;
	
	public Drive(String name, Victor leftDriveA, Victor leftDriveB, Victor rightDriveA, Victor rightDriveB, Encoder leftDriveEncoder) {
		super(name);
		this.leftMotorA = leftDriveA;
		this.leftMotorB = leftDriveB;
		this.rightMotorA = rightDriveA;
		this.rightMotorB = rightDriveB;
		this.leftEncoder = leftDriveEncoder;
	}
	
	public void setLeftRight(double left, double right) {
		leftMotorA.set(left);
		leftMotorB.set(left);
		rightMotorA.set(right);
		rightMotorB.set(right);
	}

}
