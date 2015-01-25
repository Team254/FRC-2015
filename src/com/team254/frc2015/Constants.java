package com.team254.frc2015;

import com.team254.lib.util.ConstantsBase;

public class Constants extends ConstantsBase {
	static {
		new Constants().loadFromFile();
	}

	// Physical constants
	public static double kElevatorPulleyRadiusInches = 0.564;
	public static double kElevatorEncoderCountsPerRev = 256.0;

	// Top carriage limits
	public static double kTopCarriageMinPositionInches = 10.0;
	public static double kTopCarriageMaxPositionInches = 70.0;
	public static double kTopCarriageMaxSpeedInchesPerSec = 60.0;
	public static double kTopCarriageMaxAccelInchesPerSec2 = 180.0;
	public static double kTopCarriageHomePositionInches = 20.0;

	// Bottom carriage limits
	public static double kBottomCarriageMinPositionInches = 0.0;
	public static double kBottomCarriageMaxPositionInches = 60.0;
	public static double kBottomCarriageMaxSpeedInchesPerSec = 60.0;
	public static double kBottomCarriageMaxAccelInchesPerSec2 = 180.0;
	public static double kBottomCarriageHomePositionInches = 10.0;

	// ElevatorCarriageCurrentController gains
	public static double kElevatorCarriageCurrentKp = 0.05;
	public static double kElevatorCarriageCurrentKi = 0.0;
	public static double kElevatorCarriageCurrentKd = 0.0;
	public static double kElevatorCarriageCurrentMaxOutput = 1.0;

	// ElevatorCarriagePositionController gains
	public static double kElevatorCarriagePositionKp = 0.05;
	public static double kElevatorCarriagePositionKi = 0.0;
	public static double kElevatorCarriagePositionKd = 0.0;
	public static double kElevatorCarriagePositionKv = 0.0;
	public static double kElevatorCarriagePositionKa = 0.0;

	@Override
	public String getFileLocation() {
		return "~/constants.txt";
	}
}
