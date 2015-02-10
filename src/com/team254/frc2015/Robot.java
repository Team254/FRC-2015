package com.team254.frc2015;

import java.util.Optional;

import com.team254.frc2015.auto.AutoModeExecuter;
import com.team254.frc2015.auto.modes.TestAutoMode;
import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.frc2015.web.WebServer;
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

    CheesyDriveHelper cdh = new CheesyDriveHelper(drive);

    Joystick leftStick = new Joystick(0);
    Joystick rightStick = new Joystick(1);
    Joystick buttonBoard = new Joystick(2);

    LidarLiteSensor mLidarLiteSensor = new LidarLiteSensor(I2C.Port.kMXP);
    
    private int disabledIterations = 0;

    static {
        SystemManager.getInstance().add(new RobotData());
    }

    @Override
    public void robotInit() {
        System.out.println("Start robotInit()");
        mLidarLiteSensor.start();
        WebServer.startServer();
        looper.addLoopable(top_carriage);
        looper.addLoopable(bottom_carriage);
        logUpdater.start();
    }

    @Override
    public void autonomousInit() {
        System.out.println("Start autonomousInit()");
        autoModeRunner.setAutoMode(new TestAutoMode());
        autoModeRunner.start();
        
        /*
        // JARED TESTING
        boolean set_for_top = top_carriage.getSetpoint().pos > Constants.kTopCarriageHomePositionInches + 1;
        SafeElevatorSetpointGenerator.Setpoints setpoints = new SafeElevatorSetpointGenerator.Setpoints();
        setpoints.bottom_setpoint = Optional.of(set_for_top ? Constants.kBottomCarriageHomePositionInches : 40.0);
        setpoints.top_setpoint = Optional.of(set_for_top ? Constants.kTopCarriageHomePositionInches : 65.0);
        setpoints = SafeElevatorSetpointGenerator
                .generateSafeSetpoints(setpoints);
        top_carriage.setPositionSetpoint(setpoints.top_setpoint.get(), false);
        bottom_carriage.setPositionSetpoint(setpoints.bottom_setpoint.get(),
                false);
        */
        looper.start();

        
    }

    @Override
    public void autonomousPeriodic() {
    	/*
        // JARED TESTING
        System.out.println("Top elevator height: " + top_carriage.getHeight()
                + ", goal: " + top_carriage.getSetpoint().pos + ", command: "
                + top_carriage.getCommand() + ", brake: " + top_carriage.getBrake());
        System.out.println("Bottom elevator height: "
                + bottom_carriage.getHeight() + ", goal: "
                + bottom_carriage.getSetpoint().pos + ", command: "
                + bottom_carriage.getCommand() + ", brake: " + bottom_carriage.getBrake());
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
        double bottom_speed = buttonBoard.getRawButton(9) ? Constants.kOpenLoopCarriageDriveSpeed : buttonBoard.getRawButton(10) ? -Constants.kOpenLoopCarriageDriveSpeed : 0;
        bottom_carriage.setOpenLoop(bottom_speed,false);
        double top_speed = buttonBoard.getRawButton(7) ? -Constants.kOpenLoopCarriageDriveSpeed : buttonBoard.getRawButton(8) ? Constants.kOpenLoopCarriageDriveSpeed : 0;
        top_carriage.setOpenLoop(top_speed,false);
    }

    @Override
    public void disabledInit() {
        System.out.println("Start disabledInit()");
        
        autoModeRunner.stop();
        
        looper.stop();

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
    }
    
}
