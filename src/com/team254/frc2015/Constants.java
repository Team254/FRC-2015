package com.team254.frc2015;

import com.team254.lib.util.ConstantsBase;

public class Constants extends ConstantsBase {
    public static double kDriveSensitivity = .75;
    public static double kNegativeInertiaScalar = 5.0;

    // Operator controls
    public static double kElevatorJogFastPwm = 1.0;
    public static double kElevatorJogMediumPwm = .7;
    public static double kElevatorJogSlowPwm = .35;
    public static double kManualIntakeSpeed = 1.0;
    public static double kManualExhaustSpeed = .6;

    public static double kAutoIntakeSpeed = 1.0;
    public static double kCoopIntakeSpeed = .4;
    public static double kCanIntakeSlowSpeed = .25;
    public static double kCanIntakeSpeed = .3;
    public static double kSpinnyThingSpeed = .65;

    public static double kPeacockUpManualPWM = .45;
    public static double kPeacockDownManualPWM = .1;
    // Auto mode stuff
    public static double kPeacockDriveDelayTime = .3;

    // Elevator carriage TrajectoryFollowingPositionController gains
    public static double kElevatorCarriagePositionKp = 1.0;
    public static double kElevatorCarriagePositionKi = 1.5;
    public static double kElevatorCarriagePositionKd = 0.0;
    public static double kElevatorCarriagePositionKv = 0.02;
    public static double kElevatorCarriagePositionKa = 6.0E-4;
    public static double kElevatorOnTargetError = 0.125;

    // DriveStraightController gains
    public static double kDriveMaxSpeedInchesPerSec = 120.0;
    public static double kDriveMaxAccelInchesPerSec2 = 107.0;
    public static double kDrivePositionKp = 0.7;
    public static double kDrivePositionKi = 0;
    public static double kDrivePositionKd = 0;
    public static double kDriveStraightKp = 3.0;
    public static double kDriveStraightKi = 0;
    public static double kDriveStraightKd = 0;
    public static double kDrivePositionKv = 0.008;
    public static double kDrivePositionKa = 0.0017;
    public static double kDriveOnTargetError = 0.75;
    public static double kDrivePathHeadingFollowKp = 0.01;

    // TurnInPlaceController gains
    public static double kTurnMaxSpeedRadsPerSec = 5.25;
    public static double kTurnMaxAccelRadsPerSec2 = 5.25;
    public static double kTurnKp = 3.0;
    public static double kTurnKi = 0.18;
    public static double kTurnKd = 0.23;
    public static double kTurnKv = 0.085;
    public static double kTurnKa = 0.075;
    public static double kTurnOnTargetError = 0.0225;

    // Height presets
    public static double kCoopTopHeight = 40;
    public static double kCoopBottomHeight = 10.75;

    public static double kCanPickupStartTopHeight = 21.;
    public static double kCanPickupStartBottomHeight = 6.75;
    public static double kCanPickupEndTopHeight = 5.25;
    public static double kCanPickupEndBottomHeight = 1.0;

    // Sensor calibration
    public static double kBreambeamVoltage = 1.0;

    // Common carriage parameters
    public static double kElevatorMaxSpeedInchesPerSec = 72.0;
    public static double kElevatorMaxAccelInchesPerSec2 = 120.0;

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

    public static int kLeftPeacockMotorPWM = 0;
    public static int kLeftPeacockMotorPDP = 0;
    public static int kRightPeacockMotorPWM = 9;
    public static int kRightPeacockMotorPDP = 0;

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
    public static int kBottomCarriageEncoderDIOA = 17;
    public static int kBottomCarriageEncoderDIOB = 16;
    public static int kTopCarriageEncoderDIOA = 15;
    public static int kTopCarriageEncoderDIOB = 14;
    public static int kBottomCarriageHomeDIO = 8;
    public static int kTopCarriageHomeDIO = 1;
    public static int kPressureSwitchDIO = 0;

    // Analog I/O
    public static int kBreakbeamPort = 0;

    // Solenoids
    public static int kBottomCarriageBrakeSolenoidPort = 7;
    public static int kTopCarriageBrakeSolenoidPort = 6;
    public static int kTopCarriagePivotSolenoidPort = 1;
    public static int kTopCarriageGrabberCloseSolenoidPort = 0;
    public static int kTopCarriageGrabberOpenSolenoidPort = 3;
    public static int kBottomCarriagePusherSolenoidPort = 9;
    public static int kBottomCarriageFlapperSolenoidPort = 10;
    public static int kIntakeSolenoidPort = 2;
    public static int kCoopSolenoidPort = 8;
    public static int kPinballWizardSolenoidPort = 5;
    public static int kPeacockSolenoidPort = 11;


    // Relays
    public static int kCompressorRelayPort = 0;

    // !!! Physical constants
    public static double kElevatorPulleyRadiusInches = 0.564;
    public static double kElevatorEncoderCountsPerRev = 250.0;
    public static double kBottomCarriageHeight = 1.5;


    // !!! Program constants
    public static double kControlLoopsDt = 0.005;

    // !!! Control loop constants

    public static double kElevatorCarriageSafetyMarginInches = -0.5;

    // Top carriage limits
    public static double kTopCarriageMinPositionInches = 3.5;
    public static double kTopCarriageMaxPositionInches = 67.5;
    public static double kTopCarriageHomePositionInches = 10.15;
    public static double kTopCarriageReZeroPositionInches = 10.15;
    public static double kTopCarriageSafePositionInches = 4.75;

    // Bottom carriage limits
    public static double kBottomCarriageMinPositionInches = .75;
    public static double kBottomCarriageMaxPositionInches = 65.0;
    public static double kBottomCarriageHomePositionInches = 0.0;
    public static double kBottomCarriageReZeroPositionInches = 0.0;

    // Drive parameters
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
