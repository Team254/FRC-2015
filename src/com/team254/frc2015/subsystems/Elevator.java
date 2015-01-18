package com.team254.frc2015.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import com.team254.lib.util.Subsystem;

public class Elevator extends Subsystem {
	public Solenoid elevatorSolenoid;
	
	public Elevator(String name, int solenoidPort) {
		super(name);
		this.elevatorSolenoid = new Solenoid(solenoidPort);
	}
	
	public void setElevator(boolean mode) {
		elevatorSolenoid.set(mode);
	}
}
