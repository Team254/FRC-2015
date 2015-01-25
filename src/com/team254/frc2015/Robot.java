package com.team254.frc2015;

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
	}

	@Override
	public void autonomousInit() {
		System.out.println("Start autonomousInit()");
		looper.start();
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
		cdh.cheesyDrive(leftStick.getY(), -rightStick.getX(),
				rightStick.getRawButton(1), true);
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
