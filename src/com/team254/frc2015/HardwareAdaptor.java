package com.team254.frc2015;

import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.Elevator;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Talon;

public class HardwareAdaptor {
	
	public static Drive drive = new Drive("drive",
										  new Talon(2),
										  new Talon(3),
										  new Talon(0),
										  new Talon(5),
										  new Encoder(0,1),
										  new Encoder(2,3));
	public static Elevator elevator = new Elevator("elevator", 0);
	public static PowerDistributionPanel pdp = new PowerDistributionPanel();
}
