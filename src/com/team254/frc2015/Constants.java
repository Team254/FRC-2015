package com.team254.frc2015;

import com.team254.lib.util.ConstantsBase;

public class Constants extends ConstantsBase {

    public static double kOpenLoopCarriageDriveSpeed = 0.5;
    public static double kDriveSensitivity = .75;

    // DriveStraightController gains
    public static double kDriveMaxSpeedInchesPerSec = 120.0;
    public static double kDriveMaxAccelInchesPerSec2 = 95.0;
    public static double kDrivePositionKp = 0.06;
    public static double kDrivePositionKi = 0;
    public static double kDrivePositionKd = 0;
    public static double kDriveStraightKp = 2.0;
    public static double kDriveStraightKi = 0;
    public static double kDriveStraightKd = 0;
    public static double kDrivePositionKv = 0.0075;
    public static double kDrivePositionKa = 0.0017;
    public static double kDriveOnTargetError = 0.5;

    // TurnInPlaceController gains
    public static double kTurnMaxSpeedRadsPerSec = 1.5;
    public static double kTurnMaxAccelRadsPerSec2 = 2.0;
    public static double kTurnKp = 1.0;
    public static double kTurnKi = 0.0;
    public static double kTurnKd = 0.0;
    public static double kTurnKv = 0.25;
    public static double kTurnKa = 0.11;
    public static double kTurnOnTargetError = 0.01;


    // !!! End of editable Constants! !!!
    public static int kEndEditableArea = 0;

    // !!! Electrical constants (do not change at runtime, lol)
    // Motors
    public static int kLeftDriveMotorPWM = 4;
    public static int kLeftDriveMotor1PDP = 14;
    public static int kLeftDriveMotor2PDP = 15;

    public static int kRightDriveMotorPWM = 5;
    public static int kRightDriveMotor1PDP = 0;
    public static int kRightDriveMotor2PDP = 1;

    public static int kTopCarriageMotor1PWM = 2;
    public static int kTopCarriageMotor2PWM = 3;
    public static int kTopCarriageMotor1PDP = 12;
    public static int kTopCarriageMotor2PDP = 13;

    public static int kBottomCarriageMotor1PWM = 6;
    public static int kBottomCarriageMotor2PWM = 7;
    public static int kBottomCarriageMotor1PDP = 2;
    public static int kBottomCarriageMotor2PDP = 3;

    public static int kLeftIntakeMotorPWM = 1;
    public static int kLeftIntakeMotorPDP = 11;
    public static int kRightIntakeMotorPWM = 8;
    public static int kRightIntakeMotorPDP = 4;

    // DIO
    public static int kLeftDriveEncoderDIOA = 12; // Flipped for polarity
    public static int kLeftDriveEncoderDIOB = 13; // Flipped for polarity
    public static int kRightDriveEncoderDIOA = 11;
    public static int kRightDriveEncoderDIOB = 10;
    public static int kBottomCarriageEncoderDIOA = 15;
    public static int kBottomCarriageEncoderDIOB = 14;
    public static int kTopCarriageEncoderDIOA = 17;
    public static int kTopCarriageEncoderDIOB = 16;
    public static int kBottomCarriageHomeDIO = 8;
    public static int kTopCarriageHomeDIO = 9;
    public static int kPressureSwitchDIO = 10;

    // Solenoids
    public static int kBottomCarriageBrakeSolenoidPort = 0;
    public static int kTopCarriageBrakeSolenoidPort = 1;
    public static int kTopCarriagePivotSolenoidPort = 2;
    public static int kTopCarriageGrabberSolenoidPort = 3;
    public static int kBottomCarriagePusherSolenoidPort = 4;
    public static int kBottomCarriageFlapperSolenoidPort = 5;

    // !!! Physical constants
    public static double kElevatorPulleyRadiusInches = 0.564;
    public static double kElevatorEncoderCountsPerRev = 250.0;
    public static double kTopCarriageHeight = 6.0;
    public static double kBottomCarriageHeight = 6.0;
    public static double kDriveWheelRadius = 2.0;
    public static double kWheelbaseWidth = 26.5;
    public static double kTurnSlipFactor = 1.2;

    // !!! Program constants
    public static double kControlLoopsDt = 0.005;

    // !!! Control loop constants
    // Common carriage parameters
    public static double kElevatorMaxSpeedInchesPerSec = 60.0;
    public static double kElevatorMaxAccelInchesPerSec2 = 180.0;
    public static double kElevatorCarriageSafetyMarginInches = 1.0;

    // Top carriage limits
    public static double kTopCarriageMinPositionInches = 10.0;
    public static double kTopCarriageMaxPositionInches = 70.0;
    public static double kTopCarriageHomePositionInches = 20.0;

    // Bottom carriage limits
    public static double kBottomCarriageMinPositionInches = 0.0;
    public static double kBottomCarriageMaxPositionInches = 60.0;
    public static double kBottomCarriageHomePositionInches = 0.3;

    // Elevator carriage TrajectoryFollowingPositionController gains
    public static double kElevatorCarriagePositionKp = 0.1;
    public static double kElevatorCarriagePositionKi = 0.0;
    public static double kElevatorCarriagePositionKd = 0.0;
    public static double kElevatorCarriagePositionKv = 0.015;
    public static double kElevatorCarriagePositionKa = 0.0;
    public static double kElevatorOnTargetError = 1.0;

    // Drive gains
    public static double kDriveEncoderCountsPerRev = 250.0;
    public static double kDriveWheelSizeInches = 3.875;

    @Override
    public String getFileLocation() {
        return "~/constants.txt";
    }

    static {
        new Constants().loadFromFile();
    }
}
