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
    MultiLooper slowLooper = new MultiLooper("SlowControllers", 1 / 100.0);
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
        //mLidarLiteSensor.start();
        HardwareAdaptor.kGyroThread.start();
        WebServer.startServer();
        looper.addLoopable(top_carriage);
        looper.addLoopable(bottom_carriage);
        slowLooper.addLoopable(drive);
        logUpdater.start();

    }

    @Override
    public void autonomousInit() {
        System.out.println("Start autonomousInit()");
        HardwareAdaptor.kGyroThread.rezero();
        HardwareAdaptor.kGyroThread.reset();
        HardwareAdaptor.kLeftDriveEncoder.reset();
        HardwareAdaptor.kRightDriveEncoder.reset();
        //autoModeRunner.setAutoMode(new Test3BinAutoMode());
        autoModeRunner.setAutoMode(AutoModeSelector.getInstance().getAutoMode());
        autoModeRunner.start();
        looper.start();
        slowLooper.start();
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        System.out.println("Start teleopInit()");
        looper.start();
    }

    @Override
    public void teleopPeriodic() {
        cdh.cheesyDrive(-leftStick.getY(), rightStick.getX(), rightStick.getRawButton(1), true);
        behavior_manager.update(operator_interface.getCommands());
    }

    @Override
    public void disabledInit() {
        System.out.println("Start disabledInit()");

        // Stop auto mode
        autoModeRunner.stop();

        // Stop control loops
        looper.stop();
        slowLooper.stop();

        // Stop controllers
        drive.setOpenLoop(DriveSignal.NEUTRAL);
        top_carriage.setOpenLoop(0.0, true);
        bottom_carriage.setOpenLoop(0.0, true);

        // Reload constants
        drive.reloadConstants();
        top_carriage.reloadConstants();
        bottom_carriage.reloadConstants();
        System.out.println("end disable init!");
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
