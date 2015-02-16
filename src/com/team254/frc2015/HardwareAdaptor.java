package com.team254.frc2015;

import com.team254.frc2015.subsystems.BottomCarriage;
import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.frc2015.subsystems.Intake;
import com.team254.frc2015.subsystems.TopCarriage;
import com.team254.lib.util.CheesySolenoid;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.gyro.GyroThread;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

public class HardwareAdaptor {
    // Motors
    static CheesySpeedController kLeftDriveMotor = new CheesySpeedController(
            new VictorSP(Constants.kLeftDriveMotorPWM), new int[] {
                    Constants.kLeftDriveMotor1PDP,
                    Constants.kLeftDriveMotor2PDP });
    static CheesySpeedController kRightDriveMotor = new CheesySpeedController(
            new VictorSP(Constants.kRightDriveMotorPWM), new int[] {
                    Constants.kRightDriveMotor2PDP,
                    Constants.kRightDriveMotor2PDP });
    public static CheesySpeedController kTopCarriageMotor = new CheesySpeedController(
            new SpeedController[] {
                    new VictorSP(Constants.kTopCarriageMotor1PWM),
                    new VictorSP(Constants.kTopCarriageMotor2PWM) }, new int[] {
                    Constants.kTopCarriageMotor1PDP,
                    Constants.kTopCarriageMotor2PDP });
    public static CheesySpeedController kBottomCarriageMotor = new CheesySpeedController(
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
            Constants.kBottomCarriageEncoderDIOB, true);
    static Encoder kTopCarriageEncoder = new Encoder(
            Constants.kTopCarriageEncoderDIOA,
            Constants.kTopCarriageEncoderDIOB);
    static DigitalInput kBottomCarriageHome = new DigitalInput(
            Constants.kBottomCarriageHomeDIO);
    static DigitalInput kTopCarriageHome = new DigitalInput(
            Constants.kTopCarriageHomeDIO);

    // Solenoids
    static CheesySolenoid kBottomCarriageBrakeSolenoid = new CheesySolenoid(
            Constants.kBottomCarriageBrakeSolenoidPort);
    static CheesySolenoid kTopCarriageBrakeSolenoid = new CheesySolenoid(
            Constants.kTopCarriageBrakeSolenoidPort);
    static CheesySolenoid kTopCarriagePivotSolenoid = new CheesySolenoid(
            Constants.kTopCarriagePivotSolenoidPort);
    static CheesySolenoid kTopCarriageGrabberSolenoid = new CheesySolenoid(
            Constants.kTopCarriageGrabberSolenoidPort);
    static CheesySolenoid kBottomCarriagePusherSolenoid = new CheesySolenoid(
            Constants.kBottomCarriagePusherSolenoidPort);
    static CheesySolenoid kBottomCarriageFlapperSolenoid = new CheesySolenoid(
            Constants.kBottomCarriageFlapperSolenoidPort);
    static CheesySolenoid kLeftIntakeSolenoid = new CheesySolenoid(
            Constants.kLeftIntakeSolenoidPort);
    static CheesySolenoid kRightIntakeSolenoid = new CheesySolenoid(
            Constants.kRightIntakeSolenoidPort);

    // Sensors
    public static GyroThread kGyroThread = new GyroThread();

    // Subsystems
    public static Drive kDrive = new Drive("drive", kLeftDriveMotor,
            kRightDriveMotor, kLeftDriveEncoder, kRightDriveEncoder,
            kGyroThread);
    public static TopCarriage kTopCarriage = new TopCarriage("top_carriage",
            kTopCarriageMotor, kTopCarriageBrakeSolenoid, kTopCarriageEncoder,
            kTopCarriageHome, kTopCarriagePivotSolenoid,
            kTopCarriageGrabberSolenoid);
    public static BottomCarriage kBottomCarriage = new BottomCarriage(
            "bottom_carriage", kBottomCarriageMotor,
            kBottomCarriageBrakeSolenoid, kBottomCarriageEncoder,
            kBottomCarriageHome, kBottomCarriagePusherSolenoid,
            kBottomCarriageFlapperSolenoid);
    public static Intake kIntake = new Intake("intake", kLeftIntakeSolenoid,
            kRightIntakeSolenoid, kLeftIntakeMotor, kRightIntakeMotor);
    public static PowerDistributionPanel kPDP = new PowerDistributionPanel();

    // Interface
    public static Joystick kLeftStick = new Joystick(0);
    public static Joystick kRightStick = new Joystick(1);
    public static Joystick kButtonBoard = new Joystick(2);

    static {
        kBottomCarriageMotor.setInverted(true);
        kLeftIntakeMotor.setInverted(true);
    }
}
