package com.team254.frc2015;

import com.team254.lib.util.ConstantsBase;

public class Constants extends ConstantsBase {
    static {
        new Constants().loadFromFile();
    }

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
    public static int kLeftDriveEncoderDIOA = 0;
    public static int kLeftDriveEncoderDIOB = 1;
    public static int kRightDriveEncoderDIOA = 2;
    public static int kRightDriveEncoderDIOB = 3;
    public static int kBottomCarriageEncoderDIOA = 4;
    public static int kBottomCarriageEncoderDIOB = 5;
    public static int kTopCarriageEncoderDIOA = 6;
    public static int kTopCarriageEncoderDIOB = 7;
    public static int kBottomCarriageHomeDIO = 8;
    public static int kTopCarriageHomeDIO = 9;
    public static int kPressureSwitchDIO = 10;

    // Solenoids
    public static int kBottomCarriageBrakeSolenoidPort = 0;
    public static int kTopCarriageBrakeSolenoidPort = 1;

    // !!! Physical constants
    public static double kElevatorPulleyRadiusInches = 0.564;
    public static double kElevatorEncoderCountsPerRev = 250.0;
    public static double kTopCarriageHeight = 6.0;
    public static double kBottomCarriageHeight = 6.0;
    public static double kDriveWheelRadius = 2.0;
    public static double kWheelbaseWidth = 26.0;
    public static double kTurnSlipFactor = 1.2;

    // !!! Program constants
    public static double kDt = 0.005;

    // !!! Control loop constants
    // Common carriage parameters
    public static double kElevatorMaxSpeedInchesPerSec = 60.0;
    public static double kElevatorMaxAccelInchesPerSec2 = 180.0;

    // Top carriage limits
    public static double kTopCarriageMinPositionInches = 10.0;
    public static double kTopCarriageMaxPositionInches = 70.0;
    public static double kTopCarriageHomePositionInches = 20.0;

    // Bottom carriage limits
    public static double kBottomCarriageMinPositionInches = 0.0;
    public static double kBottomCarriageMaxPositionInches = 60.0;
    public static double kBottomCarriageHomePositionInches = 10.0;

    // ElevatorCarriagePositionController gains
    public static double kElevatorCarriagePositionKp = 0.1;
    public static double kElevatorCarriagePositionKi = 0.0;
    public static double kElevatorCarriagePositionKd = 0.0;
    public static double kElevatorCarriagePositionKv = 0.015;
    public static double kElevatorCarriagePositionKa = 0.0;
    public static double kElevatorOnTargetError = 1.0;

    @Override
    public String getFileLocation() {
        return "~/constants.txt";
    }
}
