package com.team254.frc2015;

import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.lib.util.CheesySpeedController;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

public class HardwareAdaptor {
	// Motors
	static CheesySpeedController kLeftDriveMotor = new CheesySpeedController(
			new SpeedController[] { new VictorSP(2), new VictorSP(3) }, new int[] {0, 1});
	static CheesySpeedController kRightDriveMotor = new CheesySpeedController(
			new SpeedController[] { new VictorSP(0), new VictorSP(5) }, new int[] {2, 3});
	static CheesySpeedController kTopCarriageMotor = new CheesySpeedController(
			new SpeedController[] { new VictorSP(1), new VictorSP(4) }, new int[] {4, 5});
	static CheesySpeedController kBottomCarriageMotor = new CheesySpeedController(
			new SpeedController[] { new VictorSP(6), new VictorSP(7) }, new int[] {6, 7});
	static CheesySpeedController kLeftIntakeMotor = new CheesySpeedController(new VictorSP(8), 8);
	static CheesySpeedController kRightIntakeMotor = new CheesySpeedController(new VictorSP(9), 9);
	
	// DIO
	static Encoder kLeftDriveEncoder = new Encoder(0, 1);
	static Encoder kRightDriveEncoder = new Encoder(2, 3);
	
	// Solenoids
	// TODO
	
	// Subsystems
	public static Drive kDrive = new Drive("drive",
			kLeftDriveMotor,
			kRightDriveMotor,
			kLeftDriveEncoder,
			kRightDriveEncoder);
	public static ElevatorCarriage kTopCarriage = new ElevatorCarriage("top_carriage", 0);
	public static ElevatorCarriage kBottomCarriage = new ElevatorCarriage("bottom_carriage", 0);
	public static PowerDistributionPanel kPDP = new PowerDistributionPanel();
}
