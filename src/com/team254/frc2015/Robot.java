package com.team254.frc2015;

import java.util.Optional;

import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.ElevatorCarriage;
import com.team254.frc2015.web.WebServer;
import com.team254.lib.util.Logger;
import com.team254.lib.util.MultiLooper;
import com.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import com.team254.lib.util.LidarLiteSensor;

public class Robot extends IterativeRobot {
    MultiLooper looper = new MultiLooper(1 / 200.0);

    Drive drive = HardwareAdaptor.kDrive;
    ElevatorCarriage top_carriage = HardwareAdaptor.kTopCarriage;
    ElevatorCarriage bottom_carriage = HardwareAdaptor.kBottomCarriage;
    PowerDistributionPanel pdp = HardwareAdaptor.kPDP;

    CheesyDriveHelper cdh = new CheesyDriveHelper(drive);

    Joystick leftStick = new Joystick(0);
    Joystick rightStick = new Joystick(1);
    Joystick buttonBoard = new Joystick(2);

    LidarLiteSensor mLidarLiteSensor = new LidarLiteSensor(I2C.Port.kMXP);

    static {
        SystemManager.getInstance().add(RobotData.robotTime);
        SystemManager.getInstance().add(RobotData.batteryVoltage);
        SystemManager.getInstance().add(Logger.getInstance());
    }

    @Override
    public void robotInit() {
        System.out.println("Start robotInit()");
        mLidarLiteSensor.start();
        WebServer.startServer();
        looper.addLoopable(top_carriage);
        looper.addLoopable(bottom_carriage);
    }

    @Override
    public void autonomousInit() {
        System.out.println("Start autonomousInit()");
        /*
        // JARED TESTING
        SafeElevatorSetpointGenerator.Setpoints setpoints = new SafeElevatorSetpointGenerator.Setpoints();
        setpoints.bottom_setpoint = Optional.of(40.0);
        setpoints.top_setpoint = Optional.of(64.0);
        setpoints = SafeElevatorSetpointGenerator
                .generateSafeSetpoints(setpoints);
        top_carriage.setPositionSetpoint(setpoints.top_setpoint.get(), true);
        bottom_carriage.setPositionSetpoint(setpoints.bottom_setpoint.get(),
                true);

        looper.start();
        */
    }

    @Override
    public void autonomousPeriodic() {
        // JARED TESTING
        System.out.println("Top elevator height: " + top_carriage.getHeight()
                + ", goal: " + top_carriage.getSetpoint().pos + ", command: "
                + top_carriage.getCommand() + ", brake: " + top_carriage.getBrake());
        System.out.println("Bottom elevator height: "
                + bottom_carriage.getHeight() + ", goal: "
                + bottom_carriage.getSetpoint().pos + ", command: "
                + bottom_carriage.getCommand() + ", brake: " + bottom_carriage.getBrake());
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
        double bottom_speed = buttonBoard.getRawButton(9) ? .25 : buttonBoard.getRawButton(10) ? -.25 : 0;
        bottom_carriage.setOpenLoop(bottom_speed,false);
        double top_speed = buttonBoard.getRawButton(7) ? -.25 : buttonBoard.getRawButton(8) ? .25 : 0;
        top_carriage.setOpenLoop(top_speed,false);
    }

    @Override
    public void disabledInit() {
        System.out.println("Start disabledInit()");
        looper.stop();

        drive.reloadConstants();
        top_carriage.reloadConstants();
        bottom_carriage.reloadConstants();
    }

    @Override
    public void disabledPeriodic() {
        WebServer.updateAllStateStreams();
        Logger.println(SystemManager.getInstance().get().toJSONString());
    }
}
