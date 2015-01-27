package com.team254.frc2015;

import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.lib.util.CheesySpeedController;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

public class HardwareAdaptor {
	// Motors
	static CheesySpeedController kLeftDriveMotor = new CheesySpeedController(
			new SpeedController[] { new VictorSP(0) }, new int[] {0});
	static CheesySpeedController kRightDriveMotor = new CheesySpeedController(
			new SpeedController[] { new VictorSP(5) }, new int[] {2});
	static CheesySpeedController kTopCarriageMotor = new CheesySpeedController(
			new SpeedController[] { new VictorSP(1), new VictorSP(4) }, new int[] {4, 5});
	static CheesySpeedController kBottomCarriageMotor = new CheesySpeedController(
			new SpeedController[] { new VictorSP(2), new VictorSP(7) }, new int[] {6, 7});
	static CheesySpeedController kLeftIntakeMotor = new CheesySpeedController(new VictorSP(8), 8);
	static CheesySpeedController kRightIntakeMotor = new CheesySpeedController(new VictorSP(6), 9);
	
	// DIO
	static Encoder kLeftDriveEncoder = new Encoder(0, 1);
	static Encoder kRightDriveEncoder = new Encoder(2, 3);
	static Encoder kBottomCarriageEncoder = new Encoder(4, 5);
	static Encoder kTopCarriageEncoder = new Encoder(6, 7);
	static DigitalInput kBottomCarriageHome = new DigitalInput(8);
	static DigitalInput kTopCarriageHome = new DigitalInput(9);
	
	// Solenoids
	static Solenoid kBottomCarriageBrakeSolenoid = new Solenoid(0);
	static Solenoid kTopCarriageBrakeSolenoid = new Solenoid(1);
	
	// Subsystems
	public static Drive kDrive = new Drive("drive",
			kLeftDriveMotor,
			kRightDriveMotor,
			kLeftDriveEncoder,
			kRightDriveEncoder);
	public static ElevatorCarriage kTopCarriage = new ElevatorCarriage("top_carriage",
			ElevatorCarriage.Position.TOP,
			kTopCarriageMotor,
			kTopCarriageBrakeSolenoid,
			kTopCarriageEncoder,
			kTopCarriageHome);
	public static ElevatorCarriage kBottomCarriage = new ElevatorCarriage("bottom_carriage",
			ElevatorCarriage.Position.BOTTOM,
			kBottomCarriageMotor,
			kBottomCarriageBrakeSolenoid,
			kBottomCarriageEncoder,
			kBottomCarriageHome);
	public static PowerDistributionPanel kPDP = new PowerDistributionPanel();
}
