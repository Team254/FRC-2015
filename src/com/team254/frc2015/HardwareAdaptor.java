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
            new VictorSP(Constants.kLeftDriveMotorPWM),
            new int[] { Constants.kLeftDriveMotor1PDP, Constants.kLeftDriveMotor2PDP });
    static CheesySpeedController kRightDriveMotor = new CheesySpeedController(
            new VictorSP(Constants.kRightDriveMotorPWM),
            new int[] { Constants.kRightDriveMotor2PDP, Constants.kRightDriveMotor2PDP });
    static CheesySpeedController kTopCarriageMotor = new CheesySpeedController(
            new SpeedController[] {
                    new VictorSP(Constants.kTopCarriageMotor1PWM),
                    new VictorSP(Constants.kTopCarriageMotor2PWM) }, new int[] {
                    Constants.kTopCarriageMotor1PDP,
                    Constants.kTopCarriageMotor2PDP });
    static CheesySpeedController kBottomCarriageMotor = new CheesySpeedController(
            new SpeedController[] {
                    new VictorSP(Constants.kBottomCarriageMotor1PWM),
                    new VictorSP(Constants.kBottomCarriageMotor2PWM) },
            new int[] { Constants.kBottomCarriageMotor1PDP,
                    Constants.kBottomCarriageMotor2PDP });
    static CheesySpeedController kLeftIntakeMotor = new CheesySpeedController(
            new VictorSP(Constants.kLeftIntakeMotorPWM),
            Constants.kLeftIntakeMotorPDP);
    static CheesySpeedController kRightIntakeMotor = new CheesySpeedController(
            new VictorSP(Constants.kRightIntakeMotorPWM),
            Constants.kRightIntakeMotorPDP);

    // DIO
    static Encoder kLeftDriveEncoder = new Encoder(
            Constants.kLeftDriveEncoderDIOA, Constants.kLeftDriveEncoderDIOB);
    static Encoder kRightDriveEncoder = new Encoder(
            Constants.kRightDriveEncoderDIOA, Constants.kRightDriveEncoderDIOB);
    static Encoder kBottomCarriageEncoder = new Encoder(
            Constants.kBottomCarriageEncoderDIOA,
            Constants.kBottomCarriageEncoderDIOB);
    static Encoder kTopCarriageEncoder = new Encoder(
            Constants.kTopCarriageEncoderDIOA,
            Constants.kTopCarriageEncoderDIOB);
    static DigitalInput kBottomCarriageHome = new DigitalInput(
            Constants.kBottomCarriageHomeDIO);
    static DigitalInput kTopCarriageHome = new DigitalInput(
            Constants.kTopCarriageHomeDIO);

    // Solenoids
    static Solenoid kBottomCarriageBrakeSolenoid = new Solenoid(
            Constants.kBottomCarriageBrakeSolenoidPort);
    static Solenoid kTopCarriageBrakeSolenoid = new Solenoid(
            Constants.kTopCarriageBrakeSolenoidPort);

    // Subsystems
    public static Drive kDrive = new Drive("drive", kLeftDriveMotor,
            kRightDriveMotor, kLeftDriveEncoder, kRightDriveEncoder);
    public static ElevatorCarriage kTopCarriage = new ElevatorCarriage(
            "top_carriage", ElevatorCarriage.Position.TOP, kTopCarriageMotor,
            kTopCarriageBrakeSolenoid, kTopCarriageEncoder, kTopCarriageHome);
    public static ElevatorCarriage kBottomCarriage = new ElevatorCarriage(
            "bottom_carriage", ElevatorCarriage.Position.BOTTOM,
            kBottomCarriageMotor, kBottomCarriageBrakeSolenoid,
            kBottomCarriageEncoder, kBottomCarriageHome);
    public static PowerDistributionPanel kPDP = new PowerDistributionPanel();
}
