package com.team254.frc2015.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

import com.team254.lib.util.Subsystem;

public class ElevatorCarriage extends Subsystem {
	public VictorSP motor;
	public Solenoid brake;
	public Encoder encoder;
	public DigitalInput home;
	
	public enum Position {
		TOP, BOTTOM
	}
	
	public double min_position;
	public double max_position;
	public double max_speed;
	public double max_acceleration;
	public double home_position;
	
	public ElevatorCarriage(String name, int solenoidPort) {
		super(name);
	}
}
