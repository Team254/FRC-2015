package com.team254.frc2015;

import com.team254.frc2015.subsystems.Drive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class HardwareAdaptor {
	
	public static Drive drive = new Drive("drive",
										  new Victor(0),
										  new Victor(1),
										  new Victor(2),
										  new Victor(3),
										  new Encoder(0,1));
	

}
