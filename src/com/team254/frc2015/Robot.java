
package com.team254.frc2015;

import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.Elevator;
import com.team254.frc2015.web.WebServer;
import com.team254.lib.util.Logger;
import com.team254.lib.util.Serializable;
import com.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

import com.team254.lib.util.LidarLiteSensor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    int i = 0;
    Drive drive = HardwareAdaptor.drive;
    Elevator elevator = HardwareAdaptor.elevator;
    CheesyDriveHelper cdh = new CheesyDriveHelper(drive);
    Joystick leftStick = new Joystick(0);
    Joystick rightStick = new Joystick(1);
    Joystick buttonBoard = new Joystick(2);
    PowerDistributionPanel pdp = HardwareAdaptor.pdp;
    LidarLiteSensor mLidarLiteSensor = new LidarLiteSensor(I2C.Port.kMXP);
    
    // TODO: Refactor this into its own subsystem
    Solenoid intakeSolenoid = new Solenoid(1);
    Talon intakeMotorRight = new Talon(4);
    Talon intakeMotorLeft = new Talon(1);
    boolean intakeSolenoidOn = false;

    static {
        SystemManager.getInstance().add(RobotData.robotTime);
        SystemManager.getInstance().add(RobotData.batteryVoltage);
        SystemManager.getInstance().add(Logger.getInstance());
    }

    @Override
    public void robotInit() {
        mLidarLiteSensor.start();
        WebServer.startServer();
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        cdh.cheesyDrive(leftStick.getY(), -rightStick.getX(), rightStick.getRawButton(1), true);
        elevator.setElevator(buttonBoard.getRawAxis(3) < 0.5);
        
        // TODO: Refactor this stuff
        if (buttonBoard.getRawButton(2)) {
        	intakeMotorRight.set(1.0);
        	intakeMotorLeft.set(1.0);
        } else if (buttonBoard.getRawButton(3)) {
        	intakeMotorRight.set(-1.0);
        	intakeMotorLeft.set(-1.0);
        } else {
        	intakeMotorRight.set(0.0);
        	intakeMotorLeft.set(0.0);
        }
        
        if (buttonBoard.getRawButton(5)){
        	intakeSolenoidOn = true;
        } else if (buttonBoard.getRawButton(6)) {
        	intakeSolenoidOn = false;
        }
        this.intakeSolenoid.set(intakeSolenoidOn);
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {

    }

    @Override
    public void disabledPeriodic() {
        i++;
        WebServer.updateAllStateStreams();
        Logger.println(SystemManager.getInstance().get().toJSONString());
    }
}
