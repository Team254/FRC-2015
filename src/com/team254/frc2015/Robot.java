package com.team254.frc2015;

import com.team254.frc2015.auto.AutoModeExecuter;
import com.team254.frc2015.auto.AutoModeSelector;
import com.team254.frc2015.auto.modes.Test3BinAutoMode;
import com.team254.frc2015.auto.modes.TestDriveAutoMode;
import com.team254.frc2015.auto.modes.TestElevatorAutoMode;
import com.team254.frc2015.auto.modes.TestTurnAutoMode;
import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.frc2015.web.WebServer;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.LidarLiteSensor;
import com.team254.lib.util.Looper;
import com.team254.lib.util.MultiLooper;
import com.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Robot extends IterativeRobot {
    MultiLooper looper = new MultiLooper("Controllers", 1 / 200.0);
    Looper logUpdater = new Looper("Updater", new Updater(), 1 / 50.0);

    AutoModeExecuter autoModeRunner = new AutoModeExecuter();

    Drive drive = HardwareAdaptor.kDrive;
    ElevatorCarriage top_carriage = HardwareAdaptor.kTopCarriage;
    ElevatorCarriage bottom_carriage = HardwareAdaptor.kBottomCarriage;
    PowerDistributionPanel pdp = HardwareAdaptor.kPDP;

    BehaviorManager behavior_manager = new BehaviorManager();
    OperatorInterface operator_interface = new OperatorInterface();

    CheesyDriveHelper cdh = new CheesyDriveHelper(drive);

    Joystick leftStick = HardwareAdaptor.kLeftStick;
    Joystick rightStick = HardwareAdaptor.kRightStick;
    Joystick buttonBoard = HardwareAdaptor.kButtonBoard;

    LidarLiteSensor mLidarLiteSensor = new LidarLiteSensor(I2C.Port.kMXP);

    private int disabledIterations = 0;

    static {
        SystemManager.getInstance().add(new RobotData());
    }

    @Override
    public void robotInit() {
        System.out.println("Start robotInit()");
        mLidarLiteSensor.start();
        HardwareAdaptor.kGyroThread.start();
        WebServer.startServer();
        looper.addLoopable(top_carriage);
        looper.addLoopable(bottom_carriage);
        looper.addLoopable(drive);
        logUpdater.start();

    }

    @Override
    public void autonomousInit() {
        System.out.println("Start autonomousInit()");
        HardwareAdaptor.kGyroThread.reset();
        HardwareAdaptor.kLeftDriveEncoder.reset();
        HardwareAdaptor.kRightDriveEncoder.reset();
        //autoModeRunner.setAutoMode(new Test3BinAutoMode());
        autoModeRunner.setAutoMode(AutoModeSelector.getInstance().getAutoMode());
        autoModeRunner.start();

        /*
         * // JARED TESTING boolean set_for_top = top_carriage.getSetpoint().pos
         * > Constants.kTopCarriageHomePositionInches + 1;
         * SafeElevatorSetpointGenerator.Setpoints setpoints = new
         * SafeElevatorSetpointGenerator.Setpoints(); setpoints.bottom_setpoint
         * = Optional.of(set_for_top ?
         * Constants.kBottomCarriageHomePositionInches : 40.0);
         * setpoints.top_setpoint = Optional.of(set_for_top ?
         * Constants.kTopCarriageHomePositionInches : 65.0); setpoints =
         * SafeElevatorSetpointGenerator .generateSafeSetpoints(setpoints);
         * top_carriage.setPositionSetpoint(setpoints.top_setpoint.get(),
         * false);
         * bottom_carriage.setPositionSetpoint(setpoints.bottom_setpoint.get(),
         * false);
         */
        looper.start();

    }

    @Override
    public void autonomousPeriodic() {
        /*
         * // JARED TESTING System.out.println("Top elevator height: " +
         * top_carriage.getHeight() + ", goal: " +
         * top_carriage.getSetpoint().pos + ", command: " +
         * top_carriage.getCommand() + ", brake: " + top_carriage.getBrake());
         * System.out.println("Bottom elevator height: " +
         * bottom_carriage.getHeight() + ", goal: " +
         * bottom_carriage.getSetpoint().pos + ", command: " +
         * bottom_carriage.getCommand() + ", brake: " +
         * bottom_carriage.getBrake());
         */
    }

    @Override
    public void teleopInit() {
        System.out.println("Start teleopInit()");
        looper.start();
    }

    @Override
    public void teleopPeriodic() {
        cdh.cheesyDrive(-leftStick.getY(), rightStick.getX(),
                rightStick.getRawButton(1), true);
        behavior_manager.update(operator_interface.getCommands());

        // Temp for testing

        if (buttonBoard.getRawAxis(3) < 0.1) {
            if (buttonBoard.getRawButton(12)) {
                top_carriage.setOpenLoop(.5, false);
            } else if (buttonBoard.getRawButton(11)) {
                top_carriage.setOpenLoop(-.5, false);
            } else {
                top_carriage.setOpenLoop(0, true);
            }
            bottom_carriage.setOpenLoop(0, true);
        } else {

            if (buttonBoard.getRawButton(12)) {
                bottom_carriage.setOpenLoop(.5, false);
            } else if (buttonBoard.getRawButton(11)) {
                bottom_carriage.setOpenLoop(-.5, false);
            } else {
                bottom_carriage.setOpenLoop(0, true);
            }
            top_carriage.setOpenLoop(0, true);
        }
    }

    @Override
    public void disabledInit() {
        System.out.println("Start disabledInit()");

        // Stop auto mode
        autoModeRunner.stop();

        // Stop control loops
        looper.stop();

        // Stop drive
        drive.setOpenLoop(DriveSignal.NEUTRAL);

        // Reload constants
        drive.reloadConstants();
        top_carriage.reloadConstants();
        bottom_carriage.reloadConstants();
    }

    @Override
    public void disabledPeriodic() {
        disabledIterations++;
        if (disabledIterations % 50 == 0) {
            System.gc();
        }
        // System.out.println("Gyro hasdata: " +
        // HardwareAdaptor.kGyroThread.hasData() + ", Angle: " +
        // HardwareAdaptor.kGyroThread.getAngle() + ", Rate: " +
        // HardwareAdaptor.kGyroThread.getRate());
    }

}
