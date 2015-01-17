package com.team254.frc2015;

import com.team254.frc2015.subsystems.Drive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class HardwareAdaptor {
	
	public static Drive drive = new Drive("drive",
										  new Talon(2),
										  new Talon(3),
										  new Talon(0),
										  new Talon(5),
										  new Encoder(0,1),
										  new Encoder(2,3));
}
