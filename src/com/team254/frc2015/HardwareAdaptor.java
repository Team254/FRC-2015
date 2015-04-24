package com.team254.frc2015;

import com.team254.frc2015.subsystems.*;
import com.team254.lib.util.CheesyCompressor;
import com.team254.lib.util.CheesySolenoid;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.gyro.GyroThread;
import edu.wpi.first.wpilibj.*;

public class HardwareAdaptor {
    // Motors
    static CheesySpeedController kLeftDriveMotor = new CheesySpeedController(
            new VictorSP(Constants.kLeftDriveMotorPWM), new int[]{
            Constants.kLeftDriveMotor1PDP,
            Constants.kLeftDriveMotor2PDP});
    static CheesySpeedController kRightDriveMotor = new CheesySpeedController(
            new VictorSP(Constants.kRightDriveMotorPWM), new int[]{
            Constants.kRightDriveMotor2PDP,
            Constants.kRightDriveMotor2PDP});
    public static CheesySpeedController kTopCarriageMotor = new CheesySpeedController(
            new SpeedController[]{
                    new VictorSP(Constants.kTopCarriageMotor1PWM),
                    new VictorSP(Constants.kTopCarriageMotor2PWM)}, new int[]{
            Constants.kTopCarriageMotor1PDP,
            Constants.kTopCarriageMotor2PDP});
    public static CheesySpeedController kBottomCarriageMotor = new CheesySpeedController(
            new SpeedController[]{
                    new VictorSP(Constants.kBottomCarriageMotor1PWM),
                    new VictorSP(Constants.kBottomCarriageMotor2PWM)},
            new int[]{Constants.kBottomCarriageMotor1PDP,
                    Constants.kBottomCarriageMotor2PDP});
    static CheesySpeedController kLeftIntakeMotor = new CheesySpeedController(
            new VictorSP(Constants.kLeftIntakeMotorPWM),
            Constants.kLeftIntakeMotorPDP);
    static CheesySpeedController kRightIntakeMotor = new CheesySpeedController(
            new VictorSP(Constants.kRightIntakeMotorPWM),
            Constants.kRightIntakeMotorPDP);
    static CheesySpeedController kLeftPeacockMotor = new CheesySpeedController(
            new VictorSP(Constants.kLeftPeacockMotorPWM),
            Constants.kLeftPeacockMotorPDP);
    static CheesySpeedController kRightPeacockMotor = new CheesySpeedController(
            new VictorSP(Constants.kRightPeacockMotorPWM),
            Constants.kRightPeacockMotorPDP);

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
    static CheesySolenoid kTopCarriageGrabberCloseSolenoid = new CheesySolenoid(
            Constants.kTopCarriageGrabberCloseSolenoidPort);
    static CheesySolenoid kTopCarriageGrabberOpenSolenoid = new CheesySolenoid(
            Constants.kTopCarriageGrabberOpenSolenoidPort);
    static CheesySolenoid kBottomCarriagePusherSolenoid = new CheesySolenoid(
            Constants.kBottomCarriagePusherSolenoidPort);
    static CheesySolenoid kBottomCarriageFlapperSolenoid = new CheesySolenoid(
            Constants.kBottomCarriageFlapperSolenoidPort);
    static CheesySolenoid kIntakeSolenoid = new CheesySolenoid(
            Constants.kIntakeSolenoidPort);
    static CheesySolenoid kCoopSolenoid = new CheesySolenoid(
            Constants.kCoopSolenoidPort);
    static CheesySolenoid kPinballWizardSolenoid = new CheesySolenoid(
            Constants.kPinballWizardSolenoidPort);
    static CheesySolenoid kPeacockSolenoid = new CheesySolenoid(
            Constants.kPeacockSolenoidPort);

    // Sensors
    public static GyroThread kGyroThread = new GyroThread();
    public static AnalogInput kBreakbeamTopCarriage = new AnalogInput(5);
    public static AnalogInput kBreakbeamIntake = new AnalogInput(4);

    // Subsystems
    public static Drive kDrive = new Drive("drive", kLeftDriveMotor,
            kRightDriveMotor, kLeftDriveEncoder, kRightDriveEncoder,
            kGyroThread);
    public static TopCarriage kTopCarriage = new TopCarriage("top_carriage",
            kTopCarriageMotor, kTopCarriageBrakeSolenoid, kTopCarriageEncoder,
            kTopCarriageHome, kTopCarriagePivotSolenoid,
            kTopCarriageGrabberOpenSolenoid, kTopCarriageGrabberCloseSolenoid, kBreakbeamTopCarriage);
    public static BottomCarriage kBottomCarriage = new BottomCarriage(
            "bottom_carriage", kBottomCarriageMotor,
            kBottomCarriageBrakeSolenoid, kBottomCarriageEncoder,
            kBottomCarriageHome, kBottomCarriagePusherSolenoid,
            kBottomCarriageFlapperSolenoid);
    public static Intake kIntake = new Intake("intake", kIntakeSolenoid, kCoopSolenoid, kPinballWizardSolenoid,
            kLeftIntakeMotor, kRightIntakeMotor, kBreakbeamIntake);
    public static AirPeacock kAirPeacock = new AirPeacock(kPeacockSolenoid);
    public static MotorPeacock kMotorPeacock = new MotorPeacock(kLeftPeacockMotor, kRightPeacockMotor);
    public static PowerDistributionPanel kPDP = new PowerDistributionPanel();

    // Compressor
    public static Relay kCompressorRelay = new Relay(Constants.kCompressorRelayPort);
    public static DigitalInput kCompressorSwitch = new DigitalInput(Constants.kPressureSwitchDIO);
    public static CheesyCompressor kCompressor = new CheesyCompressor(kCompressorRelay, kCompressorSwitch);

    // Interface
    public static Joystick kLeftStick = new Joystick(0);
    public static Joystick kRightStick = new Joystick(1);
    public static Joystick kButtonBoard = new Joystick(2);

    static {
        kBottomCarriageMotor.setInverted(true);
        kLeftIntakeMotor.setInverted(true);
    }
}
